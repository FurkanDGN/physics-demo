package me.furkandgn.physicsdemo.common.manager;

import me.furkandgn.physicsdemo.common.Body;
import me.furkandgn.physicsdemo.common.CollisionEvent;
import me.furkandgn.physicsdemo.common.CollisionSolver;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
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
      if (body1.isSurface() && body2.isSurface()) continue;

      this.solveCollision(body1, body2);
    }
  }

  private void solveCollision(Body body1, Body body2) {
    if (body1 instanceof CircleBody circleBody && body2 instanceof FlatSurfaceBody flatSurfaceBody) {
      this.solveStaticRectangle(circleBody, flatSurfaceBody);
    } else if (body2 instanceof CircleBody circleBody && body1 instanceof FlatSurfaceBody flatSurfaceBody) {
      this.solveStaticRectangle(circleBody, flatSurfaceBody);
    }
  }

  private void solveStaticRectangle(CircleBody movingCircle, RectBody staticRectangle) {
    double x = movingCircle.x();
    double y = movingCircle.y();
    double radius = movingCircle.radius();

    double x1 = staticRectangle.x();
    double y1 = staticRectangle.y();
    int rectWidth = staticRectangle.width();
    int rectHeight = staticRectangle.height();

    if (x + radius >= x1 - rectWidth && x - radius < x1 - rectWidth) {
      Vector2d velocity = movingCircle.velocity();
      velocity.x = -1 * Math.abs(velocity.x);
    }

    if (x - radius <= x1 + rectWidth && x + radius > x1 + rectWidth) {
      Vector2d velocity = movingCircle.velocity();
      velocity.x = Math.abs(velocity.x);
    }

    if (y + radius >= y1 - rectHeight / 2d && y - radius < y1 - rectHeight / 2d) {
      Vector2d velocity = movingCircle.velocity();
      velocity.y = -1 * Math.abs(velocity.y);
    }

    if (y - radius <= y1 + rectHeight / 2d && y + radius > y1 + rectHeight) {
      Vector2d velocity = movingCircle.velocity();
      velocity.y = Math.abs(velocity.y);
      movingCircle.force().y = -PhysicConstants.GRAVITY_FORCE * 0.9;
    }
  }
}
