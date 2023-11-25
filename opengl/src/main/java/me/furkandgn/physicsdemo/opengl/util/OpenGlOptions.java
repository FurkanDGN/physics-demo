package me.furkandgn.physicsdemo.opengl.util;

/**
 * @author Furkan Doğan
 */
public record OpenGlOptions(int width, int height, String title,
                            FocusCallback focusCallback) {

  public interface FocusCallback {
    void handle(boolean focused);
  }
}
