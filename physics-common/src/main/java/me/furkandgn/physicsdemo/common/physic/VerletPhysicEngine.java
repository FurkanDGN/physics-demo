package me.furkandgn.physicsdemo.common.physic;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.collision.CollisionDetector;
import me.furkandgn.physicsdemo.common.collision.CollisionEvent;
import me.furkandgn.physicsdemo.common.collision.CollisionSolver;
import org.joml.Vector2d;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static me.furkandgn.physicsdemo.common.constants.PhysicConstants.GRAVITY_FORCE;

/**
 * @author Furkan Doğan
 */
public class VerletPhysicEngine implements PhysicEngine {

  private static final int SUB_STEPS = 2;

  private final CollisionDetector collisionDetector;
  private final CollisionSolver collisionSolver;

  public VerletPhysicEngine(CollisionDetector collisionDetector, CollisionSolver collisionSolver) {
    this.collisionDetector = collisionDetector != null ? collisionDetector : bodies -> Collections.emptySet();
    this.collisionSolver = collisionSolver != null ? collisionSolver : collisions -> {
    };
  }

  @Override
  public void evaluate(List<Body> bodies, double dt) {
    for (int i = 0; i < SUB_STEPS; i++) {
//      long now = System.nanoTime();
      Set<CollisionEvent> collisions = this.collisionDetector.findCollisions(bodies);
      this.collisionSolver.solveCollisions(collisions);
      bodies.forEach(body -> this.update(body, dt / SUB_STEPS));
//      long last = System.nanoTime();
//      System.out.printf("%.6f%n", (last - now) / TimeUtil.SECOND_TO_NANOS);
    }
  }

  private void update(Body body, double dt) {
    if (body.isSurface()) return;
    Vector2d position = body.transform().position();
    Vector2d acceleration = this.applyForces(dt);
    Vector2d force = body.force();
    Vector2d velocity = body.velocity();

    Vector2d virtualPrevPosition = new Vector2d(position).sub(velocity.mul(dt, new Vector2d()));

    this.apply(position, virtualPrevPosition, acceleration, force, dt);
    force.set(0);
    velocity.set(position).sub(virtualPrevPosition).mul(1.0 / dt);
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
