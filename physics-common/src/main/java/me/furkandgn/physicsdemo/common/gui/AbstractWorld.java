package me.furkandgn.physicsdemo.common.gui;

import me.furkandgn.physicsdemo.common.PhysicEngine;
import me.furkandgn.physicsdemo.common.Body;
import me.furkandgn.physicsdemo.common.gui.World;

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
