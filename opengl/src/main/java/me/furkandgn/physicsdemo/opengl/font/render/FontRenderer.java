package me.furkandgn.physicsdemo.opengl.font.render;

import me.furkandgn.physicsdemo.common.util.Assert;
import me.furkandgn.physicsdemo.opengl.font.domain.FontAtlasInfo;
import me.furkandgn.physicsdemo.opengl.font.domain.FontCharacter;
import me.furkandgn.physicsdemo.opengl.shader.Shader;
import me.furkandgn.physicsdemo.opengl.util.BufferUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;

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

  private final FontAtlasInfo atlas;
  private final Matrix4f projectionMatrix;
  private final Shader shader;

  private int vao, vbo;

  public FontRenderer(FontAtlasInfo atlas, Matrix4f projectionMatrix) {
    Assert.notNull(atlas, "Font atlas info cannot be null");
    Assert.notNull(atlas.characters(), "Characters cannot be null");
    Assert.notEmpty(atlas.characters(), "Characters cannot be empty");
    Assert.notNull(projectionMatrix, "Projection matrix cannot be null");
    this.atlas = atlas;
    this.projectionMatrix = projectionMatrix;
    this.shader = new Shader("/font_vertex.glsl", "/font_fragment.glsl");
    this.shader.create();
  }

  public void init() {
    this.vao = glGenVertexArrays();
    this.vbo = glGenBuffers();
    glBindVertexArray(this.vao);
    glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
    glBufferData(GL_ARRAY_BUFFER, (long) Float.BYTES * 6 * 4 * this.atlas.size().x * this.atlas.size().y, GL_DYNAMIC_DRAW);
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, COLOR_SIZE, GL_FLOAT, false, 4 * Float.BYTES, 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }

  public void render(String text, float x, float y, float scale, Vector3f color) {
    this.prepare(color);
    this.renderText(text, x, y, scale);
    this.postRender();
  }

  private void prepare(Vector3f color) {
    this.prepareShader(color);
    this.prepareOpenGl();
  }

  private void renderText(String text, float x, float y, float scale) {
    char[] charArray = text.toCharArray();
    float[][] vertices = this.buildVertices(charArray, x, y, scale);
    this.renderVertices(vertices);
  }

  private void postRender() {
    glDisable(GL_BLEND);
    this.shader.detach();
  }

  private void prepareShader(Vector3f color) {
    this.shader.use();
    this.shader.uploadVec3f("textColor", color);
    this.shader.uploadMat4f("projection", this.projectionMatrix);
  }

  private void prepareOpenGl() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glActiveTexture(GL_TEXTURE0);
    glBindVertexArray(this.vao);
    glBindTexture(GL_TEXTURE_2D, this.atlas.textureId());
    glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
  }

  private float[][] buildVertices(char[] charArray, float x, float y, float scale) {
    float[][] vertices = new float[charArray.length][];

    for (int i = 0; i < charArray.length; i++) {
      char c = charArray[i];
      FontCharacter fontCharacter = this.atlas.characters().get(c);
      if (fontCharacter == null) continue;

      float[] charVertices = this.buildCharVertices(fontCharacter, x, y, scale);
      vertices[i] = charVertices;

      x += (fontCharacter.advance() >> 6) * scale;
    }

    return vertices;
  }

  private void renderVertices(float[][] vertices) {
    FloatBuffer verticesBuffer = BufferUtils.convert(vertices, vertices.length * vertices[0].length);
    glBufferSubData(GL_ARRAY_BUFFER, 0, verticesBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glDrawArrays(GL_TRIANGLES, 0, 6 * vertices.length);
  }

  public float[] buildCharVertices(FontCharacter fontCharacter, float x, float y, float scale) {
    float xPos = x + fontCharacter.bearing().x() * scale;
    float yPos = y - fontCharacter.bearing().y() * scale;
    float w = fontCharacter.size().x() * scale;
    float h = fontCharacter.size().y() * scale;

    float atlasX = fontCharacter.coordinate().x() / this.atlas.size().x;
    float atlasY = fontCharacter.coordinate().y() / this.atlas.size().y;
    float wA = fontCharacter.size().x() / this.atlas.size().x;
    float hA = fontCharacter.size().y() / this.atlas.size().y;

    return new float[]{
      xPos,     yPos + h,    atlasX,      atlasY + hA,
      xPos,     yPos,        atlasX,      atlasY,
      xPos + w, yPos,        atlasX + wA, atlasY,

      xPos,     yPos + h,    atlasX,      atlasY + hA,
      xPos + w, yPos,        atlasX + wA, atlasY,
      xPos + w, yPos + h,    atlasX + wA, atlasY + hA
    };
  }
}
