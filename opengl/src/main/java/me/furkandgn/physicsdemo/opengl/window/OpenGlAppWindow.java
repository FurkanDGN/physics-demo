package me.furkandgn.physicsdemo.opengl.window;

import me.furkandgn.physicsdemo.common.gui.scene.Scene;
import me.furkandgn.physicsdemo.common.gui.window.AppWindow;
import me.furkandgn.physicsdemo.common.gui.world.World;
import me.furkandgn.physicsdemo.common.util.TimeUtil;
import me.furkandgn.physicsdemo.opengl.scene.SimulationScene;
import me.furkandgn.physicsdemo.opengl.shader.Shader;
import me.furkandgn.physicsdemo.opengl.util.OpenGlUtil;
import me.furkandgn.physicsdemo.opengl.util.RenderUtil;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

/**
 * @author Furkan Doğan
 */
public class OpenGlAppWindow implements AppWindow {

  private static final int FPS_LIMIT = 60;

  private final int width;
  private final int height;
  private final String title;
  private final Consumer<Void> consumer;

  private Scene scene;
  private long window;
  private double beginTime;
  private double dt = -1.0f;

  public OpenGlAppWindow(String title,
                         int width,
                         int height,
                         Consumer<Void> consumer) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.consumer = consumer;
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
      if (this.beginTime == 0) {
        this.beginTime = System.nanoTime();
      }
      this.preLoop();
      this.update();
      this.postLoop();
    }
  }

  private void update() {
    this.scene.tick(this.dt);
    this.scene.render(this.dt);
  }

  private void preLoop() {
    this.consumer.accept(null);
    glfwPollEvents();
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }

  private void postLoop() {
    glfwSwapBuffers(this.window);
    this.updateTime();
    double dtMillis = this.dt * 1000;
    if (1000d / FPS_LIMIT > dtMillis) {
      long sleepTime = (long) ((1000d / FPS_LIMIT) - dtMillis);
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
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
