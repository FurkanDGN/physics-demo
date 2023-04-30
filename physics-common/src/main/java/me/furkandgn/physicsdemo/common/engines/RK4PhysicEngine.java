package me.furkandgn.physicsdemo.common.engines;

import me.furkandgn.physicsdemo.common.PhysicEngine;
import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.collision.AABB;
import me.furkandgn.physicsdemo.common.collision.Collision;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Furkan Doğan
 */
public class RK4PhysicEngine implements PhysicEngine {

  private static final double GRAVITY_CONSTANT = -9.41; // m/s^2
  private static final double FRICTION_COEFFICIENT = 0.1;

  @Override
  public void evaluate(List<Body> bodies, double dt) {
    double deltaTime = dt * 30;

    List<Collision> collisions = new ArrayList<>();
    for (int i = 0; i < bodies.size(); i++) {
      Body bodyA = bodies.get(i);
      for (int j = i + 1; j < bodies.size(); j++) {
        Body bodyB = bodies.get(j);
        if (bodyB.isSurface()) continue;
        Collision collision = this.detectCollisionAABB(bodyA, bodyB);
        if (collision != null) {
          collisions.add(collision);
        }
      }
    }

    for (Collision collision : collisions) {
      this.resolveCollision(collision);
    }


    for (Body body : bodies) {
      if (body.isSurface()) {
        continue;
      }

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
      Vector2d k2Force = this.calculateForceAtPosition(body, currentPosition, k1Position);
      k2Force.mul(1.0 / body.mass());
      Vector2d k2Position = new Vector2d(k2Velocity).mul(deltaTime / 2.0);

      // k3 calculations
      Vector2d k3Velocity = new Vector2d(currentVelocity).add(k2Force.mul(deltaTime / 2.0));
      Vector2d k3Force = this.calculateForceAtPosition(body, currentPosition, k2Position);
      k3Force.mul(1.0 / body.mass());
      Vector2d k3Position = new Vector2d(k3Velocity).mul(deltaTime);

      // k4 calculations
      Vector2d k4Velocity = new Vector2d(currentVelocity).add(k3Force.mul(deltaTime));
      Vector2d k4Force = this.calculateForceAtPosition(body, currentPosition, k3Position);
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

  private Collision detectCollisionAABB(Body bodyA, Body bodyB) {
    // Check if the bodies are surfaces, if so, skip collision detection
    if (bodyA.isSurface() && bodyB.isSurface()) {
      return null;
    }

    // Compute the AABB bounds for both bodies
    AABB aabbA = this.calculateAABB(bodyA);
    AABB aabbB = this.calculateAABB(bodyB);

    // Check for AABB overlap
    if (aabbA.maxX() < aabbB.minX() || aabbA.minX() > aabbB.maxX() || aabbA.maxY() < aabbB.minY() || aabbA.minY() > aabbB.maxY()) {
      return null;
    }

    // Calculate the collision normal and penetration depth
    double dx = bodyA.x() - bodyB.x();
    double dy = bodyA.y() - bodyB.y();

    double overlapX = (bodyA.width() / 2.0 + bodyB.width() / 2.0) - Math.abs(dx);
    double overlapY = (bodyA.height() / 2.0 + bodyB.height() / 2.0) - Math.abs(dy);

    Vector2d normal;
    double penetrationDepth;

    if (overlapX < overlapY) {
      normal = new Vector2d(dx < 0 ? -1 : 1, 0);
      penetrationDepth = overlapX;
    } else {
      normal = new Vector2d(0, dy < 0 ? -1 : 1);
      penetrationDepth = overlapY;
    }

    return new Collision(bodyA, bodyB, normal, penetrationDepth);
  }

  private void resolveCollision(Collision collision) {
    Body bodyA = collision.bodyA();
    Body bodyB = collision.bodyB();

    double invMassA = bodyA.mass() != 0 ? 1.0 / bodyA.mass() : 0;
    double invMassB = bodyB.mass() != 0 ? 1.0 / bodyB.mass() : 0;
    double totalInvMass = invMassA + invMassB;

    // Calculate the impulse based on the collision normal and penetration depth
    double impulse = collision.penetrationDepth() / totalInvMass;
    Vector2d impulseVector = new Vector2d(collision.normal()).mul(impulse);

    // Update the velocities of the colliding bodies
    bodyA.velocity().add(new Vector2d(impulseVector).mul(invMassA));
    bodyB.velocity().sub(new Vector2d(impulseVector).mul(invMassB));

    // Update the positions of the colliding bodies to resolve interpenetration
    double percent = 0.01; // Percentage to correct the position
    double slop = 0.1; // Allow a small overlap between bodies to prevent jitter
    Vector2d correction = new Vector2d(collision.normal()).mul(Math.max(collision.penetrationDepth() - slop, 0) * percent / totalInvMass);
    bodyA.transform().position().sub(new Vector2d(correction).mul(invMassA));
    bodyB.transform().position().add(new Vector2d(correction).mul(invMassB));
  }

  public AABB calculateAABB(Body body) {
    double halfWidth = body.width() / 2.0;
    double halfHeight = body.height() / 2.0;

    double minX = body.x() - halfWidth;
    double maxX = body.x() + halfWidth;
    double minY = body.y() - halfHeight;
    double maxY = body.y() + halfHeight;

    return new AABB(minX, minY, maxX, maxY);
  }

  private Vector2d calculateForceAtPosition(Body body, Vector2d currentPosition, Vector2d deltaPosition) {
    Vector2d gravityForce = new Vector2d(0, GRAVITY_CONSTANT);

    // Calculate friction force (assuming linear friction)
    Vector2d frictionForce = new Vector2d(body.velocity());
    frictionForce.mul(-FRICTION_COEFFICIENT);

    return new Vector2d(gravityForce).add(frictionForce);
  }
}
