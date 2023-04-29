package me.furkandgn.physicsdemo.opengl.camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static me.furkandgn.physicsdemo.common.gui.GuiConstants.HEIGHT;
import static me.furkandgn.physicsdemo.common.gui.GuiConstants.WIDTH;

public class Camera {

  private final Matrix4f projectionMatrix;
  private final Matrix4f viewMatrix;

  public Vector2f position;

  public Camera(Vector2f position) {
    this.position = position;
    this.projectionMatrix = new Matrix4f();
    this.viewMatrix = new Matrix4f();
    this.adjustProjection();
  }

  public void adjustProjection() {
    this.projectionMatrix.identity();
    this.projectionMatrix.ortho(0, WIDTH * 2, HEIGHT * 2, 0, 0.0f, 1.0f);
  }

  public Matrix4f viewMatrix() {
    Vector3f cameraFront = new Vector3f(0.0f, 0.0f, 0.0f);
    Vector3f cameraUp = new Vector3f(0, 1.0f, 0.0f);
    this.viewMatrix.identity();
    this.viewMatrix.lookAt(new Vector3f(this.position.x, this.position.y, 1.0f),
      cameraFront.add(this.position.x, this.position.y, 0f),
      cameraUp);

    return this.viewMatrix;
  }

  public Matrix4f projectionMatrix() {
    return this.projectionMatrix;
  }
}