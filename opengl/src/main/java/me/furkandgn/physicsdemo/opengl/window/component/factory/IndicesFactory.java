package me.furkandgn.physicsdemo.opengl.window.component.factory;

/**
 * @author Furkan Doğan
 */
public interface IndicesFactory {

  int[] createIndices(int maxBatchSize, int count);
}
