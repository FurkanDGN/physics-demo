package me.furkandgn.physicsdemo.common.body;

import me.furkandgn.physicsdemo.common.body.attribute.Transform;
import org.joml.Vector2d;

import java.util.UUID;

/**
 * @author Furkan Doğan
 */
public abstract class Body {

  protected final float mass;
  protected final Transform transform;
  private final UUID uniqueId;

  protected Vector2d velocity;
  protected Vector2d force;

  public Body(float mass,
              Transform transform) {
    this.mass = mass;
    this.transform = transform;
    this.velocity = new Vector2d();
    this.force = new Vector2d();
    this.uniqueId = UUID.randomUUID();
  }

  public boolean isSurface() {
    return false;
  }

  public abstract int width();

  public abstract int height();

  public abstract boolean canCollide(Body anotherBody);

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

  public void velocity(Vector2d velocity) {
    this.velocity = velocity;
  }

  public Vector2d force() {
    return this.force;
  }

  public void force(Vector2d force) {
    this.force = force;
  }

  public UUID uniqueId() {
    return this.uniqueId;
  }
}
