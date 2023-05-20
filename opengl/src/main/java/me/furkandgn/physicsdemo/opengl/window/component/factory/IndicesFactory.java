package me.furkandgn.physicsdemo.opengl.window.component.factory;

import me.furkandgn.physicsdemo.common.Body;

/**
 * @author Furkan Doğan
 */
public interface IndicesFactory {

  int[] createIndices(Body body);
}
