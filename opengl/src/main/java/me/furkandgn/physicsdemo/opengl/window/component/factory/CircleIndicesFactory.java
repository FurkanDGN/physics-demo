package me.furkandgn.physicsdemo.opengl.window.component.factory;

import me.furkandgn.physicsdemo.common.Body;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;

/**
 * @author Furkan Doğan
 */
public class CircleIndicesFactory implements IndicesFactory {

  @Override
  public int[] createIndices(Body body) {
    if (body instanceof CircleBody circleBody) {
      int cornerCount = circleBody.points().size();
      int[] indices = new int[(cornerCount) * 3];
      for (int i = 0; i < cornerCount; i++) {
        indices[i * 3] = 0;
        indices[i * 3 + 1] = i;
        indices[i * 3 + 2] = i + 1 == cornerCount ? 1 : i + 1;
      }

      return indices;
    } else {
      throw new UnsupportedOperationException("Circle body expected");
    }
  }
}
