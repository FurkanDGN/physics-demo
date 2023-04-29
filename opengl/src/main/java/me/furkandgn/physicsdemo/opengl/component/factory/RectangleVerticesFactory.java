package me.furkandgn.physicsdemo.opengl.component.factory;

import me.furkandgn.physicsdemo.common.body.RectBody;
import org.joml.Vector4f;

import static me.furkandgn.physicsdemo.opengl.Constants.VERTEX_SIZE;

/**
 * @author Furkan Doğan
 */
public class RectangleVerticesFactory implements VerticesFactory {

  private final RectBody body;
  private final int windowHeight;

  public RectangleVerticesFactory(RectBody rectBody,
                                  int windowHeight) {
    this.body = rectBody;
    this.windowHeight = windowHeight;
  }

  @Override
  public void createVertices(float[] vertices, int index) {
    this.create(vertices, index);
  }

  private void create(float[] vertices, int index) {
    // Find offset within array (4 vertices per sprite)
    int offset = index * 4 * VERTEX_SIZE;
    Vector4f color = this.body.color();

    // Add vertices with the appropriate properties
    float xAdd = 0.0f;
    float yAdd = 0.0f;
    for (int i = 0; i < 4; i++) {
      if (i == 1) {
        yAdd = 2.0f;
      } else if (i == 2) {
        xAdd = 2.0f;
      } else if (i == 3) {
        yAdd = 0.0f;
      }

      Vector4f currentPos = new Vector4f(
        (float) (this.body.transform().position().x + (xAdd * this.body.transform().scale().x * this.body.width())),
        (float) (this.body.transform().position().y + (yAdd * this.body.transform().scale().y * this.body.height())),
        0, 1);

      // Load position
      vertices[offset] = currentPos.x;
      vertices[offset + 1] = this.windowHeight + (this.body.height() * 2) - currentPos.y;

      // Load color
      vertices[offset + 2] = color.x;
      vertices[offset + 3] = color.y;
      vertices[offset + 4] = color.z;
      vertices[offset + 5] = color.w;

      offset += VERTEX_SIZE;
    }
  }
}
