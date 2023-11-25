package me.furkandgn.physicsdemo.opengl;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import me.furkandgn.physicsdemo.common.body.surfaces.FlatSurfaceBody;
import me.furkandgn.physicsdemo.common.gui.World;
import me.furkandgn.physicsdemo.common.util.ColorUtil;
import me.furkandgn.physicsdemo.opengl.listener.AppListener;
import org.joml.Vector4f;

import java.util.*;

import static me.furkandgn.physicsdemo.common.constants.GuiConstants.HEIGHT;

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
    double worldWidth = 15;
    double worldHeight = 10.45082;
    double wallThickness = 0.5;

    this.world.addBody(new FlatSurfaceBody(wallThickness / 2, HEIGHT - (worldHeight / 2), wallThickness, worldHeight, color));
    this.world.addBody(new FlatSurfaceBody(worldWidth / 2, HEIGHT - wallThickness / 2, 15, wallThickness, color));
    this.world.addBody(new FlatSurfaceBody(worldWidth - wallThickness / 2, HEIGHT - (worldHeight / 2), wallThickness, worldHeight, color));
    this.world.addBody(new FlatSurfaceBody(worldWidth / 2, HEIGHT - worldHeight + wallThickness / 2, 15, wallThickness, color));
  }

  Map<UUID, List<Double>> heights = new HashMap<>();

  @Override
  public void onTick() {
    if (this.lastPut <= System.currentTimeMillis() - 10000) {
      this.spawnBody(this.world, 7);
      this.lastPut = System.currentTimeMillis();
    }

    List<Body> bodies = this.world.getBodies()
      .stream()
      .filter(body -> body instanceof CircleBody)
      .toList();

    for (Body body : bodies) {
      UUID uuid = body.uniqueId();
      List<Double> prevHeights = this.heights.computeIfAbsent(uuid, id -> new ArrayList<>(List.of(0d, body.y())));
      double currHeight = body.y();

//      double t1 = prevHeights.get(1);
//      double t2 = prevHeights.get(0);

//      if (t2 < t1 && t1 > currHeight) {
//        System.out.println("Peak: " + t1);
//      }

      prevHeights.add(currHeight);
      prevHeights.remove(0);

      this.heights.put(uuid, prevHeights);
    }
  }


  @Override
  public void onClose() {

  }

  private void spawnBody(World world, int x) {
    Body body = this.circleBody(x);
    Random random = new Random();
    int maxSpeed = 20;
    int minSpeed = -maxSpeed;
    body.velocity().add(random.nextDouble(minSpeed, maxSpeed), random.nextDouble(minSpeed, maxSpeed));
    world.addBody(body);
  }

  private CircleBody circleBody(int x) {
    int y = HEIGHT - 7;
    Vector4f color = ColorUtil.randomColor();
    return new CircleBody(0.5, 100, x, y, color);
  }

  private RectBody rectBody(int x) {
    int y = 600;
    Vector4f color = ColorUtil.randomColor();
    return new RectBody(50, 50, 1, x, y, color);
  }
}
