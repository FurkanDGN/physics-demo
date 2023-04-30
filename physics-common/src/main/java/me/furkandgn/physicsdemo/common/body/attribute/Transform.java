package me.furkandgn.physicsdemo.common.body.attribute;

import org.joml.Vector2d;

import java.util.Objects;

/**
 * @author Furkan Doğan
 */
public class Transform {

  private final Vector2d position;
  private final Vector2d lastPosition;
  private final Vector2d scale;

  private float rotation = 0.0f;

  public Transform(int x, int y) {
    this.position = new Vector2d(x, y);
    this.lastPosition = new Vector2d(this.position);
    this.scale = new Vector2d(1, 1);
  }

  public Transform(Vector2d position,
                   Vector2d scale) {
    this.position = position;
    this.lastPosition = new Vector2d(position);
    this.scale = scale;
  }

  public static Transform of(Vector2d position, Vector2d scale) {
    return new Transform(position, scale);
  }

  public Vector2d position() {
    return this.position;
  }

  public Vector2d lastPosition() {
    return this.lastPosition;
  }

  public Vector2d scale() {
    return this.scale;
  }

  public float rotation() {
    return this.rotation;
  }

  public void rotation(float rotation) {
    this.rotation = rotation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    Transform transform = (Transform) o;
    return this.position.equals(transform.position) && this.scale.equals(transform.scale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.position, this.scale);
  }
}
