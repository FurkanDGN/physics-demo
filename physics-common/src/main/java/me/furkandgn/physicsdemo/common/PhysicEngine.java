package me.furkandgn.physicsdemo.common;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public interface PhysicEngine {

  void evaluate(List<Body> body, double dt);
}
