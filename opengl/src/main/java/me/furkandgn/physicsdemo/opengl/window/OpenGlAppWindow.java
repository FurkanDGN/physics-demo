package me.furkandgn.physicsdemo.opengl.window;

import me.furkandgn.physicsdemo.common.gui.AppWindow;
import me.furkandgn.physicsdemo.common.gui.Scene;
import me.furkandgn.physicsdemo.common.gui.World;
import me.furkandgn.physicsdemo.common.util.TimeUtil;
import me.furkandgn.physicsdemo.opengl.window.listener.AppListener;
import me.furkandgn.physicsdemo.opengl.window.scene.SimulationScene;
import me.furkandgn.physicsdemo.opengl.window.shader.Shader;
import me.furkandgn.physicsdemo.opengl.window.util.OpenGlOptions;
import me.furkandgn.physicsdemo.opengl.window.util.OpenGlUtil;
import me.furkandgn.physicsdemo.opengl.window.util.PerformanceTracker;
import me.furkandgn.physicsdemo.opengl.window.util.RenderUtil;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;
import java.util.concurrent.locks.LockSupport;

import static me.furkandgn.physicsdemo.common.util.TimeUtil.SECOND_TO_NANOS;
import static me.furkandgn.physicsdemo.opengl.Constants.FPS_LIMIT;
import static me.furkandgn.physicsdemo.opengl.Constants.FPS_TIME_OFFSET;
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
  private static final PerformanceTracker PERFORMANCE_TRACKER = new PerformanceTracker();

  private final int width;
  private final int height;
  private final String title;
  private final AppListener appListener;

  private Scene scene;
  private long window;
  private long beginTime;
  private long dt = 10000000; // initial delta time
  private boolean focused;

  public OpenGlAppWindow(String title,
                         int width,
                         int height,
                         AppListener appListener) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.appListener = appListener;
  }

  @Override
  public void init(World world) {
    this.scene = new SimulationScene(world);
    this.initOpenGl();
    this.appListener.onInit();
    this.startLoop();
  }

  private void initOpenGl() {
    OpenGlOptions openGlOptions = new OpenGlOptions(this.width, this.height, this.title, focused -> {
      this.focused = focused;
    });
    this.window = OpenGlUtil.initOpenGl(openGlOptions);
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
    glClearColor(RED, GREEN, BLUE, 1f);
    this.beginTime = System.nanoTime();
    while (!glfwWindowShouldClose(this.window)) {
      this.preLoop();
      this.update();
      this.postLoop();
    }
  }


  private void update() {
    double dtInDouble = this.dt / SECOND_TO_NANOS;
    long now = System.nanoTime();

    if (this.focused) {
      this.scene.tick(dtInDouble);
      this.appListener.onTick();
    }

    long last = System.nanoTime();
    PERFORMANCE_TRACKER.recordTickTime(last - now);
    now = System.nanoTime();

    this.scene.render(dtInDouble);

    last = System.nanoTime();
    PERFORMANCE_TRACKER.recordRenderTime(last - now);
    PERFORMANCE_TRACKER.countFrame();
  }

  private void preLoop() {
    glfwPollEvents();
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }

  private void postLoop() {
    glfwSwapBuffers(this.window);
    this.updateTime();
    this.limitFrameRate();
  }

  private void limitFrameRate() {
    long framePerTime = Math.round(SECOND_TO_NANOS / FPS_LIMIT);
    long currentTime = System.nanoTime();

    if (framePerTime > this.dt) {
      long sleepTime = framePerTime - this.dt;
      long targetTime = currentTime + sleepTime;
      int offset = FPS_TIME_OFFSET * FPS_LIMIT;

      while (System.nanoTime() < targetTime) {
        LockSupport.parkNanos(targetTime - System.nanoTime() - offset);
      }

      this.updateTime();
    }
  }

  private void showWindow() {
    glfwShowWindow(this.window);
  }

  private void destroyWindow() {
    Callbacks.glfwFreeCallbacks(this.window);
    glfwDestroyWindow(this.window);
    this.appListener.onClose();

    glfwTerminate();
    Objects.requireNonNull(glfwSetErrorCallback(null)).free();
  }

  private void updateTime() {
    this.dt = TimeUtil.getDeltaTimeNanos(this.beginTime);
    this.beginTime = TimeUtil.getCurrentTimeNanos();
  }
}
