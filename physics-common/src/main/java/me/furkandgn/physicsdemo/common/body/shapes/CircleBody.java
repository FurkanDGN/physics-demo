package me.furkandgn.physicsdemo.common.body.shapes;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.attribute.Transform;
import org.joml.Vector2d;
import org.joml.Vector4f;

/**
 * @author Furkan Doğan
 */
public class CircleBody extends Body {

  private final int radius;
  private final Vector4f color;

  public CircleBody(int radius,
                    float mass,
                    int x,
                    int y,
                    Vector4f color) {
    super(mass, Transform.of(new Vector2d(x, y), new Vector2d(1, 1)));
    this.radius = radius;
    this.color = color;
  }

  @Override
  public int width() {
    return this.radius * 2;
  }

  @Override
  public int height() {
    return this.radius * 2;
  }

  @Override
  public boolean canCollide(Body anotherBody) {
    if (anotherBody instanceof RectBody) {
      return true;
    } else if (anotherBody instanceof CircleBody circleBody) {
      Vector2d position1 = this.transform.position();
      Vector2d position2 = circleBody.transform.position();
      double abs = Math.abs(new Vector2d(position1).sub(position2).length());
      return abs < this.radius * 2;
    }
    return false;
  }

  public int radius() {
    return this.radius;
  }

  public Vector4f color() {
    return this.color;
  }
}
