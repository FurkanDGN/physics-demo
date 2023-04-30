package me.furkandgn.physicsdemo.opengl;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import me.furkandgn.physicsdemo.common.body.surfaces.FlatSurfaceBody;
import me.furkandgn.physicsdemo.common.gui.world.World;
import me.furkandgn.physicsdemo.opengl.window.listener.AppListener;
import org.joml.Vector4f;

import java.security.SecureRandom;

import static me.furkandgn.physicsdemo.common.gui.GuiConstants.WIDTH;

/**
 * @author Furkan Doğan
 */
public class DefaultAppListener implements AppListener {

  private static final SecureRandom secureRandom = new SecureRandom();

  private final World world;

  private long lastPut = 0;

  public DefaultAppListener(World world) {
    this.world = world;
  }

  @Override
  public void onInit() {
    this.world.addBody(new FlatSurfaceBody(WIDTH, 200, 0, 0, 200, new Vector4f(1.f, 1.f, 1.f, 1.f)));
  }

  @Override
  public void onTick() {
    if (this.lastPut <= System.currentTimeMillis() - 1000L) {
      this.spawnBody(this.world, WIDTH / 2);
      this.lastPut = System.currentTimeMillis();
    }
  }

  @Override
  public void onClose() {

  }

  private void spawnBody(World world, int x) {
    Body body = this.circleBody(x);
    world.addBody(body);
  }

  private CircleBody circleBody(int x) {
    int y = 1500;
    float r = secureRandom.nextInt(255);
    float g = secureRandom.nextInt(255);
    float b = secureRandom.nextInt(255);
    Vector4f color = new Vector4f(r / 255f, g / 255f, b / 255f, 1);
    return new CircleBody(50, 1, x, y, color);
  }

  private RectBody rectBody(int x) {
    int y = 1500;
    float r = secureRandom.nextInt(255);
    float g = secureRandom.nextInt(255);
    float b = secureRandom.nextInt(255);
    Vector4f color = new Vector4f(r / 255f, g / 255f, b / 255f, 1);
    return new RectBody(50, 50, 1, x, y, color);
  }
}
