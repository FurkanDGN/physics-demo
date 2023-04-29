package me.furkandgn.physicsdemo.common.engines;

import me.furkandgn.physicsdemo.common.PhysicEngine;
import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.CircleBody;
import me.furkandgn.physicsdemo.common.collision.CollisionDetector;
import me.furkandgn.physicsdemo.common.collision.swp.CollisionEvent;
import me.furkandgn.physicsdemo.common.collision.swp.EventDrivenSweepAndPrune;
import org.joml.Vector2d;

import java.util.List;

import static me.furkandgn.physicsdemo.common.gui.GuiConstants.HEIGHT;
import static me.furkandgn.physicsdemo.common.gui.GuiConstants.WIDTH;

/**
 * @author Furkan Doğan
 */
public class VerletPhysicEngine implements PhysicEngine {

  private static final float GRAVITY = -9.81f;
  private static final Vector2d CONSTRAINT_POS = new Vector2d(WIDTH / 2, HEIGHT);
  private static final float CONSTRAINT_RADIUS = 500;
  private static final float OVERLAP_SLOP = 1.0f;

  private final CollisionDetector<List<CollisionEvent>> collisionDetector = new EventDrivenSweepAndPrune();

  @Override
  public void evaluate(List<Body> bodies, double dt) {
    bodies.forEach(body -> this.appleConstants(body, dt));
    for (int i = 0; i < 10; i++) {
      List<CollisionEvent> collisions = this.collisionDetector.findCollisions(bodies);
      for (CollisionEvent collision : collisions) {
        Body body1 = collision.body1();
        Body body2 = collision.body2();

        this.solveCollision(body1, body2);
      }
    }
  }

  private void solveCollision(Body body1, Body body2) {
    if (body1 instanceof CircleBody circleBody1 && body2 instanceof CircleBody circleBody2) {
      double responseCoef = 0.75d;
      Vector2d v = new Vector2d(body1.transform().position()).sub(new Vector2d(body2.transform().position()));
      double dist2 = v.x * v.x + v.y * v.y;
      int minDist = circleBody1.radius() + circleBody2.radius();
      double dist = Math.sqrt(dist2);
      Vector2d n = new Vector2d(v).div(dist);
      double massRatio1 = (double) circleBody1.radius() / (circleBody1.radius() + circleBody2.radius());
      double massRatio2 = (double) circleBody2.radius() / (circleBody1.radius() + circleBody2.radius());
      double delta = 0.5 * responseCoef * (dist - minDist);
      Vector2d mul = new Vector2d(n).mul(massRatio2 * delta);
      body1.transform().position().sub(mul);
      Vector2d mul1 = new Vector2d(n).mul(massRatio1 * delta);
      body2.transform().position().add(mul1);
    }
  }

  private void appleConstants(Body body, double dt) {
    this.gravity(body, dt);
    this.constraint(body);
  }

  private void gravity(Body body, double dt) {
    float mass = body.mass();

    Vector2d force = body.force();
    Vector2d velocity = body.velocity();
    Vector2d position = body.transform().position();
    Vector2d lastPosition = body.transform().lastPosition();
    Vector2d displacement = new Vector2d(position).sub(lastPosition);

    lastPosition.set(position);

    float mg = mass * GRAVITY;
    force.add(0, mg);
    velocity.add(new Vector2d(force).div(mass));
    position.add(displacement).add(new Vector2d(velocity).mul(dt * dt));

    body.force(new Vector2d());
  }

  private void constraint(Body body) {
    Vector2d position = new Vector2d(body.transform().position());

    Vector2d v = new Vector2d(CONSTRAINT_POS).sub(position);
    double dist = v.length();

    if (body instanceof CircleBody circleBody) {
      int radius = circleBody.radius();
      if (dist > CONSTRAINT_RADIUS - radius) {
        Vector2d ratio = new Vector2d(v).div(dist);
        Vector2d newPos = new Vector2d(CONSTRAINT_POS).sub(ratio.mul(CONSTRAINT_RADIUS));
        body.transform().position().set(newPos);
      }
    }
  }
}
