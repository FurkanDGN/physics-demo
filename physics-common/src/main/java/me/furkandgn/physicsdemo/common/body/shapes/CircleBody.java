package me.furkandgn.physicsdemo.common.body.shapes;

import me.furkandgn.physicsdemo.common.ColoredBody;
import me.furkandgn.physicsdemo.common.body.attribute.Transform;
import me.furkandgn.physicsdemo.common.util.ShapeUtils;
import org.joml.Vector2d;
import org.joml.Vector4f;

/**
 * @author Furkan Doğan
 */
public class CircleBody extends ColoredBody {

  private final int radius;

  public CircleBody(int radius,
                    float mass,
                    int x,
                    int y,
                    int cornerCount,
                    Vector4f color) {
    super(ShapeUtils.createCirclePoints(radius, cornerCount), Transform.of(new Vector2d(x, y), new Vector2d(1, 1)), mass, color);
    this.radius = radius;
  }

  public int radius() {
    return this.radius;
  }
}
