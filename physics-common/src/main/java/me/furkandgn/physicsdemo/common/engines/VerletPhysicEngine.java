package me.furkandgn.physicsdemo.common.engines;

import me.furkandgn.physicsdemo.common.*;
import org.joml.Vector2d;

import java.util.List;

import static me.furkandgn.physicsdemo.common.constants.PhysicConstants.GRAVITY_FORCE;

/**
 * @author Furkan Doğan
 */
public class VerletPhysicEngine implements PhysicEngine {

  private final CollisionDetector collisionDetector;
  private final CollisionSolver collisionSolver;

  public VerletPhysicEngine(CollisionDetector collisionDetector,
                            CollisionSolver collisionSolver) {
    this.collisionDetector = collisionDetector;
    this.collisionSolver = collisionSolver;
  }

  @Override
  public void evaluate(List<Body> bodies, double dt) {
    bodies.forEach(body -> this.update(body, dt));
    List<CollisionEvent> collisions = this.collisionDetector.findCollisions(bodies);
    this.collisionSolver.solveCollisions(collisions);
  }

  private void update(Body body, double dt) {
    if (body.isSurface()) return;
    Vector2d position = body.transform().position();
    Vector2d prevPosition = body.transform().lastPosition();
    Vector2d acceleration = this.applyForces(dt);
    Vector2d force = body.force();

    double timeStep = dt * 10;

    this.apply(position, prevPosition, acceleration, force, timeStep);
    force.set(0);
  }

  private Vector2d applyForces(double dt) {
    return new Vector2d(0, GRAVITY_FORCE);
  }

  private void apply(Vector2d position,
                     Vector2d prevPosition,
                     Vector2d acceleration,
                     Vector2d extraForce,
                     double timeStep) {
    Vector2d temp = new Vector2d(position);
    Vector2d netAcceleration = new Vector2d(acceleration).add(extraForce);
    position.add(position).sub(prevPosition).add(netAcceleration.mul(timeStep * timeStep, new Vector2d()));
    prevPosition.set(temp);
  }
}
