package me.furkandgn.physicsdemo.common.body.surfaces;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.attribute.Transform;

/**
 * @author Furkan Doğan
 */
public class FlatSurfaceBody extends Body {

  public FlatSurfaceBody(float mass, Transform transform) {
    super(mass, transform);
  }

  @Override
  public int width() {
    return 0;
  }

  @Override
  public int height() {
    return 0;
  }

  @Override
  public boolean canCollide(Body anotherBody) {
    return false;
  }
}
