package me.furkandgn.physicsdemo.opengl.window.component.factory;

import me.furkandgn.physicsdemo.common.Body;
import me.furkandgn.physicsdemo.common.ColoredBody;
import me.furkandgn.physicsdemo.common.Point;
import org.joml.Vector2d;
import org.joml.Vector4f;

import java.util.List;

import static me.furkandgn.physicsdemo.opengl.Constants.VERTEX_SIZE;

/**
 * @author Furkan Doğan
 */
public class PolygonVerticesFactory implements VerticesFactory {

  private final Body body;
  private final int windowHeight;

  public PolygonVerticesFactory(Body body,
                                int windowHeight) {
    this.body = body;
    this.windowHeight = windowHeight;
  }

  @Override
  public float[] createVertices() {
    Vector2d scale = this.body.transform().scale();
    List<Point> points = this.body.points();

    float[] vertices = new float[points.size() * VERTEX_SIZE];

    for (int i = 0; i < points.size(); i++) {
      Point point = points.get(i);
      int start = i * VERTEX_SIZE;

      vertices[start] = (float) ((point.x() + this.body.x()) * scale.x);
      vertices[start + 1] = this.windowHeight - (float) ((this.body.y() + point.y()) * scale.y);

      Vector4f color = this.body instanceof ColoredBody coloredBody ? coloredBody.color() : new Vector4f(1f, 1f, 1f, 1f);

      vertices[start + 2] = color.x;
      vertices[start + 3] = color.y;
      vertices[start + 4] = color.z;
      vertices[start + 5] = color.w;
    }

    return vertices;
  }
}
