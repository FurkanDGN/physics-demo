package me.furkandgn.freetype2;

public class Kerning {

  private final int x, y;

  public Kerning(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getHorizontalKerning() {
    return this.x;
  }

  public int getVerticalKerning() {
    return this.y;
  }

  @Override
  public String toString() {
    return "Kerning(" + this.x + ", " + this.y + ")";
  }
}