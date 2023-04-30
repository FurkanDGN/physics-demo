package me.furkandgn.physicsdemo.opengl.window.constants;

/**
 * @author Furkan Doğan
 */
public enum Shapes {

  CIRCLE(62),
  RECTANGLE(4);

  private final int dotCount;

  Shapes(int dotCount) {
    this.dotCount = dotCount;
  }

  public int getDotCount() {
    return this.dotCount;
  }
}
