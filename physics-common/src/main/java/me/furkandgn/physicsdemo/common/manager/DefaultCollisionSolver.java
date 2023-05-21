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
//    if (body1 instanceof CircleBody circleBody1 && body2 instanceof CircleBody circleBody2) {
//      double responseCoef = 0.75d;
//      Vector2d v = new Vector2d(body1.transform().position()).sub(new Vector2d(body2.transform().position()));
//      double dist2 = v.x * v.x + v.y * v.y;
//      int minDist = circleBody1.radius() + circleBody2.radius();
//      double dist = Math.sqrt(dist2);
//      Vector2d n = new Vector2d(v).div(dist);
//      double massRatio1 = (double) circleBody1.radius() / (circleBody1.radius() + circleBody2.radius());
//      double massRatio2 = (double) circleBody2.radius() / (circleBody1.radius() + circleBody2.radius());
//      double delta = 0.5 * responseCoef * (dist - minDist);
//      Vector2d mul = new Vector2d(n).mul(massRatio2 * delta);
//      body1.transform().position().sub(mul);
//      Vector2d mul1 = new Vector2d(n).mul(massRatio1 * delta);
//      body2.transform().position().add(mul1);
//    }

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
      movingCircle.force().y = -PhysicConstants.GRAVITY_FORCE;
    }
  }
}
