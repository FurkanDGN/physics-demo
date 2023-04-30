package me.furkandgn.physicsdemo.opengl.window.render;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.opengl.window.component.Component;
import me.furkandgn.physicsdemo.opengl.window.render.factory.DefaultRenderContextFactory;
import me.furkandgn.physicsdemo.opengl.window.render.factory.RenderContextFactory;
import me.furkandgn.physicsdemo.opengl.window.util.RenderUtil;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static me.furkandgn.physicsdemo.opengl.Constants.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * @author Furkan Doğan
 */
public class RenderBatch {

  private final int maxBatchSize;
  private final Matrix4f viewMatrix;
  private final Matrix4f projectionMatrix;
  private final Map<Class<?>, RenderContext> renderContexts;
  private final Map<Class<? extends Body>, List<Component>> components;
  private final RenderContextFactory renderContextFactory;

  public RenderBatch(int maxBatchSize,
                     Matrix4f viewMatrix,
                     Matrix4f projectionMatrix) {
    this.maxBatchSize = maxBatchSize;
    this.viewMatrix = viewMatrix;
    this.projectionMatrix = projectionMatrix;
    this.renderContexts = new ConcurrentHashMap<>();
    this.components = new ConcurrentHashMap<>();
    this.renderContextFactory = new DefaultRenderContextFactory();
  }

  public void add(Component component) {
    Class<? extends Body> clazz = component.body().getClass();
    this.components.computeIfAbsent(clazz, key -> new CopyOnWriteArrayList<>()).add(component);
    this.initComponent(component);
  }

  public void update() {
    this.components.forEach(this::updateComponents);
  }

  public void render() {
    this.renderComponents();
  }

  public boolean hasRoom() {
    return this.components.values()
      .stream()
      .mapToInt(List::size)
      .sum() < this.maxBatchSize;
  }

  private void updateComponents(Class<? extends Body> bodyClass, List<Component> components) {
    RenderContext renderContext = this.renderContexts.get(bodyClass);

    for (int i = 0; i < components.size(); i++) {
      Component component = components.get(i);
      this.checkUpdateComponent(component, renderContext, i);
      this.checkShouldDelete(components, component, i);
    }
  }

  private void checkShouldDelete(List<Component> components, Component component, int i) {
    if (component.shouldDestroy()) {
      this.deleteVertices(component, i);
      components.remove(component);
    }
  }

  private void checkUpdateComponent(Component component, RenderContext renderContext, int i) {
    if (component.shouldUpdate()) {
      this.upsertVertices(component, renderContext, i);
      renderContext.refreshBufferData(true);
    }
  }

  private void renderComponents() {
    this.renderContexts.values().forEach(renderContext -> {
      if (renderContext.refreshBufferData()) {
        this.refreshBuffer(renderContext);
      }

      Class<? extends Body> bodyClass = renderContext.clazz();
      int count = this.components.get(bodyClass).size();
      RenderUtil.render(bodyClass, renderContext, this.viewMatrix, this.projectionMatrix, count);
    });
  }

  private void initComponent(Component component) {
    Class<? extends Body> bodyClass = component.body().getClass();
    int index = this.components.get(bodyClass).size() - 1;
    RenderContext renderContext = this.createRenderContext(component, index);
    this.updateRenderContext(component, renderContext, index);
  }

  private RenderContext createRenderContext(Component component, int index) {
    Class<? extends Body> bodyClass = component.body().getClass();
    return this.renderContexts.computeIfAbsent(bodyClass, key -> this.renderContextFactory.createRenderContext(component, index));
  }

  private void updateRenderContext(Component component, RenderContext renderContext, int index) {
    this.upsertVertices(component, renderContext, index);
    this.insertIndices(component, renderContext);
    this.setupGl(renderContext);
  }

  private void setupGl(RenderContext renderContext) {
    this.setupVertexArray(renderContext);
    this.setupVertexBuffer(renderContext);
    this.setupElementBuffer(renderContext);
    this.refreshBuffer(renderContext);
    this.unbindVertexArrayAndBuffers();
  }

  private void deleteVertices(Component component, int index) {
    Class<? extends Body> key = component.body().getClass();
    RenderContext renderContext = this.renderContexts.get(key);
    float[] vertices = renderContext.vertices();
    int dotCount = component.dotCount();

    int fromIndex = index * dotCount * VERTEX_SIZE;
    int toIndex = index * dotCount * VERTEX_SIZE + (VERTEX_SIZE * dotCount);
    Arrays.fill(vertices, fromIndex, toIndex, 0);
    System.arraycopy(vertices, toIndex, vertices, fromIndex, vertices.length - toIndex);

    renderContext.vertices(vertices);
  }

  private void refreshBuffer(RenderContext renderContext) {
    glBindBuffer(GL_ARRAY_BUFFER, renderContext.vboId());
    float[] vertices = renderContext.vertices();
    glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
    renderContext.refreshBufferData(false);
  }

  private void upsertVertices(Component component, RenderContext renderContext, int index) {
    float[] vertices = renderContext.vertices() != null ? renderContext.vertices() : this.createVerticesArray(component);
    component.verticesFactory().createVertices(vertices, index);
    renderContext.vertices(vertices);
  }

  private void insertIndices(Component component, RenderContext renderContext) {
    int[] indices = this.createIndicesArray(component);
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
    glBindVertexArray(0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
  }

  private int[] createIndicesArray(Component component) {
    Class<? extends Body> bodyClass = component.body().getClass();
    int count = this.components.get(bodyClass).size();
    return component.indicesFactory().createIndices(this.maxBatchSize, count);
  }

  private float[] createVerticesArray(Component component) {
    return new float[this.maxBatchSize * component.dotCount() * VERTEX_SIZE];
  }
}