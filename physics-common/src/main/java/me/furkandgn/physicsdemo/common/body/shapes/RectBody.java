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
public class RectBody extends ColoredBody {

  public RectBody(double width, double height, float mass, double x, double y, Vector4f color) {
    super(ShapeUtils.createRectanglePoints(width, height), Transform.of(new Vector2d(x, y), new Vector2d(1, 1)), mass, color);
  }

  @Override
  public BodyType bodyType() {
    return BodyType.RECTANGLE;
  }
}
