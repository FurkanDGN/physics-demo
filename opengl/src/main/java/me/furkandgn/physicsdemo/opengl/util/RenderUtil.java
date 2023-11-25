package me.furkandgn.physicsdemo.opengl.util;

import me.furkandgn.physicsdemo.opengl.render.RenderContext;
import me.furkandgn.physicsdemo.opengl.shader.Shader;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class RenderUtil {

  private static Shader shader;

  public static void bindShader(Shader shader) {
    RenderUtil.shader = shader;
  }

  public static Shader getBoundShader() {
    return shader;
  }

  public static void render(RenderContext renderContext, Matrix4f viewMatrix, Matrix4f projectionMatrix) {
    Shader shader = RenderUtil.getBoundShader();
    shader.use();
    shader.uploadMat4f("uProjection", projectionMatrix);
    shader.uploadMat4f("uView", viewMatrix);
    glEnable(GL_MULTISAMPLE);
    renderShape(renderContext);
    glDisable(GL_MULTISAMPLE);

    shader.detach();
  }

  private static void renderShape(RenderContext renderContext) {
    glBindVertexArray(renderContext.vaoId());
    glBindBuffer(GL_ARRAY_BUFFER, renderContext.vboId());
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, renderContext.eboId());
    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);
    int length = renderContext.indices().length;

    glDrawElements(GL_TRIANGLES, length, GL_UNSIGNED_INT, 0);
  }
}