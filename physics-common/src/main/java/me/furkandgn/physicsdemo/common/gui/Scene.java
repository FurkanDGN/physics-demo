package me.furkandgn.physicsdemo.common.gui;

/**
 * @author Furkan Doğan
 */
public interface Scene {

  void init();

  void tick(double dt);

  void render(double dt);
}
