package me.furkandgn.physicsdemo.common;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.collision.swp.CollisionEvent;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public interface CollisionDetector {

  List<CollisionEvent> findCollisions(List<Body> bodies);
}
