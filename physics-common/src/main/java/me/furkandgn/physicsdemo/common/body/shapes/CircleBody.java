package me.furkandgn.physicsdemo.common.body.shapes;

import me.furkandgn.physicsdemo.common.body.BodyType;
import me.furkandgn.physicsdemo.common.body.ColoredBody;
import me.furkandgn.physicsdemo.common.body.Transform;
import me.furkandgn.physicsdemo.common.util.ShapeUtils;
import org.joml.Vector2d;
import org.joml.Vector4f;

/**
 * @author Furkan Doğan
 */
public class CircleBody extends ColoredBody {

  private static final double DISTANCE = 0.1;

  private final double radius;

  public CircleBody(double radius,
                    float mass,
                    int x,
                    int y,
                    Vector4f color) {
    super(ShapeUtils.createCirclePoints(radius, (int) Math.round((2 * Math.PI * radius) / DISTANCE)), Transform.of(new Vector2d(x, y), new Vector2d(1, 1)), mass, color);
    this.radius = radius;
  }

  @Override
  public BodyType bodyType() {
    return BodyType.CIRCLE;
  }

  public double radius() {
    return this.radius;
  }

  @Override
  public String toString() {
    return "CircleBody{" +
      "radius=" + this.radius +
      ", color=" + this.color +
      ", width=" + this.width +
      ", height=" + this.height +
      ", points=" + this.points +
      ", transform=" + this.transform +
      ", mass=" + this.mass +
      ", uniqueId=" + this.uniqueId +
      ", force=" + this.force +
      ", velocity=" + this.velocity +
      '}';
  }
}
