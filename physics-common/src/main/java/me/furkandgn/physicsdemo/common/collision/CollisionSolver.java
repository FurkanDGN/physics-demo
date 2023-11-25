package me.furkandgn.physicsdemo.common.collision;

import java.util.Set;

/**
 * @author Furkan Doğan
 */
public interface CollisionSolver {

  void solveCollisions(Set<CollisionEvent> collisions);
}
