package me.furkandgn.physicsdemo.opengl.component.factory;

import me.furkandgn.physicsdemo.common.body.Body;

/**
 * @author Furkan Doğan
 */
public interface IndicesFactory {

  int[] createIndices(Body body);
}
