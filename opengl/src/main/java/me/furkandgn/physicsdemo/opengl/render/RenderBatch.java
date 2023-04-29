package me.furkandgn.physicsdemo.opengl.render;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.opengl.component.sprite.SpriteComponent;
import me.furkandgn.physicsdemo.opengl.util.RenderUtil;
import org.joml.Matrix4f;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static me.furkandgn.physicsdemo.opengl.Constants.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Furkan Doğan
 */
public class RenderBatch {

  private final int maxBatchSize;
  private final Map<Class<? extends Body>, List<SpriteComponent>> spriteComponents;
  private final Matrix4f viewMatrix;
  private final Matrix4f projectionMatrix;
  private final Map<Class<?>, RenderContext> renderContexts;

  public RenderBatch(int maxBatchSize,
                     Matrix4f viewMatrix,
                     Matrix4f projectionMatrix) {
    this.spriteComponents = new HashMap<>();
    this.maxBatchSize = maxBatchSize;
    this.viewMatrix = viewMatrix;
    this.projectionMatrix = projectionMatrix;
    this.renderContexts = new ConcurrentHashMap<>();
  }

  public void render() {
    for (Class<? extends Body> clazz : this.spriteComponents.keySet()) {
      List<SpriteComponent> spriteComponentList = this.spriteComponents.get(clazz);
      for (int i = 0; i < spriteComponentList.size(); i++) {
        SpriteComponent spriteComponent = spriteComponentList.get(i);
        RenderContext renderContext = this.renderContexts.get(clazz);

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

    for (RenderContext renderContext : this.renderContexts.values()) {
      if (renderContext.refreshBufferData()) {
        glBindBuffer(GL_ARRAY_BUFFER, renderContext.vboId());
        float[] vertices = renderContext.vertices();
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        renderContext.refreshBufferData(false);
      }

      Class<? extends Body> key = renderContext.clazz();
      int count = this.spriteComponents.get(key).size();

      RenderUtil.render(key, renderContext, this.viewMatrix, this.projectionMatrix, count);
    }
  }

  public void add(SpriteComponent spriteComponent) {
    Class<? extends Body> clazz = spriteComponent.body().getClass();
    this.spriteComponents.compute(clazz, (c, list) -> {
      if (list == null) {
        List<SpriteComponent> objects = new ArrayList<>();
        objects.add(spriteComponent);
        return objects;
      } else {
        list.add(spriteComponent);
        return list;
      }
    });
    this.init(spriteComponent);
  }

  public boolean hasRoom() {
    return this.spriteComponents.values()
      .stream()
      .map(List::size)
      .mapToInt(value -> value)
      .sum() < this.maxBatchSize;
  }

  private void init(SpriteComponent spriteComponent) {
    this.updateContext(spriteComponent);

    RenderContext renderContext = this.renderContexts.get(spriteComponent.body().getClass());

    float[] vertices = renderContext.vertices();

    int vaoID = renderContext.vaoId();
    glBindVertexArray(vaoID);

    // Allocate space for vertices
    int vboID = renderContext.vboId();
    glBindBuffer(GL_ARRAY_BUFFER, vboID);
    glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

    // Create and upload indices buffer
    int eboID = renderContext.eboId();
    int[] indices = renderContext.indices();

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

    glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);

    glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);

    glBindBuffer(GL_ARRAY_BUFFER, -1);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, -1);
  }

  private void updateContext(SpriteComponent spriteComponent) {
    Class<? extends Body> key = spriteComponent.body().getClass();
    RenderContext renderContext = this.renderContexts.get(key);
    if (renderContext == null) {
      renderContext = new RenderContext(key);
      this.renderContexts.put(key, renderContext);
      this.updateIds(spriteComponent);
      this.updateVertices(spriteComponent, this.spriteComponents.get(key).size() - 1);
      this.updateIndices(spriteComponent);
    } else {
      this.updateVertices(spriteComponent, this.spriteComponents.get(key).size() - 1);
      this.updateIndices(spriteComponent);
      renderContext.refreshBufferData(true);
    }
    spriteComponent.clean();
    this.renderContexts.put(key, renderContext);
  }

  private void updateIds(SpriteComponent spriteComponent) {
    Class<? extends Body> key = spriteComponent.body().getClass();
    RenderContext renderContext = this.renderContexts.get(key);

    int vaoID = glGenVertexArrays();
    int vboID = glGenBuffers();
    int eboID = glGenBuffers();

    renderContext.vboId(vboID);
    renderContext.vaoId(vaoID);
    renderContext.eboId(eboID);
  }

  private void updateVertices(SpriteComponent spriteComponent, int index) {
    Class<? extends Body> key = spriteComponent.body().getClass();
    RenderContext renderContext = this.renderContexts.get(key);
    int cornerCount = spriteComponent.cornerCount();

    float[] vertices = renderContext.vertices() != null ? renderContext.vertices() : new float[this.maxBatchSize * cornerCount * VERTEX_SIZE];

    spriteComponent.verticesFactory().createVertices(vertices, index);

    renderContext.vertices(vertices);
  }

  private void updateIndices(SpriteComponent spriteComponent) {
    Class<? extends Body> key = spriteComponent.body().getClass();
    RenderContext renderContext = this.renderContexts.get(key);

    int count = this.spriteComponents.get(key).size();

    int[] indices = spriteComponent.indicesFactory().createIndices(this.maxBatchSize, count);

    renderContext.indices(indices);
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
}
