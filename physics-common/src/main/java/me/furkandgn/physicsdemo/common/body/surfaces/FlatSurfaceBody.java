package me.furkandgn.physicsdemo.common.body.surfaces;

import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import org.joml.Vector4f;

/**
 * @author Furkan Doğan
 */
public class FlatSurfaceBody extends RectBody {

  public FlatSurfaceBody(int width,
                         int height,
                         float mass,
                         int x,
                         int y,
                         Vector4f color) {
    super(width, height, mass, x, y, color);
  }

  @Override
  public boolean isSurface() {
    return true;
  }
}
