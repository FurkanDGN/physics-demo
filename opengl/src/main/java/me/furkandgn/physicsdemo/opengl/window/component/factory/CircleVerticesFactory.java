package me.furkandgn.physicsdemo.opengl.window.component.factory;

import me.furkandgn.physicsdemo.common.body.CircleBody;
import org.joml.Vector2d;
import org.joml.Vector4f;

import static me.furkandgn.physicsdemo.opengl.Constants.CIRCLE_CORNERS;
import static me.furkandgn.physicsdemo.opengl.Constants.VERTEX_SIZE;

/**
 * @author Furkan Doğan
 */
public class CircleVerticesFactory implements VerticesFactory {

  private final CircleBody body;
  private final int windowHeight;

  public CircleVerticesFactory(CircleBody body,
                               int windowHeight) {
    this.body = body;
    this.windowHeight = windowHeight;
  }

  @Override
  public void createVertices(float[] vertices, int index) {
    this.create(vertices, index);
  }

  private void create(float[] vertices, int index) {
    int offset = index * (CIRCLE_CORNERS + 2) * VERTEX_SIZE;
    Vector4f color = this.body.color();

    double v = Math.PI * 2 / CIRCLE_CORNERS;

    int radius = this.body.radius();

    Vector2d position = this.body.transform().position();
    Vector2d scale = this.body.transform().scale();
    this.setVertex(vertices, offset, radius, new Vector2d(position.x, position.y), color);

    for (int i = 0; i <= CIRCLE_CORNERS; i++) {
      float x = (float) (Math.cos(v * i) * radius * scale.x + position.x);
      float y = (float) (Math.sin(v * i) * radius * scale.y + position.y);

      this.setVertex(vertices, offset + VERTEX_SIZE * (i + 1), radius, new Vector2d(x, y), color);
    }
  }

  private void setVertex(float[] vertices, int offset, int radius, Vector2d position, Vector4f color) {
    vertices[offset] = (float) position.x;
    vertices[offset + 1] = (float) (this.windowHeight + this.body.radius() - position.y);

    vertices[offset + 2] = color.x;
    vertices[offset + 3] = color.y;
    vertices[offset + 4] = color.z;
    vertices[offset + 5] = color.w;
  }
}
