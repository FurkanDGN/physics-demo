package me.furkandgn.physicsdemo.opengl.window;

import me.furkandgn.physicsdemo.common.gui.scene.Scene;
import me.furkandgn.physicsdemo.common.gui.window.AppWindow;
import me.furkandgn.physicsdemo.common.gui.world.World;
import me.furkandgn.physicsdemo.common.util.TimeUtil;
import me.furkandgn.physicsdemo.opengl.window.listener.TickListener;
import me.furkandgn.physicsdemo.opengl.window.scene.SimulationScene;
import me.furkandgn.physicsdemo.opengl.window.shader.Shader;
import me.furkandgn.physicsdemo.opengl.window.util.OpenGlUtil;
import me.furkandgn.physicsdemo.opengl.window.util.RenderUtil;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;
import java.util.concurrent.locks.LockSupport;

import static me.furkandgn.physicsdemo.common.util.TimeUtil.SECOND_TO_NANO;
import static me.furkandgn.physicsdemo.opengl.Constants.FPS_LIMIT;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

/**
 * @author Furkan Doğan
 */
public class OpenGlAppWindow implements AppWindow {

  // BACKGROUND COLOR
  private static final float BRIGHTNESS = 0.3f;
  private static final float RED = (0x63 / 255f) * BRIGHTNESS;
  private static final float GREEN = (0x59 / 255f) * BRIGHTNESS;
  private static final float BLUE = (0x85 / 255f) * BRIGHTNESS;

  private final int width;
  private final int height;
  private final String title;
  private final TickListener tickListener;

  private Scene scene;
  private long window;
  private long beginTime;
  private long dt = -1;

  public OpenGlAppWindow(String title,
                         int width,
                         int height,
                         TickListener tickListener) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.tickListener = tickListener;
  }

  @Override
  public void init(World world) {
    this.scene = new SimulationScene(world);
    this.initOpenGl();
    this.startLoop();
  }

  private void initOpenGl() {
    this.window = OpenGlUtil.initOpenGl(this.width, this.height, this.title);
    Shader shader = new Shader();
    shader.create();
    RenderUtil.bindShader(shader);
  }

  private void startLoop() {
    this.showWindow();
    this.loop();
    this.destroyWindow();
  }

  private void loop() {
    glfwPollEvents();
    while (!glfwWindowShouldClose(this.window)) {
      glClearColor(RED, GREEN, BLUE, 1f);
      if (this.beginTime == 0) {
        this.beginTime = System.nanoTime();
      }
      this.preLoop();
      this.update();
      this.postLoop();
    }
  }

  private void update() {
    this.scene.tick(this.dt / SECOND_TO_NANO);
    this.tickListener.onTick();
    this.scene.render(this.dt / SECOND_TO_NANO);
  }

  private void preLoop() {
    glfwPollEvents();
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }

  private void postLoop() {
    glfwSwapBuffers(this.window);
    this.updateTime();
    if (SECOND_TO_NANO / FPS_LIMIT > this.dt) {
      double sleepTime = (SECOND_TO_NANO / FPS_LIMIT) - this.dt;
      LockSupport.parkNanos((long) sleepTime - 500000);
      this.updateTime();
    }
  }

  private void updateTime() {
    this.dt = TimeUtil.deltaTime(this.beginTime);
    this.beginTime = TimeUtil.getTime();
  }

  private void showWindow() {
    glfwShowWindow(this.window);
  }

  private void destroyWindow() {
    Callbacks.glfwFreeCallbacks(this.window);
    glfwDestroyWindow(this.window);

    glfwTerminate();
    Objects.requireNonNull(glfwSetErrorCallback(null)).free();
  }
}
