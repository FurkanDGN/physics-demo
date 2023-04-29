package me.furkandgn.physicsdemo.opengl.component.factory;

/**
 * @author Furkan Doğan
 */
public class RectangleIndicesFactory implements IndicesFactory {

  @Override
  public int[] createIndices(int maxBatchSize, int count) {
    int[] elements = new int[6 * count];
    for (int i = 0; i < count; i++) {
      this.loadElementIndices(elements, i);
    }

    return elements;
  }

  private void loadElementIndices(int[] elements, int index) {
    int triangleArrayIndex = 6 * index;
    int pointOffset = 4 * index;

    // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
    // Triangle 1
    elements[triangleArrayIndex] = pointOffset + 3;
    elements[triangleArrayIndex + 1] = pointOffset + 2;
    elements[triangleArrayIndex + 2] = pointOffset;

    // Triangle 2
    elements[triangleArrayIndex + 3] = pointOffset;
    elements[triangleArrayIndex + 4] = pointOffset + 2;
    elements[triangleArrayIndex + 5] = pointOffset + 1;
  }
}
