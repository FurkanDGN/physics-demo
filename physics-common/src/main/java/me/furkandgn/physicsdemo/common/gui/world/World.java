package me.furkandgn.physicsdemo.common.gui.world;

import me.furkandgn.physicsdemo.common.body.Body;

/**
 * @author Furkan Doğan
 */
public interface World {

  void addBody(Body body);

  void tick(double dt);

  void render();
}
