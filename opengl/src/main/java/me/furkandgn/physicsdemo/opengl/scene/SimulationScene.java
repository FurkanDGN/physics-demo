package me.furkandgn.physicsdemo.opengl.scene;

import me.furkandgn.physicsdemo.common.gui.Scene;
import me.furkandgn.physicsdemo.common.gui.World;
import me.furkandgn.physicsdemo.common.util.MetricType;
import me.furkandgn.physicsdemo.opengl.font.domain.FontAtlasInfo;
import me.furkandgn.physicsdemo.opengl.font.load.FontLoader;
import me.furkandgn.physicsdemo.opengl.font.load.GlFontLoader;
import me.furkandgn.physicsdemo.opengl.font.render.FontRenderer;
import me.furkandgn.physicsdemo.opengl.window.OpenGlAppWindow;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.InputStream;
import java.util.Objects;

import static me.furkandgn.physicsdemo.common.constants.GuiConstants.HEIGHT;
import static me.furkandgn.physicsdemo.common.constants.GuiConstants.WIDTH;

/**
 * @author Furkan Doğan
 */
public class SimulationScene implements Scene {

  private final World world;

  private FontRenderer fontRenderer;

  public SimulationScene(World world) {
    this.world = world;
  }

  @Override
  public void init() {
    FontLoader fontLoader = new GlFontLoader(true);
    InputStream inputStream = Objects.requireNonNull(SimulationScene.class.getResourceAsStream("/fonts/OpenSans-Light.ttf"), "File");
    FontAtlasInfo fontAtlasInfo = fontLoader.load(inputStream, 27);
    Matrix4f projectionMatrix = new Matrix4f();
    projectionMatrix.identity();
    projectionMatrix.ortho(0, WIDTH, HEIGHT, 0, 0.0f, 1.0f);
    this.fontRenderer = new FontRenderer(fontAtlasInfo, projectionMatrix);
    this.fontRenderer.init();
  }

  @Override
  public void tick(double dt) {
    if (dt != 0) {
      this.world.tick(dt);
    }
  }

  @Override
  public void render(double dt) {
    int fps = OpenGlAppWindow.PERFORMANCE_TRACKER.getFPS();
    double frameTime = OpenGlAppWindow.PERFORMANCE_TRACKER.getAverage(MetricType.FRAME);
    double tickTime = OpenGlAppWindow.PERFORMANCE_TRACKER.getAverage(MetricType.TICK);
    double renderTime = OpenGlAppWindow.PERFORMANCE_TRACKER.getAverage(MetricType.RENDER);
    double preTime = OpenGlAppWindow.PERFORMANCE_TRACKER.getAverage(MetricType.PRE);
    double postTime = OpenGlAppWindow.PERFORMANCE_TRACKER.getAverage(MetricType.POST);
    String text = String.format("FPS: %d     frame time: %.2f     tick time: %.2f     render time: %.2f     pre time: %.2f   post time: %.2f",
      fps, frameTime, tickTime, renderTime, preTime, postTime);

    this.world.render();
    this.fontRenderer.render(text, 10, 30, 1.0f, new Vector3f(1f, 1f, 1f));
  }
}
