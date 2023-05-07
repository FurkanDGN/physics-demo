package me.furkandgn.physicsdemo.common;

import me.furkandgn.physicsdemo.common.collision.swp.CollisionEvent;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public interface CollisionSolver {

  void solveCollisions(List<CollisionEvent> collisions);
}
