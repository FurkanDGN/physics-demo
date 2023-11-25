package me.furkandgn.physicsdemo.opengl.font.render;

import me.furkandgn.physicsdemo.common.util.Assert;
import me.furkandgn.physicsdemo.opengl.font.domain.FontCharacter;
import me.furkandgn.physicsdemo.opengl.font.domain.FontAtlasInfo;
import me.furkandgn.physicsdemo.opengl.shader.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.util.Map;

import static me.furkandgn.physicsdemo.opengl.Constants.COLOR_SIZE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * @author Furkan Doğan
 */
public class FontRenderer {

  private final int atlasTextureId;
  private final Map<Character, FontCharacter> characters;
  private final int atlasWidth;
  private final int atlasHeight;
  private final Matrix4f projectionMatrix;
  private final Shader shader;

  private int vao, vbo;

  public FontRenderer(FontAtlasInfo fontAtlasInfo, Matrix4f projectionMatrix) {
    Assert.notNull(fontAtlasInfo, "Font atlas info cannot be null");
    Assert.notNull(fontAtlasInfo.characters(), "Characters cannot be null");
    Assert.notEmpty(fontAtlasInfo.characters(), "Characters cannot be empty");
    Assert.notNull(projectionMatrix, "Projection matrix cannot be null");
    this.atlasTextureId = fontAtlasInfo.atlasTextureId();
    this.characters = fontAtlasInfo.characters();
    this.atlasWidth = fontAtlasInfo.atlasSize().x;
    this.atlasHeight = fontAtlasInfo.atlasSize().y;
    this.projectionMatrix = projectionMatrix;
    this.shader = new Shader("/font_vertex.glsl", "/font_fragment.glsl");
    this.shader.create();
  }

  public void init() {
    this.vao = glGenVertexArrays();
    this.vbo = glGenBuffers();
    glBindVertexArray(this.vao);
    glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
    glBufferData(GL_ARRAY_BUFFER, (long) Float.BYTES * 6 * 4 * this.atlasHeight * this.atlasWidth, GL_DYNAMIC_DRAW);
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, COLOR_SIZE, GL_FLOAT, false, 4 * Float.BYTES, 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }

  public void render(String text, float x, float y, float scale, Vector3f color) {
    this.shader.use();
    this.shader.uploadVec3f("textColor", color);
    this.shader.uploadMat4f("projection", this.projectionMatrix);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    glActiveTexture(GL_TEXTURE0);
    glBindVertexArray(this.vao);

    float[][] vertices = new float[text.toCharArray().length][];

    char[] charArray = text.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      char c = charArray[i];
      FontCharacter fontCharacter = this.characters.get(c);
      float xPos = x + fontCharacter.bearing().x() * scale;
      float yPos = y - (fontCharacter.size().y()) * scale;

      float w = fontCharacter.size().x() * scale;
      float h = fontCharacter.size().y() * scale;

      float atlasX = fontCharacter.coordinate().x() / this.atlasWidth;
      float atlasY = fontCharacter.coordinate().y() / this.atlasHeight;
      float charWidthInAtlas = fontCharacter.size().x() / this.atlasWidth;
      float charHeightInAtlas = fontCharacter.size().y() / this.atlasHeight;

      float[] charVertices = {
        xPos, yPos + h, atlasX, atlasY + charHeightInAtlas,
        xPos, yPos, atlasX, atlasY,
        xPos + w, yPos, atlasX + charWidthInAtlas, atlasY,

        xPos, yPos + h, atlasX, atlasY + charHeightInAtlas,
        xPos + w, yPos, atlasX + charWidthInAtlas, atlasY,
        xPos + w, yPos + h, atlasX + charWidthInAtlas, atlasY + charHeightInAtlas
      };
      vertices[i] = charVertices;

      x += (fontCharacter.advance() >> 6) * scale;
    }

    glBindTexture(GL_TEXTURE_2D, this.atlasTextureId);
    glBindBuffer(GL_ARRAY_BUFFER, this.vbo);

    FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * vertices[0].length);
    for (float[] vertex : vertices) {
      verticesBuffer.put(vertex);
    }
    verticesBuffer.flip();

    glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, verticesBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glDrawArrays(GL_TRIANGLES, 0, 6 * charArray.length);

    glDisable(GL_BLEND);
  }
}
