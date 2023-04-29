package me.furkandgn.physicsdemo.common.gui.scene;

/**
 * @author Furkan Doğan
 */
public interface Scene {

  void tick(double dt);

  void render(double dt);

  void reset();
}
