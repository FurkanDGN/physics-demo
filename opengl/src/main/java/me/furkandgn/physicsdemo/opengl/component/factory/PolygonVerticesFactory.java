package me.furkandgn.physicsdemo.opengl.component.factory;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.ColoredBody;
import me.furkandgn.physicsdemo.common.util.Point;
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

  public PolygonVerticesFactory(Body body, int windowHeight) {
    this.body = body;
    this.windowHeight = windowHeight;
  }

  @Override
  public float[] createVertices() {
    Vector2d scale = this.body.transform().scale();
    float rotation = this.body.transform().rotation();
    List<Point> points = this.body.points();

    float[] vertices = new float[points.size() * VERTEX_SIZE];

    for (int i = 0; i < points.size(); i++) {
      Point point = points.get(i);
      int start = i * VERTEX_SIZE;

      double rotatedX = point.x() * Math.cos(rotation) - point.y() * Math.sin(rotation);
      double rotatedY = point.x() * Math.sin(rotation) + point.y() * Math.cos(rotation);

      vertices[start] = (float) ((rotatedX + this.body.x()) * scale.x);
      vertices[start + 1] = this.windowHeight - (float) ((rotatedY + this.body.y()) * scale.y);

      Vector4f color = this.body instanceof ColoredBody coloredBody ? coloredBody.color() : new Vector4f(1f, 1f, 1f, 1f);

      vertices[start + 2] = color.x;
      vertices[start + 3] = color.y;
      vertices[start + 4] = color.z;
      vertices[start + 5] = color.w;
    }

    return vertices;
  }
}
