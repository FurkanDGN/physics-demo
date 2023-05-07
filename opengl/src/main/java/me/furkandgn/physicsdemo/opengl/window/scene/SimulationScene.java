package me.furkandgn.physicsdemo.opengl.window.scene;

import me.furkandgn.physicsdemo.common.gui.Scene;
import me.furkandgn.physicsdemo.common.gui.World;

/**
 * @author Furkan Doğan
 */
public class SimulationScene implements Scene {

  private final World world;

  public SimulationScene(World world) {
    this.world = world;
  }

  @Override
  public void tick(double dt) {
    if (dt != 0) {
      this.world.tick(dt);
    }
  }

  @Override
  public void render(double dt) {
    this.world.render();
  }
}
