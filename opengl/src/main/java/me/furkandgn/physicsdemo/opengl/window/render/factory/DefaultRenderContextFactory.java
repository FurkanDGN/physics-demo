package me.furkandgn.physicsdemo.opengl.window.render.factory;

import me.furkandgn.physicsdemo.opengl.window.render.RenderContext;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * @author Furkan Doğan
 */
public class DefaultRenderContextFactory implements RenderContextFactory {

  @Override
  public RenderContext createRenderContext() {
    RenderContext renderContext = new RenderContext();
    this.assignIds(renderContext);
    return renderContext;
  }

  private void assignIds(RenderContext renderContext) {
    int vaoID = glGenVertexArrays();
    int vboID = glGenBuffers();
    int eboID = glGenBuffers();

    renderContext.vaoId(vaoID);
    renderContext.vboId(vboID);
    renderContext.eboId(eboID);
  }
}
