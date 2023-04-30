package me.furkandgn.physicsdemo.opengl;

import me.furkandgn.physicsdemo.common.PhysicEngine;
import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import me.furkandgn.physicsdemo.common.body.attribute.Transform;
import me.furkandgn.physicsdemo.common.engines.RK4PhysicEngine;
import me.furkandgn.physicsdemo.common.gui.window.AppWindow;
import me.furkandgn.physicsdemo.common.gui.world.World;
import me.furkandgn.physicsdemo.opengl.window.camera.Camera;
import me.furkandgn.physicsdemo.opengl.window.OpenGlAppWindow;
import me.furkandgn.physicsdemo.opengl.window.world.SimulationWorld;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

import static me.furkandgn.physicsdemo.common.gui.GuiConstants.HEIGHT;
import static me.furkandgn.physicsdemo.common.gui.GuiConstants.WIDTH;

/**
 * @author Furkan Doğan
 */
public class OpenGLApp {

  private static final SecureRandom secureRandom = new SecureRandom();

  public static void main(String[] args) {
    Camera camera = new Camera(new Vector2f(-WIDTH / 2f, -HEIGHT));

    PhysicEngine physicEngine = new RK4PhysicEngine();
    World world = new SimulationWorld(physicEngine, camera, HEIGHT);

    AtomicLong lastput = new AtomicLong(0);

    AppWindow appWindow = new OpenGlAppWindow("Physics Demo", WIDTH, HEIGHT, () -> {
      if (lastput.get() <= System.currentTimeMillis() - 1000L) {
        spawnBody(world, WIDTH / 2);
        lastput.set(System.currentTimeMillis());
      }
    });
    appWindow.init(world);
  }

  private static void spawnBody(World world, int x) {
    Body body = circleBody(x);
    world.addBody(body);
  }

  private static CircleBody circleBody(int x) {
    int y = 1500;
    float r = secureRandom.nextInt(255);
    float g = secureRandom.nextInt(255);
    float b = secureRandom.nextInt(255);
    Vector4f color = new Vector4f(r / 255f, g / 255f, b / 255f, 1);
    return new CircleBody(50, 1, x, y, color);
  }

  private static RectBody rectBody() {
    int x = 510 + secureRandom.nextInt(1100);
    int y = 400 + secureRandom.nextInt(500);
    Transform transform = new Transform(new Vector2d(x, y), new Vector2d(1, 1));
    float r = 80;
    float g = 80;
    float b = 140 + secureRandom.nextInt(115);
    Vector4f color = new Vector4f(r / 255f, g / 255f, b / 255f, 1);
    return new RectBody(50, 50, 1, transform, color);
  }
}
