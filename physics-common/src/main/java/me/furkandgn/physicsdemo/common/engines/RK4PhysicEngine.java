package me.furkandgn.physicsdemo.common.engines;

import me.furkandgn.physicsdemo.common.PhysicEngine;
import me.furkandgn.physicsdemo.common.body.Body;
import org.joml.Vector2d;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public class RK4PhysicEngine implements PhysicEngine {

  private static final double GRAVITY_CONSTANT = -3.81; // m/s^2

  @Override
  public void evaluate(List<Body> bodies, double dt) {
    double deltaTime = dt * 50;

    for (Body body : bodies) {
      Vector2d currentVelocity = body.velocity();
      Vector2d currentForce = body.force();
      Vector2d currentPosition = body.transform().position();

      // k1 calculations
      Vector2d k1Velocity = new Vector2d(currentVelocity);
      Vector2d k1Force = new Vector2d(currentForce);
      k1Force.mul(1.0 / body.mass());
      Vector2d k1Position = new Vector2d(k1Velocity).mul(deltaTime / 2.0);

      // k2 calculations
      Vector2d k2Velocity = new Vector2d(currentVelocity).add(k1Force.mul(deltaTime / 2.0));
      Vector2d k2Force = this.calculateForceAtPosition(currentPosition, k1Position);
      k2Force.mul(1.0 / body.mass());
      Vector2d k2Position = new Vector2d(k2Velocity).mul(deltaTime / 2.0);

      // k3 calculations
      Vector2d k3Velocity = new Vector2d(currentVelocity).add(k2Force.mul(deltaTime / 2.0));
      Vector2d k3Force = this.calculateForceAtPosition(currentPosition, k2Position);
      k3Force.mul(1.0 / body.mass());
      Vector2d k3Position = new Vector2d(k3Velocity).mul(deltaTime);

      // k4 calculations
      Vector2d k4Velocity = new Vector2d(currentVelocity).add(k3Force.mul(deltaTime));
      Vector2d k4Force = this.calculateForceAtPosition(currentPosition, k3Position);
      k4Force.mul(1.0 / body.mass());

      // Update velocity
      Vector2d deltaVelocity = new Vector2d(k1Force).add(k2Force.mul(2.0)).add(k3Force.mul(2.0)).add(k4Force);
      deltaVelocity.mul(deltaTime / 6.0);
      Vector2d newVelocity = new Vector2d(currentVelocity).add(deltaVelocity);
      body.velocity(newVelocity);

      // Update position
      Vector2d deltaPosition = new Vector2d(k1Velocity).add(k2Velocity.mul(2.0)).add(k3Velocity.mul(2.0)).add(k4Velocity);
      deltaPosition.mul(deltaTime / 6.0);
      Vector2d newPosition = new Vector2d(currentPosition).add(deltaPosition);
      body.transform().position().set(newPosition);
    }
  }

  private Vector2d calculateForceAtPosition(Vector2d currentPosition, Vector2d deltaPosition) {
    return new Vector2d(0, GRAVITY_CONSTANT);
  }
}
