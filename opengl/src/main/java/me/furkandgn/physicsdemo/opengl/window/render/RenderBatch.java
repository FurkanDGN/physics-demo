package me.furkandgn.physicsdemo.opengl.window.render;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.opengl.window.component.sprite.SpriteComponent;
import me.furkandgn.physicsdemo.opengl.window.render.factory.DefaultRenderContextFactory;
import me.furkandgn.physicsdemo.opengl.window.render.factory.RenderContextFactory;
import me.furkandgn.physicsdemo.opengl.window.util.RenderUtil;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
  private final Map<Class<? extends Body>, List<SpriteComponent>> spriteComponents;
  private final RenderContextFactory renderContextFactory;

  public RenderBatch(int maxBatchSize,
                     Matrix4f viewMatrix,
                     Matrix4f projectionMatrix) {
    this.maxBatchSize = maxBatchSize;
    this.viewMatrix = viewMatrix;
    this.projectionMatrix = projectionMatrix;
    this.renderContexts = new ConcurrentHashMap<>();
    this.spriteComponents = new ConcurrentHashMap<>();
    this.renderContextFactory = new DefaultRenderContextFactory(this::createVerticesArray, this::createIndicesArray);
  }

  public void add(SpriteComponent spriteComponent) {
    Class<? extends Body> clazz = spriteComponent.body().getClass();
    this.spriteComponents.computeIfAbsent(clazz, key -> new ArrayList<>()).add(spriteComponent);
    this.initSpriteComponent(spriteComponent);
  }

  public void update() {
    this.spriteComponents.forEach(this::updateSpriteComponents);
  }

  public void render() {
    this.renderSpriteComponents();
  }

  public boolean hasRoom() {
    return this.spriteComponents.values()
      .stream()
      .mapToInt(List::size)
      .sum() < this.maxBatchSize;
  }

  private void updateSpriteComponents(Class<? extends Body> bodyClass, List<SpriteComponent> spriteComponentList) {
    RenderContext renderContext = this.renderContexts.get(bodyClass);

    for (int i = 0; i < spriteComponentList.size(); i++) {
      SpriteComponent spriteComponent = spriteComponentList.get(i);

      if (spriteComponent.shouldDestroy()) {
        this.deleteVertices(spriteComponent, i--);
        spriteComponentList.remove(spriteComponent);
      } else if (spriteComponent.dirty()) {
        this.updateVertices(spriteComponent, i);
        spriteComponent.clean();
        renderContext.refreshBufferData(true);
      }
    }
  }

  private void renderSpriteComponents() {
    this.renderContexts.values().forEach(renderContext -> {
      if (renderContext.refreshBufferData()) {
        this.refreshBuffer(renderContext);
      }

      Class<? extends Body> bodyClass = renderContext.clazz();
      int count = this.spriteComponents.get(bodyClass).size();
      RenderUtil.render(bodyClass, renderContext, this.viewMatrix, this.projectionMatrix, count);
    });
  }

  private void initSpriteComponent(SpriteComponent spriteComponent) {
    Class<? extends Body> bodyClass = spriteComponent.body().getClass();
    int index = this.spriteComponents.get(bodyClass).size() - 1;
    this.createRenderContext(spriteComponent, index);
  }

  private void createRenderContext(SpriteComponent spriteComponent, int index) {
    Class<? extends Body> bodyClass = spriteComponent.body().getClass();
    this.renderContexts.computeIfAbsent(bodyClass, key -> this.renderContextFactory.createRenderContext(spriteComponent, index));
  }

  private void updateVertices(SpriteComponent spriteComponent, int index) {
    Class<? extends Body> key = spriteComponent.body().getClass();
    RenderContext renderContext = this.renderContexts.get(key);

    float[] vertices = renderContext.vertices();
    spriteComponent.verticesFactory().createVertices(vertices, index);
    renderContext.vertices(vertices);
  }

  private void deleteVertices(SpriteComponent spriteComponent, int index) {
    Class<? extends Body> key = spriteComponent.body().getClass();
    RenderContext renderContext = this.renderContexts.get(key);
    float[] vertices = renderContext.vertices();
    int cornerCount = spriteComponent.cornerCount();

    int fromIndex = index * cornerCount * VERTEX_SIZE;
    int toIndex = index * cornerCount * VERTEX_SIZE + (VERTEX_SIZE * cornerCount);
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

  private int[] createIndicesArray(SpriteComponent spriteComponent) {
    Class<? extends Body> bodyClass = spriteComponent.body().getClass();
    int count = this.spriteComponents.get(bodyClass).size();
    return spriteComponent.indicesFactory().createIndices(this.maxBatchSize, count);
  }

  private float[] createVerticesArray(SpriteComponent spriteComponent) {
    return new float[this.maxBatchSize * spriteComponent.cornerCount() * VERTEX_SIZE];
  }
}