package me.furkandgn.physicsdemo.opengl.window;

import me.furkandgn.physicsdemo.common.gui.AppWindow;
import me.furkandgn.physicsdemo.common.gui.Scene;
import me.furkandgn.physicsdemo.common.gui.World;
import me.furkandgn.physicsdemo.common.util.MetricType;
import me.furkandgn.physicsdemo.common.util.PerformanceTracker;
import me.furkandgn.physicsdemo.common.util.TimeUtil;
import me.furkandgn.physicsdemo.opengl.camera.Camera;
import me.furkandgn.physicsdemo.opengl.listener.AppListener;
import me.furkandgn.physicsdemo.opengl.scene.SimulationScene;
import me.furkandgn.physicsdemo.opengl.shader.Shader;
import me.furkandgn.physicsdemo.opengl.util.OpenGlOptions;
import me.furkandgn.physicsdemo.opengl.util.OpenGlUtil;
import me.furkandgn.physicsdemo.opengl.util.RenderUtil;
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

  public static final PerformanceTracker PERFORMANCE_TRACKER = new PerformanceTracker();

  // BACKGROUND COLOR
  private static final float BRIGHTNESS = 0.3f;
  private static final float RED = (0x63 / 255f) * BRIGHTNESS;
  private static final float GREEN = (0x59 / 255f) * BRIGHTNESS;
  private static final float BLUE = (0x85 / 255f) * BRIGHTNESS;

  private final int width;
  private final int height;
  private final String title;
  private final Camera camera;
  private final AppListener appListener;

  private Scene scene;
  private long window;
  private long beginTime;
  private long dt = 10000000; // initial delta time
  private boolean focused;

  public OpenGlAppWindow(String title, int width, int height, Camera camera, AppListener appListener) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.camera = camera;
    this.appListener = appListener;
  }

  @Override
  public void init(World world) {
    this.scene = new SimulationScene(world, this.camera);
    this.initOpenGl();
    this.appListener.onInit();
    this.startLoop();
  }

  private void initOpenGl() {
    OpenGlOptions openGlOptions = new OpenGlOptions(this.width, this.height, this.title, focused -> this.focused = focused);
    this.window = OpenGlUtil.initOpenGl(openGlOptions);
    Shader shader = new Shader();
    shader.create();
    RenderUtil.bindShader(shader);
  }

  private void startLoop() {
    this.scene.init();
    this.showWindow();
    this.loop();
    this.destroyWindow();
  }

  private void loop() {
    glfwPollEvents();
    glClearColor(RED, GREEN, BLUE, 1f);
    this.beginTime = System.nanoTime();
    while (!glfwWindowShouldClose(this.window)) {
      long now = System.nanoTime();
      this.preLoop();
      this.update();
      this.postLoop();
      long last = System.nanoTime();
      PERFORMANCE_TRACKER.recordMetric(MetricType.FRAME, last - now);
      PERFORMANCE_TRACKER.countFrame();
      this.updateTime();
      this.limitFrameRate();
    }
  }

  private void update() {
    double dtNanos = this.dt / SECOND_TO_NANOS;
    long now = System.nanoTime();

    if (this.focused) {
      this.scene.tick(dtNanos);
      this.appListener.onTick();
    }

    long last = System.nanoTime();
    PERFORMANCE_TRACKER.recordMetric(MetricType.TICK, last - now);
    now = System.nanoTime();

    this.scene.render(dtNanos);

    last = System.nanoTime();
    PERFORMANCE_TRACKER.recordMetric(MetricType.RENDER, last - now);
  }

  private void preLoop() {
    long now = System.nanoTime();
    glfwPollEvents();
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    long last = System.nanoTime();
    PERFORMANCE_TRACKER.recordMetric(MetricType.PRE, last - now);
  }

  private void postLoop() {
    long now = System.nanoTime();
    glfwSwapBuffers(this.window);
    long last = System.nanoTime();
    PERFORMANCE_TRACKER.recordMetric(MetricType.POST, last - now);
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

      this.dt = TimeUtil.getDeltaTimeNanos(currentTime);
      this.beginTime = System.nanoTime();
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
    this.beginTime = System.nanoTime();
  }
}
