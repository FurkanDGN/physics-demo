package me.furkandgn.physicsdemo.opengl.scene;

import me.furkandgn.physicsdemo.common.gui.Scene;
import me.furkandgn.physicsdemo.common.gui.World;
import me.furkandgn.physicsdemo.common.util.MetricType;
import me.furkandgn.physicsdemo.common.util.PerformanceTracker;
import me.furkandgn.physicsdemo.opengl.camera.Camera;
import me.furkandgn.physicsdemo.opengl.font.domain.FontAtlasInfo;
import me.furkandgn.physicsdemo.opengl.font.load.FontLoader;
import me.furkandgn.physicsdemo.opengl.font.load.GlFontLoader;
import me.furkandgn.physicsdemo.opengl.font.render.FontRenderer;
import me.furkandgn.physicsdemo.opengl.window.OpenGlAppWindow;
import org.joml.Vector3f;

import java.io.InputStream;
import java.util.Objects;

/**
 * @author Furkan Doğan
 */
public class SimulationScene implements Scene {

  private final World world;
  private final Camera camera;

  private FontRenderer fontRenderer;

  public SimulationScene(World world, Camera camera) {
    this.world = world;
    this.camera = camera;
  }

  @Override
  public void init() {
    FontLoader fontLoader = new GlFontLoader(true);
    InputStream inputStream = Objects.requireNonNull(SimulationScene.class.getResourceAsStream("/fonts/OpenSans-Light.ttf"), "File");
    FontAtlasInfo fontAtlasInfo = fontLoader.load(inputStream, 22);
    this.fontRenderer = new FontRenderer(fontAtlasInfo, this.camera.projectionMatrix());
    this.fontRenderer.init();
  }

  @Override
  public void tick(double dt) {
    if (dt != 0) {
//      this.world.tick(dt);
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
    this.fontRenderer.render(text, 30, 30, 1f, new Vector3f(1f, 1f, 1f));
//    this.world.render();
//    for (int i = 0; i < 30; i++) {
      this.fontRenderer.render("asdasyY", 30, 60, 1f, new Vector3f(1f, 0.5f, 1f));
//    }
  }
}
