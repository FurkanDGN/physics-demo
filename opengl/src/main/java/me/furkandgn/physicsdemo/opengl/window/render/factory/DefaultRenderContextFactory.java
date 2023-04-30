package me.furkandgn.physicsdemo.opengl.window.render.factory;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.misc.Producer;
import me.furkandgn.physicsdemo.opengl.window.component.Component;
import me.furkandgn.physicsdemo.opengl.window.render.RenderContext;

import static me.furkandgn.physicsdemo.opengl.Constants.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * @author Furkan Doğan
 */
public class DefaultRenderContextFactory implements RenderContextFactory {

  private final Producer<Component, float[]> verticesProducer;
  private final Producer<Component, int[]> indicesProducer;

  public DefaultRenderContextFactory(Producer<Component, float[]> verticesProducer,
                                     Producer<Component, int[]> indicesProducer) {
    this.verticesProducer = verticesProducer;
    this.indicesProducer = indicesProducer;
  }

  @Override
  public RenderContext createRenderContext(Component component, int index) {
    Class<? extends Body> bodyClass = component.body().getClass();
    RenderContext renderContext = new RenderContext(bodyClass);

    this.assignIds(renderContext);
    this.upsertVertices(component, renderContext, index);
    this.insertIndices(component, renderContext);
    this.setupVertexArray(renderContext);
    this.setupVertexBuffer(renderContext);
    this.setupElementBuffer(renderContext);
    this.unbindVertexArrayAndBuffers();

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

  private void upsertVertices(Component component, RenderContext renderContext, int index) {
    float[] vertices = renderContext.vertices() != null ? renderContext.vertices() : this.verticesProducer.produce(component);
    component.verticesFactory().createVertices(vertices, index);
    renderContext.vertices(vertices);
  }

  private void insertIndices(Component component, RenderContext renderContext) {
    int[] indices = this.indicesProducer.produce(component);
    renderContext.indices(indices);
  }

  private void setupVertexBuffer(RenderContext renderContext) {
    int vboID = renderContext.vboId();
    float[] vertices = renderContext.vertices();

    glBindBuffer(GL_ARRAY_BUFFER, vboID);
    glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
  }

  private void setupElementBuffer(RenderContext renderContext) {
    int eboID = renderContext.eboId();
    int[] indices = renderContext.indices();

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

    glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
    glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
  }

  private void setupVertexArray(RenderContext renderContext) {
    int vaoID = renderContext.vaoId();
    glBindVertexArray(vaoID);
  }

  private void unbindVertexArrayAndBuffers() {
    glBindBuffer(GL_ARRAY_BUFFER, -1);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, -1);
  }
}
