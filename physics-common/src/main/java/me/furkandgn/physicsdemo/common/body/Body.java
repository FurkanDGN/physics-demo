package me.furkandgn.physicsdemo.common.body;

import me.furkandgn.physicsdemo.common.util.Point;
import me.furkandgn.physicsdemo.common.util.ShapeUtils;
import org.joml.Vector2d;

import java.util.List;
import java.util.UUID;

/**
 * @author Furkan Doğan
 */
public abstract class Body {

  protected final double width;
  protected final double height;
  protected final List<Point> points;
  protected final Transform transform;
  protected final float mass;
  protected final UUID uniqueId;

  protected Vector2d force;
  protected Vector2d velocity;

  public Body(List<Point> points, Transform transform, float mass) {
    if (points.size() < 3) {
      throw new IllegalArgumentException("Points size cannot be less than 3");
    }
    this.width = points.stream().mapToDouble(Point::x).max()
      .orElse(0) - points.stream().mapToDouble(Point::x).min()
      .orElse(0);
    this.height = points.stream().mapToDouble(Point::y).max()
      .orElse(0) - points.stream().mapToDouble(Point::y).min()
      .orElse(0);
    this.points = points;
    this.transform = transform;
    this.mass = mass;
    this.uniqueId = UUID.randomUUID();
    this.force = new Vector2d();
    this.velocity = new Vector2d();
  }

  public abstract BodyType bodyType();

  public double width() {
    return this.width;
  }

  public double height() {
    return this.height;
  }

  public List<Point> points() {
    return this.points;
  }

  public List<Point> transformedPoints() {
    return ShapeUtils.transformPoints(this.points, this.transform);
  }

  public double x() {
    return this.transform.position().x;
  }

  public double y() {
    return this.transform.position().y;
  }

  public float mass() {
    return this.mass;
  }

  public Transform transform() {
    return this.transform;
  }

  public Vector2d velocity() {
    return this.velocity;
  }

  public Vector2d force() {
    return this.force;
  }

  public UUID uniqueId() {
    return this.uniqueId;
  }

  public boolean isSurface() {
    return false;
  }
}
