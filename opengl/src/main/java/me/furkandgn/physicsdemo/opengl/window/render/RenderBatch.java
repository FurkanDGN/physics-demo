package me.furkandgn.physicsdemo.opengl.window.render;

import me.furkandgn.physicsdemo.opengl.window.component.Component;
import me.furkandgn.physicsdemo.opengl.window.render.factory.DefaultRenderContextFactory;
import me.furkandgn.physicsdemo.opengl.window.render.factory.RenderContextFactory;
import me.furkandgn.physicsdemo.opengl.window.util.ArrayUtil;
import me.furkandgn.physicsdemo.opengl.window.util.RenderUtil;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.furkandgn.physicsdemo.opengl.Constants.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Furkan Doğan
 */
public class RenderBatch {

  private final int maxBatchSize;
  private final Matrix4f viewMatrix;
  private final Matrix4f projectionMatrix;
  private final RenderContext renderContext;
  private final List<Component> components;

  public RenderBatch(int maxBatchSize,
                     Matrix4f viewMatrix,
                     Matrix4f projectionMatrix) {
    this.maxBatchSize = maxBatchSize;
    this.viewMatrix = viewMatrix;
    this.projectionMatrix = projectionMatrix;
    this.components = new ArrayList<>();
    RenderContextFactory renderContextFactory = new DefaultRenderContextFactory();
    this.renderContext = renderContextFactory.createRenderContext();
  }

  public void add(Component component) {
    this.components.add(component);
    this.updateRenderContext();
  }

  public void update() {
    for (int i = 0; i < this.components.size(); i++) {
      Component component = this.components.get(i);
      if (component.shouldDestroy()) {
        this.components.remove(component);
        this.updateDraw();
      } else if (component.shouldUpdate()) {
        this.updateDraw();
      }
    }
  }

  public void render() {
    this.renderComponents();
  }

  public boolean hasRoom() {
    return this.components.size() < this.maxBatchSize;
  }

  private void updateDraw() {
    this.updateVertices();
    this.updateIndices();
    this.renderContext.refreshBufferData(true);
  }

  private void updateRenderContext() {
    this.updateVertices();
    this.updateIndices();
    this.setupGl();
  }

  private void renderComponents() {
    if (this.renderContext.refreshBufferData()) {
      this.refreshBuffer();
    }

    RenderUtil.render(this.renderContext, this.viewMatrix, this.projectionMatrix);
    this.unbindVertexArrayAndBuffers();
  }

  private void updateVertices() {
    List<Float> floats = new ArrayList<>();
    for (Component component : this.components) {
      float[] vertices = component.verticesFactory().createVertices();
      for (float vertex : vertices) {
        floats.add(vertex);
      }
    }

    float[] vertices = this.renderContext.vertices();
    float[] currentVertices = ArrayUtil.floatListToArray(floats);

    if (currentVertices.length > vertices.length) {
      vertices = Arrays.copyOf(vertices, currentVertices.length);
    }

    System.arraycopy(currentVertices, 0, vertices, 0, currentVertices.length);
    if (currentVertices.length < vertices.length) {
      Arrays.fill(vertices, currentVertices.length, vertices.length, 0);
    }

    this.renderContext.vertices(vertices);
  }

  private void updateIndices() {
    int[] indices = this.renderContext.indices();
    int offset = 0;
    int previousDotCount = 0;

    for (Component component : this.components) {
      int[] componentIndices = component.indicesFactory().createIndices(component.body());

      if (componentIndices.length + offset > indices.length) {
        indices = Arrays.copyOf(indices, indices.length + componentIndices.length);
      }

      for (int i = 0; i < componentIndices.length; i++) {
        indices[offset + i] = componentIndices[i] + previousDotCount;
      }

      offset += componentIndices.length;
      previousDotCount += component.dotCount();
    }

    if (offset < indices.length) {
      Arrays.fill(indices, offset, indices.length, 0);
    }

    this.renderContext.indices(indices);
  }

  private void setupGl() {
    this.setupVertexArray(this.renderContext);
    this.setupVertexBuffer(this.renderContext);
    this.setupElementBuffer(this.renderContext);

    this.refreshBuffer();
    this.unbindVertexArrayAndBuffers();
  }

  private void refreshBuffer() {
    float[] vertices = this.renderContext.vertices();
    int[] indices = this.renderContext.indices();
    int vboId = this.renderContext.vboId();
    int eboId = this.renderContext.eboId();

    this.setupVertexArray(this.renderContext);

    glBindBuffer(GL_ARRAY_BUFFER, vboId);
    glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
    glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indices);

    this.renderContext.refreshBufferData(false);
  }

  private void setupVertexArray(RenderContext renderContext) {
    int vaoID = renderContext.vaoId();
    glBindVertexArray(vaoID);
  }

  private void setupVertexBuffer(RenderContext renderContext) {
    int vboID = renderContext.vboId();
    float[] vertices = renderContext.vertices();

    glBindBuffer(GL_ARRAY_BUFFER, vboID);
    glBufferData(GL_ARRAY_BUFFER, (long) this.maxBatchSize * vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
  }

  private void setupElementBuffer(RenderContext renderContext) {
    int eboID = renderContext.eboId();
    int[] indices = renderContext.indices();

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, (long) Integer.BYTES * indices.length * this.maxBatchSize, GL_DYNAMIC_DRAW);

    glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
    glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
  }

  private void unbindVertexArrayAndBuffers() {
    glBindVertexArray(0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
  }
}