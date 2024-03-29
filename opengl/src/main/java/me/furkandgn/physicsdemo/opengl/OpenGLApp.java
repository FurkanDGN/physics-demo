package me.furkandgn.physicsdemo.opengl;

import me.furkandgn.physicsdemo.common.collision.CollisionDetector;
import me.furkandgn.physicsdemo.common.collision.CollisionSolver;
import me.furkandgn.physicsdemo.common.collision.DefaultCollisionSolver;
import me.furkandgn.physicsdemo.common.collision.GJK;
import me.furkandgn.physicsdemo.common.gui.AppWindow;
import me.furkandgn.physicsdemo.common.gui.World;
import me.furkandgn.physicsdemo.common.physic.PhysicEngine;
import me.furkandgn.physicsdemo.common.physic.VerletPhysicEngine;
import me.furkandgn.physicsdemo.opengl.camera.Camera;
import me.furkandgn.physicsdemo.opengl.listener.AppListener;
import me.furkandgn.physicsdemo.opengl.window.OpenGlAppWindow;
import me.furkandgn.physicsdemo.opengl.world.SimulationWorld;
import org.joml.Vector2f;

import static me.furkandgn.physicsdemo.common.constants.GuiConstants.HEIGHT;
import static me.furkandgn.physicsdemo.common.constants.GuiConstants.WIDTH;

/**
 * @author Furkan Doğan
 */
public class OpenGLApp {

  public static void main(String[] args) {
    Camera camera = new Camera(new Vector2f(0, 0));
    CollisionSolver collisionSolver = new DefaultCollisionSolver();
    CollisionDetector collisionDetector = new GJK();
    PhysicEngine physicEngine = new VerletPhysicEngine(collisionDetector, collisionSolver);
    World world = new SimulationWorld(physicEngine, camera, HEIGHT);
    AppListener appListener = new DefaultAppListener(world);

    AppWindow appWindow = new OpenGlAppWindow("Physics Demo", WIDTH, HEIGHT, appListener);
    appWindow.init(world);
  }
}
