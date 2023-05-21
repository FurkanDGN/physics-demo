package me.furkandgn.physicsdemo.common.body.surfaces;

import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import org.joml.Vector4f;

/**
 * @author Furkan Doğan
 */
public class FlatSurfaceBody extends RectBody {

  public FlatSurfaceBody(int x,
                         int y,
                         int width,
                         int height,
                         Vector4f color) {
    super(width, height, 0, x, y, color);
  }

  @Override
  public boolean isSurface() {
    return true;
  }
}
