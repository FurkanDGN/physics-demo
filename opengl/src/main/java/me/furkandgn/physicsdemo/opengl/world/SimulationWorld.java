package me.furkandgn.physicsdemo.opengl.world;

import me.furkandgn.physicsdemo.common.PhysicEngine;
import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.CircleBody;
import me.furkandgn.physicsdemo.common.body.RectBody;
import me.furkandgn.physicsdemo.common.gui.world.AbstractWorld;
import me.furkandgn.physicsdemo.opengl.Constants;
import me.furkandgn.physicsdemo.opengl.camera.Camera;
import me.furkandgn.physicsdemo.opengl.component.factory.CircleIndicesFactory;
import me.furkandgn.physicsdemo.opengl.component.factory.CircleVerticesFactory;
import me.furkandgn.physicsdemo.opengl.component.factory.RectangleIndicesFactory;
import me.furkandgn.physicsdemo.opengl.component.factory.RectangleVerticesFactory;
import me.furkandgn.physicsdemo.opengl.component.sprite.SpriteComponent;
import me.furkandgn.physicsdemo.opengl.render.RenderBatchManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Furkan Doğan
 */
public class SimulationWorld extends AbstractWorld {

  private final RenderBatchManager renderBatchManager;
  private final List<SpriteComponent> spriteComponents = new ArrayList<>();
  private final int height;

  public SimulationWorld(PhysicEngine physicEngine,
                         Camera camera,
                         int height) {
    super(physicEngine);
    this.renderBatchManager = new RenderBatchManager(Constants.MAX_BATCH_SIZE, camera);
    this.height = height;
  }

  @Override
  public void addBody(Body body) {
    super.addBody(body);

    SpriteComponent spriteComponent = this.buildSpriteComponent(body);
    spriteComponent.init();
    this.renderBatchManager.add(spriteComponent);
    this.spriteComponents.add(spriteComponent);
  }

  @Override
  public void tick(double dt) {
    for (SpriteComponent spriteComponent : this.spriteComponents) {
      spriteComponent.update(dt);
    }
    super.tick(dt);
  }

  @Override
  public void render() {
    this.renderBodies();
  }

  private void renderBodies() {
    this.renderBatchManager.render();
  }

  private SpriteComponent buildSpriteComponent(Body body) {
    if (body instanceof RectBody) {
      return new SpriteComponent(body, new RectangleIndicesFactory(), new RectangleVerticesFactory((RectBody) body, this.height));
    } else if (body instanceof CircleBody) {
      return new SpriteComponent(body, new CircleIndicesFactory(), new CircleVerticesFactory((CircleBody) body, this.height));
    } else {
      throw new RuntimeException("Unsupported body type " + body.getClass().getSimpleName());
    }
  }
}
