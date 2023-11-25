package me.furkandgn.physicsdemo.common.physic;

import me.furkandgn.physicsdemo.common.body.Body;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public interface PhysicEngine {

  void evaluate(List<Body> body, double dt);
}
