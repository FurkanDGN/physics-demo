package me.furkandgn.physicsdemo.opengl.window.util;

import me.furkandgn.physicsdemo.common.Body;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import me.furkandgn.physicsdemo.opengl.window.render.RenderContext;
import me.furkandgn.physicsdemo.opengl.window.shader.Shader;
import org.joml.Matrix4f;
import org.lwjgl.PointerBuffer;

import static me.furkandgn.physicsdemo.opengl.window.constants.Shapes.CIRCLE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
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

  public static void render(Class<? extends Body> clazz, RenderContext renderContext, Matrix4f viewMatrix, Matrix4f projectionMatrix, int count) {
    Shader shader = RenderUtil.getBoundShader();
    shader.use();
    shader.uploadMat4f("uProjection", projectionMatrix);
    shader.uploadMat4f("uView", viewMatrix);
    glEnable(GL_MULTISAMPLE);
    if (clazz.isAssignableFrom(CircleBody.class) || clazz.getSuperclass().isAssignableFrom(CircleBody.class)) {
      renderCircle(renderContext, count);
    } else if (clazz.isAssignableFrom(RectBody.class) || clazz.getSuperclass().isAssignableFrom(RectBody.class)) {
      renderRect(renderContext, count);
    } else {
      throw new RuntimeException("Unsupported body type " + clazz.getSimpleName());
    }

    glDisable(GL_MULTISAMPLE);

    shader.detach();
  }

  private static void renderCircle(RenderContext renderContext, int count) {
    glBindVertexArray(renderContext.vaoId());
    glBindBuffer(GL_ARRAY_BUFFER, renderContext.vboId());
    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);

    for (int i = 0; i < count; i++) {
      long offset = (long) CIRCLE.getDotCount() * i * Integer.BYTES;
      glDrawElements(GL_TRIANGLE_FAN, CIRCLE.getDotCount(), GL_UNSIGNED_INT, offset);
    }

    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }

  private static void renderRect(RenderContext renderContext, int count) {
    int vaoId = renderContext.vaoId();
    glBindVertexArray(vaoId);
    glBindBuffer(GL_ARRAY_BUFFER, renderContext.vboId());
    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);

    glMultiDrawElements(GL_TRIANGLES, new int[]{6 * count}, GL_UNSIGNED_INT, PointerBuffer.allocateDirect(count * Long.BYTES));

    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }
}