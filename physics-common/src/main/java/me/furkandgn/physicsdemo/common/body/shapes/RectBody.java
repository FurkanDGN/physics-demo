package me.furkandgn.physicsdemo.common.body.shapes;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.attribute.Transform;
import org.joml.Vector4f;

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
                  Transform transform,
                  Vector4f color) {
    super(mass, transform);
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
    return true;
  }

  public Vector4f color() {
    return this.color;
  }
}
