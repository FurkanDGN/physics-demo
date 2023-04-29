package me.furkandgn.physicsdemo.opengl.window.world;

import me.furkandgn.physicsdemo.common.PhysicEngine;
import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.CircleBody;
import me.furkandgn.physicsdemo.common.body.RectBody;
import me.furkandgn.physicsdemo.common.gui.world.AbstractWorld;
import me.furkandgn.physicsdemo.opengl.Constants;
import me.furkandgn.physicsdemo.opengl.window.camera.Camera;
import me.furkandgn.physicsdemo.opengl.window.component.factory.*;
import me.furkandgn.physicsdemo.opengl.window.component.sprite.SpriteComponent;
import me.furkandgn.physicsdemo.opengl.window.render.RenderBatchManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Furkan Doğan
 */
public class SimulationWorld extends AbstractWorld {

  private static final Map<Class<? extends Body>, IndicesFactory> INDICES_FACTORIES = Map.of(
    RectBody.class, new RectangleIndicesFactory(),
    CircleBody.class, new CircleIndicesFactory()
  );

  private final RenderBatchManager renderBatchManager;
  private final List<SpriteComponent> spriteComponents = new CopyOnWriteArrayList<>();
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
      if (spriteComponent.shouldDestroy()) {
        this.bodies.remove(spriteComponent.body());
        this.spriteComponents.remove(spriteComponent);
      }
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
    IndicesFactory indicesFactory = INDICES_FACTORIES.get(body.getClass());
    if (body instanceof RectBody) {
      return new SpriteComponent(body, indicesFactory, new RectangleVerticesFactory((RectBody) body, this.height));
    } else if (body instanceof CircleBody) {
      return new SpriteComponent(body, indicesFactory, new CircleVerticesFactory((CircleBody) body, this.height));
    } else {
      throw new RuntimeException("Unsupported body type " + body.getClass().getSimpleName());
    }
  }
}
