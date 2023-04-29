package me.furkandgn.physicsdemo.opengl.window.render;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.opengl.window.component.sprite.SpriteComponent;
import me.furkandgn.physicsdemo.opengl.window.util.RenderUtil;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static me.furkandgn.physicsdemo.opengl.Constants.*;
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

  public RenderBatch(int maxBatchSize,
                     Matrix4f viewMatrix,
                     Matrix4f projectionMatrix) {
    this.maxBatchSize = maxBatchSize;
    this.viewMatrix = viewMatrix;
    this.projectionMatrix = projectionMatrix;
    this.renderContexts = new ConcurrentHashMap<>();
    this.spriteComponents = new ConcurrentHashMap<>();
  }

  public void add(SpriteComponent spriteComponent) {
    Class<? extends Body> clazz = spriteComponent.body().getClass();
    this.spriteComponents.computeIfAbsent(clazz, key -> new ArrayList<>()).add(spriteComponent);
    this.initSpriteComponent(spriteComponent);
  }

  public void render() {
    this.updateAndRenderSpriteComponents();
  }

  public boolean hasRoom() {
    return this.spriteComponents.values()
      .stream()
      .mapToInt(List::size)
      .sum() < this.maxBatchSize;
  }

  private void updateAndRenderSpriteComponents() {
    this.spriteComponents.entrySet()
      .parallelStream()
      .forEach(entry -> this.updateSpriteComponents(entry.getKey(), entry.getValue()));
    this.renderSpriteComponents();
  }

  private void updateSpriteComponents(Class<? extends Body> bodyClass, List<SpriteComponent> spriteComponentList) {
    RenderContext renderContext = this.renderContexts.get(bodyClass);

    for (int i = 0; i < spriteComponentList.size(); i++) {
      SpriteComponent spriteComponent = spriteComponentList.get(i);

      if (spriteComponent.shouldDestroy()) {
        this.deleteVertices(spriteComponent, i--);
        spriteComponentList.remove(spriteComponent);
      } else if (spriteComponent.dirty()) {
        this.upsertVertices(spriteComponent, i);
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
    RenderContext renderContext = this.createRenderContext(spriteComponent);
    this.updateVerticesAndIndices(spriteComponent, spriteComponent.body().getClass());

    this.setupVertexBuffer(renderContext);
    this.setupElementBuffer(renderContext);
    this.setupVertexArray(renderContext);
    this.unbindVertexArrayAndBuffers();
  }

  private RenderContext createRenderContext(SpriteComponent spriteComponent) {
    Class<? extends Body> bodyClass = spriteComponent.body().getClass();
    return this.renderContexts.computeIfAbsent(bodyClass, key -> this.createRenderContext(bodyClass));
  }

  private void updateVerticesAndIndices(SpriteComponent spriteComponent, Class<? extends Body> bodyClass) {
    this.upsertVertices(spriteComponent, this.spriteComponents.get(bodyClass).size() - 1);
    this.insertIndices(spriteComponent);
  }

  private RenderContext createRenderContext(Class<? extends Body> bodyClass) {
    RenderContext renderContext = new RenderContext(bodyClass);
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

  private void upsertVertices(SpriteComponent spriteComponent, int index) {
    Class<? extends Body> key = spriteComponent.body().getClass();
    RenderContext renderContext = this.renderContexts.get(key);
    int cornerCount = spriteComponent.cornerCount();

    float[] vertices = renderContext.vertices() != null ? renderContext.vertices() : new float[this.maxBatchSize * cornerCount * VERTEX_SIZE];
    spriteComponent.verticesFactory().createVertices(vertices, index);
    renderContext.vertices(vertices);
  }

  private void insertIndices(SpriteComponent spriteComponent) {
    Class<? extends Body> key = spriteComponent.body().getClass();
    RenderContext renderContext = this.renderContexts.get(key);
    int count = this.spriteComponents.get(key).size();

    int[] indices = spriteComponent.indicesFactory().createIndices(this.maxBatchSize, count);
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
}