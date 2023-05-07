package me.furkandgn.physicsdemo.common.gui;

/**
 * @author Furkan Doğan
 */
public interface Scene {

  void tick(double dt);

  void render(double dt);

  void reset();
}
