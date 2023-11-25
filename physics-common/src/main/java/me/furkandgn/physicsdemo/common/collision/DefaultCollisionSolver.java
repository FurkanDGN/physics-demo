package me.furkandgn.physicsdemo.common.collision;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.BodyType;
import me.furkandgn.physicsdemo.common.collision.CollisionEvent;
import me.furkandgn.physicsdemo.common.collision.CollisionSolver;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import me.furkandgn.physicsdemo.common.body.surfaces.FlatSurfaceBody;
import me.furkandgn.physicsdemo.common.constants.PhysicConstants;
import org.joml.Vector2d;

import java.util.Set;

/**
 * @author Furkan Doğan
 */
public class DefaultCollisionSolver implements CollisionSolver {

  @Override
  public void solveCollisions(Set<CollisionEvent> collisions) {
    for (CollisionEvent collision : collisions) {
      Body body1 = collision.body1();
      Body body2 = collision.body2();
      this.solveCollision(body1, body2);
    }
  }

  private void solveCollision(Body body1, Body body2) {
    if (body1.bodyType() == BodyType.CIRCLE && body2.bodyType() == BodyType.FLAT_SURFACE) {
      this.solveFlatSurfaceCollision((CircleBody) body1, (FlatSurfaceBody) body2);
    } else if (body2.bodyType() == BodyType.CIRCLE && body1.bodyType() == BodyType.FLAT_SURFACE) {
      this.solveFlatSurfaceCollision((CircleBody) body2, (FlatSurfaceBody) body1);
    }
  }

  private void solveFlatSurfaceCollision(CircleBody circleBody, FlatSurfaceBody flatSurfaceBody) {
    double x = circleBody.x();
    double y = circleBody.y();
    double radius = circleBody.radius();
    double x1 = flatSurfaceBody.x();
    double y1 = flatSurfaceBody.y();
    double surfaceWidth = flatSurfaceBody.width();
    double surfaceHeight = flatSurfaceBody.height();

    if (x + radius >= x1 - surfaceWidth / 2d && x - radius < x1 - surfaceWidth / 2d) {
      Vector2d velocity = circleBody.velocity();
      velocity.x = -1 * Math.abs(velocity.x);
    }

    if (x - radius <= x1 + surfaceWidth / 2d && x + radius > x1 + surfaceWidth / 2d) {
      Vector2d velocity = circleBody.velocity();
      velocity.x = Math.abs(velocity.x);
    }

    if (y + radius >= y1 - surfaceHeight / 2d && y - radius < y1 - surfaceHeight / 2d) {
      Vector2d velocity = circleBody.velocity();
      velocity.y = -1 * Math.abs(velocity.y);
    }

    if (y - radius <= y1 + surfaceHeight / 2d && y + radius > y1 + surfaceHeight / 2d) {
      Vector2d velocity = circleBody.velocity();
      velocity.y = Math.abs(velocity.y);
      circleBody.force().y = -PhysicConstants.GRAVITY_FORCE;
    }
  }
}
