package me.furkandgn.physicsdemo.common.collision;

import me.furkandgn.physicsdemo.common.body.Body;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public interface CollisionDetector<R> {

  R findCollisions(List<Body> bodies);
}
