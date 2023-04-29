package me.furkandgn.physicsdemo.opengl.scene;

import me.furkandgn.physicsdemo.common.gui.scene.Scene;
import me.furkandgn.physicsdemo.common.gui.world.World;

/**
 * @author Furkan Doğan
 */
public class SimulationScene implements Scene {

  private final World world;

  private long firstRender = 0;
  private short frameCount = 0;

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
    if (this.firstRender == 0) {
      this.firstRender = System.currentTimeMillis();
    } else if (this.firstRender <= System.currentTimeMillis() - 1000) {
      System.out.println("FPS: " + this.frameCount);
      this.firstRender = System.currentTimeMillis();
      this.frameCount = 0;
    }

    this.world.render();
    this.frameCount++;
  }

  @Override
  public void reset() {
  }
}
