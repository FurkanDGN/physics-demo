package me.furkandgn.physicsdemo.opengl.font.load;

import me.furkandgn.freetype2.*;
import me.furkandgn.physicsdemo.opengl.font.domain.FontCharacter;
import me.furkandgn.physicsdemo.opengl.font.domain.FontAtlasInfo;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.IntStream;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

/**
 * @author Furkan Doğan
 */
public class GlFontLoader implements FontLoader {

  private final Library library;
  private boolean deleteFreeType;

  public GlFontLoader() {
    this(false);
  }

  public GlFontLoader(boolean deleteFreeType) {
    this.library = Objects.requireNonNull(FreeType.newLibrary(), "Library cannot be loaded");
    this.deleteFreeType = deleteFreeType;
  }

  @Override
  public FontAtlasInfo load(InputStream inputStream, int fontSize) {
    Map<Character, FontCharacter> characters = new HashMap<>();
    try {
      Face face = this.loadFace(inputStream);
      face.setPixelSizes(0, fontSize);
      glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

      int atlasWidth = 1090;
      int atlasHeight = 1090;
      ByteBuffer atlasBuffer = MemoryUtil.memAlloc(atlasWidth * atlasHeight);

      int offsetX = 0, offsetY = 0, rowHeight = 0;
      List<Character> chars = this.getChars(face);
      if (chars.isEmpty()) {
        chars = IntStream.range(32, 1000)
          .mapToObj(operand -> (char) operand)
          .toList();
      }
      for (Character character : chars) {
        if (face.loadChar(character, FreeTypeConstants.FT_LOAD_RENDER)) {
          continue;
        }

        GlyphSlot slot = face.getGlyphSlot();
        Bitmap bitmap = slot.getBitmap();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getRows();

        if (offsetX + bitmapWidth > atlasWidth) {
          offsetX = 0;
          offsetY += rowHeight;
          rowHeight = 0;
        }

        for (int y = 0; y < bitmapHeight; y++) {
          for (int x = 0; x < bitmapWidth; x++) {
            int value = bitmap.getBuffer().get(x + bitmapWidth * y) & 0xFF;
            atlasBuffer.put((offsetY + y) * atlasWidth + (offsetX + x), (byte) value);
          }
        }

        FontCharacter fontCharacter = new FontCharacter(
          new Vector2f(offsetX, offsetY),
          new Vector2f(bitmapWidth, bitmapHeight),
          new Vector2f(slot.getBitmapLeft(), slot.getBitmapTop()),
          slot.getAdvance().getX());

        characters.put(character, fontCharacter);

        offsetX += bitmapWidth + 1;
        rowHeight = Math.max(rowHeight, bitmapHeight);
      }

      int textureId = glGenTextures();
      glBindTexture(GL_TEXTURE_2D, textureId);
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, atlasWidth, atlasHeight, 0, GL_RED, GL_UNSIGNED_BYTE, atlasBuffer);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

      MemoryUtil.memFree(atlasBuffer);
      face.delete();

      if (this.deleteFreeType) {
        this.library.delete();
      }

      return new FontAtlasInfo(textureId, new Vector2i(atlasWidth, atlasHeight), characters);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setDeleteFreeType(boolean deleteFreeType) {
    this.deleteFreeType = deleteFreeType;
  }

  private List<Character> getChars(Face face) {
    List<Character> chars = new ArrayList<>();

    int currentChar = face.getFirstChar()[0];
    while (currentChar != 0) {
      chars.add((char) currentChar);
      int[] nextChar = face.getNextChar(currentChar);
      currentChar = nextChar[0];
    }

    return chars;
  }

  private Face loadFace(InputStream inputStream) throws IOException {
    byte[] bytes = inputStream.readAllBytes();
    return this.library.newFace(bytes, 0);
  }
}
