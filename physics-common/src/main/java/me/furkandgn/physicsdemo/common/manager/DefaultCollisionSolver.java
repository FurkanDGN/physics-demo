package me.furkandgn.physicsdemo.common.manager;

import me.furkandgn.physicsdemo.common.Body;
import me.furkandgn.physicsdemo.common.CollisionEvent;
import me.furkandgn.physicsdemo.common.CollisionSolver;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import org.joml.Vector2d;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public class DefaultCollisionSolver implements CollisionSolver {

  @Override
  public void solveCollisions(List<CollisionEvent> collisions) {
    for (int i = 0; i < 10; i++) {
      for (CollisionEvent collision : collisions) {
        Body body1 = collision.body1();
        Body body2 = collision.body2();
        if (body1.isSurface() && body2.isSurface()) continue;

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
}
