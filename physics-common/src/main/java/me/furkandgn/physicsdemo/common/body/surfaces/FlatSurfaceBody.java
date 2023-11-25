package me.furkandgn.physicsdemo.common.body.surfaces;

import me.furkandgn.physicsdemo.common.body.BodyType;
import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import org.joml.Vector4f;

/**
 * @author Furkan Doğan
 */
public class FlatSurfaceBody extends RectBody {

  public FlatSurfaceBody(double x, double y, double width, double height, Vector4f color) {
    super(width, height, 0, x, y, color);
  }

  @Override
  public BodyType bodyType() {
    return BodyType.FLAT_SURFACE;
  }

  @Override
  public boolean isSurface() {
    return true;
  }
}
