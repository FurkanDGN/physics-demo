package me.furkandgn.physicsdemo.opengl;

import me.furkandgn.physicsdemo.common.CollisionManager;
import me.furkandgn.physicsdemo.common.PhysicEngine;
import me.furkandgn.physicsdemo.common.engines.VerletPhysicEngine;
import me.furkandgn.physicsdemo.common.gui.window.AppWindow;
import me.furkandgn.physicsdemo.common.gui.world.World;
import me.furkandgn.physicsdemo.common.manager.DefaultCollisionManager;
import me.furkandgn.physicsdemo.opengl.window.OpenGlAppWindow;
import me.furkandgn.physicsdemo.opengl.window.camera.Camera;
import me.furkandgn.physicsdemo.opengl.window.listener.AppListener;
import me.furkandgn.physicsdemo.opengl.window.world.SimulationWorld;
import org.joml.Vector2f;

import static me.furkandgn.physicsdemo.common.gui.GuiConstants.HEIGHT;
import static me.furkandgn.physicsdemo.common.gui.GuiConstants.WIDTH;

/**
 * @author Furkan Doğan
 */
public class OpenGLApp {

  public static void main(String[] args) {
    Camera camera = new Camera(new Vector2f(0, 0));

    CollisionManager collisionManager = new DefaultCollisionManager();
    PhysicEngine physicEngine = new VerletPhysicEngine(collisionManager);
    World world = new SimulationWorld(physicEngine, camera, HEIGHT);
    AppListener appListener = new DefaultAppListener(world);

    AppWindow appWindow = new OpenGlAppWindow("Physics Demo", WIDTH, HEIGHT, appListener);
    appWindow.init(world);
  }
}
