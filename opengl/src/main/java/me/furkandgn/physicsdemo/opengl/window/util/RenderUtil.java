package me.furkandgn.physicsdemo.opengl.window.util;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.CircleBody;
import me.furkandgn.physicsdemo.common.body.RectBody;
import me.furkandgn.physicsdemo.opengl.Constants;
import me.furkandgn.physicsdemo.opengl.window.render.RenderContext;
import me.furkandgn.physicsdemo.opengl.window.shader.Shader;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glMultiDrawArrays;
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
    if (clazz.isAssignableFrom(CircleBody.class)) {
      renderCircle(renderContext, count);
    } else if (clazz.isAssignableFrom(RectBody.class)) {
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

    int[] first = new int[count];
    int[] counts = new int[count];
    for (int i = 0; i < count; i++) {
      first[i] = i * (Constants.CIRCLE_CORNERS + 2);
      counts[i] = Constants.CIRCLE_CORNERS + 2;
    }

    glMultiDrawArrays(GL_TRIANGLE_FAN, first, counts);

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

    glDrawElements(GL_TRIANGLES, count * 6, GL_UNSIGNED_INT, 0);

    glDisableVertexAttribArray(0);
    glDisableVertexAttribArray(1);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }
}