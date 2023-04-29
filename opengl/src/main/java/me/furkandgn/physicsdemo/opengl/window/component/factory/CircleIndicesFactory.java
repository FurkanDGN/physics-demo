package me.furkandgn.physicsdemo.opengl.window.component.factory;

import static me.furkandgn.physicsdemo.opengl.Constants.CIRCLE_CORNERS;

/**
 * @author Furkan Doğan
 */
public class CircleIndicesFactory implements IndicesFactory {

  @Override
  public int[] createIndices(int maxBatchSize, int count) {
    int[] elements = new int[count * (CIRCLE_CORNERS + 2)];

    for (int i = 0; i < Math.max(1, count); i++) {
      this.loadElementIndices(elements, i);
    }

    return elements;
  }

  private void loadElementIndices(int[] elements, int index) {
    int triangleArraySize = CIRCLE_CORNERS + 2;
    int pointOffset = (CIRCLE_CORNERS + 2) * index;

    for (int i = pointOffset; i < triangleArraySize * (index + 1); i += triangleArraySize) {
      for (int j = 0; j < CIRCLE_CORNERS + 2; j++) {
        elements[i] = i % (CIRCLE_CORNERS + 2);
      }
    }
  }
}
