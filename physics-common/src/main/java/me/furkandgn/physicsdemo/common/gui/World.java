package me.furkandgn.physicsdemo.common.gui;

import me.furkandgn.physicsdemo.common.Body;

/**
 * @author Furkan Doğan
 */
public interface World {

  void addBody(Body body);

  void tick(double dt);

  void render();
}
