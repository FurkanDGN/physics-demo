package me.furkandgn.physicsdemo.common.gui;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.physic.PhysicEngine;

import java.util.Collections;
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
  public List<Body> getBodies() {
    return Collections.unmodifiableList(this.bodies);
  }

  @Override
  public void addBody(Body body) {
    this.bodies.add(body);
  }

  @Override
  public void tick(double dt) {
    dt = Math.round(dt * 100000) / 100000.0;
    // TODO: 1.07.2023 Sometimes dt returns 0, fix this
    this.physicEngine.evaluate(this.bodies, dt);
  }
}
