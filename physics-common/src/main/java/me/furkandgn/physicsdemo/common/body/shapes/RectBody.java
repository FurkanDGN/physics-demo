package me.furkandgn.physicsdemo.common.body.shapes;

import me.furkandgn.physicsdemo.common.ColoredBody;
import me.furkandgn.physicsdemo.common.body.attribute.Transform;
import me.furkandgn.physicsdemo.common.util.ShapeUtils;
import org.joml.Vector2d;
import org.joml.Vector4f;

/**
 * @author Furkan Doğan
 */
public class RectBody extends ColoredBody {

  public RectBody(int width,
                  int height,
                  float mass,
                  int x,
                  int y,
                  Vector4f color) {
    super(ShapeUtils.createRectanglePoints(width, height), Transform.of(new Vector2d(x, y), new Vector2d(1, 1)), mass, color);
  }
}
