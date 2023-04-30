package me.furkandgn.physicsdemo.opengl.window.render.factory;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.misc.Producer;
import me.furkandgn.physicsdemo.opengl.window.component.sprite.SpriteComponent;
import me.furkandgn.physicsdemo.opengl.window.render.RenderContext;

import static me.furkandgn.physicsdemo.opengl.Constants.*;
import static me.furkandgn.physicsdemo.opengl.Constants.COLOR_OFFSET;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * @author Furkan Doğan
 */
public class DefaultRenderContextFactory implements RenderContextFactory {

  private final Producer<SpriteComponent, float[]> verticesProducer;
  private final Producer<SpriteComponent, int[]> indicesProducer;

  public DefaultRenderContextFactory(Producer<SpriteComponent, float[]> verticesProducer,
                                     Producer<SpriteComponent, int[]> indicesProducer) {
    this.verticesProducer = verticesProducer;
    this.indicesProducer = indicesProducer;
  }

  @Override
  public RenderContext createRenderContext(SpriteComponent spriteComponent, int index) {
    Class<? extends Body> bodyClass = spriteComponent.body().getClass();
    RenderContext renderContext = new RenderContext(bodyClass);

    this.assignIds(renderContext);
    this.upsertVertices(spriteComponent, renderContext, index);
    this.insertIndices(spriteComponent, renderContext);
    this.setupVertexBuffer(renderContext);
    this.setupElementBuffer(renderContext);
    this.setupVertexArray(renderContext);
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

  private void upsertVertices(SpriteComponent spriteComponent, RenderContext renderContext, int index) {
    float[] vertices = renderContext.vertices() != null ? renderContext.vertices() : this.verticesProducer.produce(spriteComponent);
    spriteComponent.verticesFactory().createVertices(vertices, index);
    renderContext.vertices(vertices);
  }

  private void insertIndices(SpriteComponent spriteComponent, RenderContext renderContext) {
    int[] indices = this.indicesProducer.produce(spriteComponent);
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
  }

  private void setupVertexArray(RenderContext renderContext) {
    int vaoID = renderContext.vaoId();
    glBindVertexArray(vaoID);
    glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
    glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
  }

  private void unbindVertexArrayAndBuffers() {
    glBindVertexArray(0);
    glBindBuffer(GL_ARRAY_BUFFER, -1);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, -1);
  }
}
