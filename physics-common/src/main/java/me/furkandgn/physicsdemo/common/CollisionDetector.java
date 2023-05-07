package me.furkandgn.physicsdemo.common;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public interface CollisionDetector {

  List<CollisionEvent> findCollisions(List<Body> bodies);
}
