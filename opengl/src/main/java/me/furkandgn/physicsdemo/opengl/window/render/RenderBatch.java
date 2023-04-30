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

import static me.furkandgn.physicsdemo.opengl.Constants.VERTEX_SIZE;
import static org.lwjgl.opengl.GL30.*;

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
    this.renderContextFactory = new DefaultRenderContextFactory(this::createVerticesArray, this::createIndicesArray);
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
      this.updateComponent(component, renderContext, i);
      this.checkShouldDelete(components, component, i);
    }
  }

  private void checkShouldDelete(List<Component> components, Component component, int i) {
    if (component.shouldDestroy()) {
      this.deleteVertices(component, i);
      components.remove(component);
    }
  }

  private void updateComponent(Component component, RenderContext renderContext, int i) {
    if (component.shouldUpdate()) {
      this.updateVertices(component, i);
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
    this.createRenderContext(component, index);
  }

  private void createRenderContext(Component component, int index) {
    Class<? extends Body> bodyClass = component.body().getClass();
    this.renderContexts.computeIfAbsent(bodyClass, key -> this.renderContextFactory.createRenderContext(component, index));
  }

  private void updateVertices(Component component, int index) {
    Class<? extends Body> key = component.body().getClass();
    RenderContext renderContext = this.renderContexts.get(key);

    float[] vertices = renderContext.vertices();
    component.verticesFactory().createVertices(vertices, index);
    renderContext.vertices(vertices);
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

  private int[] createIndicesArray(Component component) {
    Class<? extends Body> bodyClass = component.body().getClass();
    int count = this.components.get(bodyClass).size();
    return component.indicesFactory().createIndices(this.maxBatchSize, count);
  }

  private float[] createVerticesArray(Component component) {
    return new float[this.maxBatchSize * component.dotCount() * VERTEX_SIZE];
  }
}