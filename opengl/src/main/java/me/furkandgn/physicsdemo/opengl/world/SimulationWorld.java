package me.furkandgn.physicsdemo.opengl.world;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.physic.PhysicEngine;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import me.furkandgn.physicsdemo.common.body.surfaces.FlatSurfaceBody;
import me.furkandgn.physicsdemo.common.gui.AbstractWorld;
import me.furkandgn.physicsdemo.opengl.Constants;
import me.furkandgn.physicsdemo.opengl.component.factory.*;
import me.furkandgn.physicsdemo.opengl.camera.Camera;
import me.furkandgn.physicsdemo.opengl.component.Component;
import me.furkandgn.physicsdemo.opengl.component.sprite.SpriteComponent;
import me.furkandgn.physicsdemo.opengl.render.RenderBatchManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Furkan Doğan
 */
public class SimulationWorld extends AbstractWorld {

  private static final Map<Class<? extends Body>, IndicesFactory> INDICES_FACTORIES = Map.of(
    FlatSurfaceBody.class, new PolygonIndicesFactory(),
    RectBody.class, new PolygonIndicesFactory(),
    CircleBody.class, new CircleIndicesFactory()
  );

  private final RenderBatchManager renderBatchManager;
  private final List<Component> components = new CopyOnWriteArrayList<>();
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
    Component component = this.buildComponent(body);
    component.init();
    this.renderBatchManager.add(component);
    this.components.add(component);
  }

  @Override
  public void tick(double dt) {
    for (Component component : this.components) {
      component.update(dt);
      if (component.shouldDestroy()) {
        this.bodies.remove(component.body());
        this.components.remove(component);
      }
    }
    super.tick(dt);
    this.renderBatchManager.update();
  }

  @Override
  public void render() {
    this.renderBodies();
  }

  private void renderBodies() {
    this.renderBatchManager.render();
  }

  private Component buildComponent(Body body) {
    IndicesFactory indicesFactory = INDICES_FACTORIES.get(body.getClass());
    VerticesFactory verticesFactory = this.buildVerticesFactory(body);
    return new SpriteComponent(body, indicesFactory, verticesFactory, body.points().size());
  }

  private VerticesFactory buildVerticesFactory(Body body) {
    return new PolygonVerticesFactory(body, this.height);
  }
}
