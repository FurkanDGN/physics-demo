package me.furkandgn.physicsdemo.opengl;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import me.furkandgn.physicsdemo.common.body.surfaces.FlatSurfaceBody;
import me.furkandgn.physicsdemo.common.gui.world.World;
import me.furkandgn.physicsdemo.opengl.window.listener.AppListener;
import me.furkandgn.physicsdemo.opengl.window.util.ColorUtil;
import org.joml.Vector4f;

import static me.furkandgn.physicsdemo.common.gui.GuiConstants.HEIGHT;
import static me.furkandgn.physicsdemo.common.gui.GuiConstants.WIDTH;

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
    this.world.addBody(new FlatSurfaceBody(30, HEIGHT, 15, HEIGHT / 2, color));
    this.world.addBody(new FlatSurfaceBody(WIDTH, 30, WIDTH / 2, 15, color));
    this.world.addBody(new FlatSurfaceBody(30, HEIGHT, WIDTH - 15, HEIGHT / 2, color));
    this.world.addBody(new FlatSurfaceBody(WIDTH, 30, WIDTH / 2, HEIGHT - 15, color));

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
    body.velocity().add(-10, 0);
    world.addBody(body);
  }

  private CircleBody circleBody(int x) {
    int y = 200;
    Vector4f color = ColorUtil.randomColor();
    return new CircleBody(20, 1, x, y, color);
  }

  private RectBody rectBody(int x) {
    int y = 800;
    Vector4f color = ColorUtil.randomColor();
    return new RectBody(50, 50, 1, x, y, color);
  }
}
