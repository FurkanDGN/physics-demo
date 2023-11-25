package me.furkandgn.physicsdemo.common.body;

import org.joml.Vector2d;

/**
 * @author Furkan Doğan
 */
public class Transform {

  private final Vector2d position;
  private final Vector2d lastPosition;
  private final Vector2d scale;

  private float rotation = 0.0f;

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
  public String toString() {
    return "Transform{" +
      "position=" + this.position +
      ", lastPosition=" + this.lastPosition +
      ", scale=" + this.scale +
      ", rotation=" + this.rotation +
      '}';
  }
}
