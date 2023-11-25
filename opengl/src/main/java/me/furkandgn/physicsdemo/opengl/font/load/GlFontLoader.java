package me.furkandgn.physicsdemo.opengl.font.load;

import me.furkandgn.freetype2.Face;
import me.furkandgn.freetype2.FreeType;
import me.furkandgn.freetype2.FreeTypeConstants;
import me.furkandgn.freetype2.Library;
import me.furkandgn.physicsdemo.opengl.font.domain.FontCharacter;
import org.joml.Vector2f;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

/**
 * @author Furkan Doğan
 */
public class GlFontLoader implements FontLoader {

  private static final String ALPHABET = "abcçdefgğhıijklmnoöprsştuüvwxyzABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVWXYZ0123456789 !";

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
  public Map<Character, FontCharacter> load(InputStream inputStream, int fontSize) {
    Map<Character, FontCharacter> map = new HashMap<>();
    try {
      Face face = this.loadFace(inputStream);
      face.setPixelSizes(0, fontSize);
      glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

      for (char c : ALPHABET.toCharArray()) {
        if (face.loadChar(c, FreeTypeConstants.FT_LOAD_RENDER)) {
          throw new RuntimeException("An error occurred when loading char: " + c);
        }
        int rows = face.getGlyphSlot().getBitmap().getRows();
        int width = face.getGlyphSlot().getBitmap().getWidth();
        int left = face.getGlyphSlot().getBitmapLeft();
        int top = face.getGlyphSlot().getBitmapTop();
        int advance = face.getGlyphSlot().getAdvance().getX();
        ByteBuffer buffer = face.getGlyphSlot().getBitmap().getBuffer();

        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, width, rows, 0, GL_RED, GL_UNSIGNED_BYTE, buffer);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        FontCharacter fontCharacter = new FontCharacter(textureId, new Vector2f(width, rows), new Vector2f(left, top), advance);
        map.put(c, fontCharacter);
      }

      face.delete();

      if (this.deleteFreeType) {
        this.library.delete();
      }

      return map;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setDeleteFreeType(boolean deleteFreeType) {
    this.deleteFreeType = deleteFreeType;
  }

  private Face loadFace(InputStream inputStream) throws IOException {
    byte[] bytes = inputStream.readAllBytes();
    return this.library.newFace(bytes, 0);
  }
}
