package me.furkandgn.physicsdemo.common;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public interface CollisionSolver {

  void solveCollisions(List<CollisionEvent> collisions);
}
