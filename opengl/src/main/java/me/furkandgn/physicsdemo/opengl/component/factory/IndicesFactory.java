package me.furkandgn.physicsdemo.opengl.component.factory;

/**
 * @author Furkan Doğan
 */
public interface IndicesFactory {

  int[] createIndices(int maxBatchSize, int count);
}
