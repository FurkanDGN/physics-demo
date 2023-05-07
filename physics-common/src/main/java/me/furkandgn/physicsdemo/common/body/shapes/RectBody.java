package me.furkandgn.physicsdemo.common.body.shapes;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.attribute.Transform;
import org.joml.Vector2d;
import org.joml.Vector4f;

import java.awt.*;

/**
 * @author Furkan Doğan
 */
public class RectBody extends Body {

  private final int width;
  private final int height;
  private final Vector4f color;

  public RectBody(int width,
                  int height,
                  float mass,
                  int x,
                  int y,
                  Vector4f color) {
    super(mass, Transform.of(new Vector2d(x, y), new Vector2d(1, 1)));
    this.width = width;
    this.height = height;
    this.color = color;
  }

  public int width() {
    return this.width;
  }

  public int height() {
    return this.height;
  }

  @Override
  public boolean canCollide(Body anotherBody) {
    if (anotherBody instanceof CircleBody circleBody) {
      int minXc = (int) circleBody.x() - circleBody.radius();
      int minYc = (int) circleBody.y() - circleBody.radius();

      int minX = (int) this.x() - this.width() / 2;
      int minY = (int) this.y() - this.height() / 2;

      return new Rectangle(minX, minY, this.width(), this.height()).intersects(new Rectangle(minXc, minYc, circleBody.width(), circleBody.height()));
    }

    return false;
  }

  public Vector4f color() {
    return this.color;
  }
}
