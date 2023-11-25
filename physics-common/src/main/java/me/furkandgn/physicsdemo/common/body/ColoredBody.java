package me.furkandgn.physicsdemo.common.body;

import me.furkandgn.physicsdemo.common.util.Point;
import org.joml.Vector4f;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public abstract class ColoredBody extends Body {

  protected final Vector4f color;

  public ColoredBody(List<Point> points, Transform transform, float mass, Vector4f color) {
    super(points, transform, mass);
    this.color = color;
  }

  public Vector4f color() {
    return this.color;
  }
}
