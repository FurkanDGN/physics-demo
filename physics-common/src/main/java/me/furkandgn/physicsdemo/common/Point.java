package me.furkandgn.physicsdemo.common;

public record Point(float x, float y) {

  public static Point of(float x, float y) {
    return new Point(x, y);
  }
}