package me.furkandgn.physicsdemo.opengl.font.render;

import me.furkandgn.physicsdemo.common.util.Assert;
import me.furkandgn.physicsdemo.opengl.font.domain.FontCharacter;
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

  private final Map<Character, FontCharacter> characters;
  private final Matrix4f projectionMatrix;
  private final Shader shader;

  private int vao, vbo;

  public FontRenderer(Map<Character, FontCharacter> characters, Matrix4f projectionMatrix) {
    Assert.notNull(characters, "Characters cannot be null");
    Assert.notEmpty(characters, "Characters cannot be empty");
    Assert.notNull(projectionMatrix, "Projection matrix cannot be null");
    this.characters = characters;
    this.projectionMatrix = projectionMatrix;
    this.shader = new Shader("/font_vertex.glsl", "/font_fragment.glsl");
    this.shader.create();
  }

  public void init() {
    this.vao = glGenVertexArrays();
    this.vbo = glGenBuffers();
    glBindVertexArray(this.vao);
    glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
    glBufferData(GL_ARRAY_BUFFER, Float.BYTES * 6 * 4, GL_DYNAMIC_DRAW);
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

    for (char c : text.toCharArray()) {
      FontCharacter fontCharacter = this.characters.get(c);
      float xPos = x + fontCharacter.bearing().x * scale;
      float yPos = y - (fontCharacter.size().y) * scale;
      float width = fontCharacter.size().x * scale;
      float height = fontCharacter.size().y * scale;
      float[][] vertices = new float[][]{
        { xPos,     yPos + height,   0.0f, 1.0f },
        { xPos,     yPos,            0.0f, 0.0f },
        { xPos + width, yPos,        1.0f, 0.0f },

        { xPos,     yPos + height,   0.0f, 1.0f },
        { xPos + width, yPos,        1.0f, 0.0f },
        { xPos + width, yPos + height,   1.0f, 1.0f }
      };

      glBindTexture(GL_TEXTURE_2D, fontCharacter.textureId());
      glBindBuffer(GL_ARRAY_BUFFER, this.vbo);

      FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * vertices[0].length);
      for (float[] vertex : vertices) {
        verticesBuffer.put(vertex);
      }
      verticesBuffer.flip();

      glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, verticesBuffer);
      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glDrawArrays(GL_TRIANGLES, 0, 6);
      x += (fontCharacter.advance() >> 6) * scale;
    }

    glDisable(GL_BLEND);
  }
}
