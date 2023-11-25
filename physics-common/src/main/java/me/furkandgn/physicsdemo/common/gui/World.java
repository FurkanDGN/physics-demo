package me.furkandgn.physicsdemo.common.gui;

import me.furkandgn.physicsdemo.common.body.Body;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public interface World {

  List<Body> getBodies();

  void addBody(Body body);

  void tick(double dt);

  void render();
}
