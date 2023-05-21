package me.furkandgn.physicsdemo.opengl;

import me.furkandgn.physicsdemo.common.Body;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import me.furkandgn.physicsdemo.common.body.surfaces.FlatSurfaceBody;
import me.furkandgn.physicsdemo.common.gui.World;
import me.furkandgn.physicsdemo.opengl.window.listener.AppListener;
import me.furkandgn.physicsdemo.common.util.ColorUtil;
import org.joml.Vector4f;

import java.util.Random;

import static me.furkandgn.physicsdemo.common.constants.GuiConstants.HEIGHT;
import static me.furkandgn.physicsdemo.common.constants.GuiConstants.WIDTH;

/**
 * @author Furkan Doğan
 */
public class DefaultAppListener implements AppListener {

  private final World world;

  private long lastPut = 0;

  public DefaultAppListener(World world) {
    this.world = world;
  }

  @Override
  public void onInit() {
    Vector4f color = ColorUtil.getColor(105, 95, 163);
    this.world.addBody(new FlatSurfaceBody( 15, HEIGHT / 2, 30, HEIGHT, color));
    this.world.addBody(new FlatSurfaceBody(WIDTH / 2, 15, WIDTH, 30, color));
    this.world.addBody(new FlatSurfaceBody( WIDTH - 15, HEIGHT / 2, 30, HEIGHT, color));
    this.world.addBody(new FlatSurfaceBody(WIDTH / 2, HEIGHT - 15, WIDTH, 30,  color));
  }

  @Override
  public void onTick() {
    if (this.lastPut <= System.currentTimeMillis() - 10000000L) {
      this.spawnBody(this.world, WIDTH - 200);
      this.lastPut = System.currentTimeMillis();
    }
  }

  @Override
  public void onClose() {

  }

  private void spawnBody(World world, int x) {
    Body body = this.circleBody(x);
    Random random = new Random();
//    body.velocity().add(100, 0);
    body.velocity().add(100 - random.nextInt(200), 100 - random.nextInt(200));
//    body.force(new Vector2d(0, 200));
    world.addBody(body);
  }

  private CircleBody circleBody(int x) {
    int y = 210;
    Vector4f color = ColorUtil.randomColor();
    return new CircleBody(60, 100, x, y, color);
  }

  private RectBody rectBody(int x) {
    int y = 600;
    Vector4f color = ColorUtil.randomColor();
    return new RectBody(50, 50, 1, x, y, color);
  }
}
