package me.furkandgn.physicsdemo.opengl.render;

import me.furkandgn.physicsdemo.opengl.camera.Camera;
import me.furkandgn.physicsdemo.opengl.component.sprite.SpriteComponent;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Furkan Doğan
 */
public class RenderBatchManager {

  private final int maxBatchSize;
  private final List<RenderBatch> renderBatches;
  private final Matrix4f viewMatrix;
  private final Matrix4f projectionMatrix;

  public RenderBatchManager(int maxBatchSize,
                            Camera camera) {
    this.maxBatchSize = maxBatchSize;
    this.viewMatrix = camera.viewMatrix();
    this.projectionMatrix = camera.projectionMatrix();
    this.renderBatches = new ArrayList<>();
  }

  public void add(SpriteComponent spriteComponent) {
    RenderBatch renderBatch = this.availableRenderBatch();
    renderBatch.add(spriteComponent);
  }

  public void render() {
    for (RenderBatch batch : this.renderBatches) {
      batch.render();
    }
  }

  private RenderBatch availableRenderBatch() {
    return this.renderBatches.stream()
      .filter(RenderBatch::hasRoom)
      .findFirst()
      .orElseGet(this::createRenderBatch);
  }

  private RenderBatch createRenderBatch() {
    RenderBatch renderBatch = new RenderBatch(this.maxBatchSize, this.viewMatrix, this.projectionMatrix);
    this.renderBatches.add(renderBatch);
    return renderBatch;
  }

}
