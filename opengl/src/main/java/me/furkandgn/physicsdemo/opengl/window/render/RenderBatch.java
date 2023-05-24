package me.furkandgn.physicsdemo.opengl.window.render;

import me.furkandgn.physicsdemo.opengl.window.component.Component;
import me.furkandgn.physicsdemo.opengl.window.render.factory.DefaultRenderContextFactory;
import me.furkandgn.physicsdemo.opengl.window.render.factory.RenderContextFactory;
import me.furkandgn.physicsdemo.opengl.window.util.RenderUtil;
import org.joml.Matrix4f;

import java.util.*;

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
  private final Component[] components;
  private final Queue<Component> addQueue;
  private final Queue<Integer> emptyIndexes;

  private int dotCount;
  private int componentCount;
  private volatile boolean refreshBufferData;

  public RenderBatch(int maxBatchSize,
                     Matrix4f viewMatrix,
                     Matrix4f projectionMatrix) {
    this.maxBatchSize = maxBatchSize;
    this.viewMatrix = viewMatrix;
    this.projectionMatrix = projectionMatrix;
    this.components = new Component[this.maxBatchSize];
    this.addQueue = new LinkedList<>();
    this.emptyIndexes = new LinkedList<>();
    for (int i = 0; i < this.maxBatchSize; i++) {
      this.emptyIndexes.add(i);
    }
    RenderContextFactory renderContextFactory = new DefaultRenderContextFactory();
    this.renderContext = renderContextFactory.createRenderContext();
  }

  public void add(Component component) {
    this.addQueue.add(component);

    if (this.dotCount == 0) {
      this.dotCount = component.dotCount();
      this.initRenderContext();
    }
  }

  public void update() {
    this.solveAddQueue();
    this.updateComponents();
  }

  public void render() {
    this.renderComponents();
  }

  public boolean hasRoom(int dotCount) {
    int totalSize = this.componentCount + this.addQueue.size();
    return totalSize < this.maxBatchSize &&
      (this.dotCount == dotCount || this.dotCount == 0);
  }

  private void updateComponents() {
    for (int i = 0; i < this.componentCount; i++) {
      Component component = this.components[i];
      if (component == null) continue;
      this.updateComponent(component, i);
    }
  }

  private void updateComponent(Component component, int index) {
    if (component.shouldUpdate()) {
      this.updateVertices(component, index);
      this.refreshBufferData = true;
    } else if (component.shouldDestroy()) {
      this.deleteComponent(index);
      this.refreshBufferData = true;
    }
  }

  private void solveAddQueue() {
    int size = this.addQueue.size();
    for (int i = 0; i < size; i++) {
      Component component = this.addQueue.poll();
      Integer index = this.emptyIndexes.poll();
      if (component == null || index == null) {
        throw new RuntimeException("component == null || index == null");
      }

      this.components[index] = component;
      this.componentCount++;
      this.updateVertices(component, index);
      this.updateIndices(component, index);
      this.refreshBuffer();
    }
  }

  private void updateVertices(Component component, int index) {
    float[] vertices = this.renderContext.vertices();
    int fromIndex = index * this.dotCount * VERTEX_SIZE;
    float[] componentVertices = component.verticesFactory().createVertices();

    System.arraycopy(componentVertices, 0, vertices, fromIndex, componentVertices.length);
    this.renderContext.vertices(vertices);
  }

  private void updateIndices(Component component, int index) {
    int[] indices = this.renderContext.indices();
    int[] componentIndices = component.indicesFactory().createIndices(component.body());
    int offset = componentIndices.length * index;

    for (int i = 0; i < componentIndices.length; i++) {
      indices[offset + i] = componentIndices[i] + this.dotCount * index;
    }

    this.renderContext.indices(indices);
  }

  private void deleteComponent(int index) {
    this.components[index] = null;
    this.componentCount--;
    float[] vertices = this.renderContext.vertices();
    int fromIndex = index * this.dotCount * VERTEX_SIZE;

    Arrays.fill(vertices, fromIndex, fromIndex + this.dotCount * VERTEX_SIZE, 0);
    this.emptyIndexes.add(index);
  }

  private void renderComponents() {
    if (this.refreshBufferData) {
      this.refreshBuffer();
    }

    RenderUtil.render(this.renderContext, this.viewMatrix, this.projectionMatrix);
    this.unbindVertexArrayAndBuffers();
  }

  private void refreshBuffer() {
    float[] vertices = this.renderContext.vertices();
    int[] indices = this.renderContext.indices();
    int vboId = this.renderContext.vboId();
    int eboId = this.renderContext.eboId();

    this.bindVertexArray(this.renderContext);

    glBindBuffer(GL_ARRAY_BUFFER, vboId);
    glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
    glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indices);

    this.refreshBufferData = false;
  }

  private void initRenderContext() {
    this.initVerticesBuffer();
    this.initIndicesBuffer();
    this.initBuffers();
  }

  private void initBuffers() {
    this.bindVertexArray(this.renderContext);
    this.initVertexBuffer(this.renderContext);
    this.initElementBuffer(this.renderContext);
  }

  private void initVerticesBuffer() {
    float[] vertices = this.createVerticesArray();
    this.renderContext.vertices(vertices);
  }

  private void initIndicesBuffer() {
    int[] indices = this.createIndicesArray();
    this.renderContext.indices(indices);
  }

  private void bindVertexArray(RenderContext renderContext) {
    int vaoID = renderContext.vaoId();
    glBindVertexArray(vaoID);
  }

  private void initVertexBuffer(RenderContext renderContext) {
    int vboID = renderContext.vboId();
    float[] vertices = renderContext.vertices();

    glBindBuffer(GL_ARRAY_BUFFER, vboID);
    glBufferData(GL_ARRAY_BUFFER, (long) this.maxBatchSize * vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
  }

  private void initElementBuffer(RenderContext renderContext) {
    int eboID = renderContext.eboId();
    int[] indices = renderContext.indices();

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, (long) this.maxBatchSize * indices.length * Integer.BYTES, GL_STATIC_DRAW);

    glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
    glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
  }

  private void unbindVertexArrayAndBuffers() {
    glBindVertexArray(0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
  }

  private float[] createVerticesArray() {
    return new float[this.maxBatchSize * this.dotCount * VERTEX_SIZE];
  }

  private int[] createIndicesArray() {
    return new int[this.maxBatchSize * this.dotCount * 3];
  }
}