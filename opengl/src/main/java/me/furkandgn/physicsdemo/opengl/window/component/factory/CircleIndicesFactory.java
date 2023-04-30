package me.furkandgn.physicsdemo.opengl.window.component.factory;

import me.furkandgn.physicsdemo.opengl.window.constants.Shapes;

/**
 * @author Furkan Doğan
 */
public class CircleIndicesFactory implements IndicesFactory {

  @Override
  public int[] createIndices(int maxBatchSize, int count) {
    int dotCount = Shapes.CIRCLE.getDotCount();
    int[] elements = new int[count * dotCount];

    for (int i = 0; i < Math.max(1, count); i++) {
      this.loadElementIndices(elements, i);
    }

    return elements;
  }

  private void loadElementIndices(int[] elements, int index) {
    int dotCount = Shapes.CIRCLE.getDotCount();
    int pointOffset = dotCount * index;

    for (int i = pointOffset; i < dotCount * (index + 1); i += dotCount) {
      for (int j = 0; j < dotCount; j++) {
        elements[i] = i % dotCount;
      }
    }
  }
}
