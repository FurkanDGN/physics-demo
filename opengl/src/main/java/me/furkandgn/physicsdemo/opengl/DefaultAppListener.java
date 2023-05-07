package me.furkandgn.physicsdemo.opengl;

import me.furkandgn.physicsdemo.common.Body;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import me.furkandgn.physicsdemo.common.body.surfaces.FlatSurfaceBody;
import me.furkandgn.physicsdemo.common.gui.World;
import me.furkandgn.physicsdemo.opengl.window.listener.AppListener;
import me.furkandgn.physicsdemo.opengl.window.util.ColorUtil;
import org.joml.Vector4f;

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
    this.world.addBody(new FlatSurfaceBody(30, HEIGHT, 15, HEIGHT / 2, color));
    this.world.addBody(new FlatSurfaceBody(WIDTH, 30, WIDTH / 2, 15, color));
    this.world.addBody(new FlatSurfaceBody(30, HEIGHT, WIDTH - 15, HEIGHT / 2, color));
    this.world.addBody(new FlatSurfaceBody(WIDTH, 30, WIDTH / 2, HEIGHT - 15, color));

  }

  @Override
  public void onTick() {
    if (this.lastPut <= System.currentTimeMillis() - 10000000L) {
      this.spawnBody(this.world, WIDTH / 2);
      this.lastPut = System.currentTimeMillis();
    }
  }

  @Override
  public void onClose() {

  }

  private void spawnBody(World world, int x) {
    Body body = this.circleBody(x);
//    body.velocity().add(100, 0);
//    body.force(new Vector2d(0, 200));
    world.addBody(body);
  }

  private CircleBody circleBody(int x) {
    int y = 600;
    Vector4f color = ColorUtil.randomColor();
    return new CircleBody(20, 100, x, y, color);
  }

  private RectBody rectBody(int x) {
    int y = 800;
    Vector4f color = ColorUtil.randomColor();
    return new RectBody(50, 50, 1, x, y, color);
  }
}
