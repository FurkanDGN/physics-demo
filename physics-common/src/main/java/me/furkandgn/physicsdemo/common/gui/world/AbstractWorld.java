package me.furkandgn.physicsdemo.common.gui.world;

import me.furkandgn.physicsdemo.common.PhysicEngine;
import me.furkandgn.physicsdemo.common.body.Body;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Furkan Doğan
 */
public abstract class AbstractWorld implements World {

  protected final List<Body> bodies = new CopyOnWriteArrayList<>();
  protected final PhysicEngine physicEngine;

  public AbstractWorld(PhysicEngine physicEngine) {
    this.physicEngine = physicEngine;
  }

  @Override
  public void addBody(Body body) {
    this.bodies.add(body);
  }

  @Override
  public void tick(double dt) {
    this.physicEngine.evaluate(this.bodies, dt);
  }
}
