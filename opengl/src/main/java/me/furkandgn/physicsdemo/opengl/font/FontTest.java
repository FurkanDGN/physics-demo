package me.furkandgn.physicsdemo.opengl.font;

import me.furkandgn.freetype2.*;
import me.furkandgn.physicsdemo.opengl.OpenGLApp;
import me.furkandgn.physicsdemo.opengl.font.domain.FontCharacter;
import org.joml.Vector2f;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.glPixelStorei;

/**
 * @author Furkan Doğan
 */
public class FontTest {

  public static void main(String[] args) throws IOException {
    Face face = loadFace("OpenSans-LightItalic.ttf");
    face.setPixelSizes(0, 48);

    int atlasWidth = 1024;
    int atlasHeight = 1128;
    ByteBuffer atlasBuffer = MemoryUtil.memAlloc(atlasWidth * atlasHeight);

    int offsetX = 0, offsetY = 0, rowHeight = 0;
    List<Character> chars = getChars(face);
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
          int index = (offsetX + x) + (offsetY + y) * atlasWidth;
          atlasBuffer.put(index, (byte) value);
        }
      }

      offsetX += bitmapWidth;
      rowHeight = Math.max(rowHeight, bitmapHeight);
    }

    MemoryUtil.memFree(atlasBuffer);

    System.out.println(offsetX);
    System.out.println(offsetY);

    face.delete();
  }

  private static List<Character> getChars(Face face) {
    List<Character> chars = new ArrayList<>();

    int currentChar = face.getFirstChar()[0];
    while (currentChar != 0) {
      chars.add((char) currentChar);
      int[] nextChar = face.getNextChar(currentChar);
      currentChar = nextChar[0];
    }

    return chars;
  }

  private static Face loadFace(String file) throws IOException {
    Library library = Objects.requireNonNull(FreeType.newLibrary(), "Library cannot be loaded");
    InputStream inputStream = Objects.requireNonNull(OpenGLApp.class.getResourceAsStream("/fonts/" + file), "File");
    byte[] bytes = inputStream.readAllBytes();
    return library.newFace(bytes, 0);
  }
}
