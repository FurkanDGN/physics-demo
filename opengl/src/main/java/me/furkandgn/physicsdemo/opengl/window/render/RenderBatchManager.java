package me.furkandgn.physicsdemo.opengl.window.render;

import me.furkandgn.physicsdemo.opengl.window.camera.Camera;
import me.furkandgn.physicsdemo.opengl.window.component.Component;
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

  public void add(Component component) {
    int dotCount = component.dotCount();
    RenderBatch renderBatch = this.availableRenderBatch(dotCount);
    renderBatch.add(component);
  }

  public void update() {
    for (RenderBatch batch : this.renderBatches) {
      batch.update();
    }
  }

  public void render() {
    for (RenderBatch batch : this.renderBatches) {
      batch.render();
    }
  }

  private RenderBatch availableRenderBatch(int dotCount) {
    for (RenderBatch renderBatch : this.renderBatches) {
      if (!renderBatch.hasRoom(dotCount)) continue;
      return renderBatch;
    }

    return this.createRenderBatch();
  }

  private RenderBatch createRenderBatch() {
    RenderBatch renderBatch = new RenderBatch(this.maxBatchSize, this.viewMatrix, this.projectionMatrix);
    this.renderBatches.add(renderBatch);
    return renderBatch;
  }

}
