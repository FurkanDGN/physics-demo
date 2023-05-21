package me.furkandgn.physicsdemo.common;

import java.util.List;
import java.util.Set;

/**
 * @author Furkan Doğan
 */
public interface CollisionDetector {

  Set<CollisionEvent> findCollisions(List<Body> bodies);
}
