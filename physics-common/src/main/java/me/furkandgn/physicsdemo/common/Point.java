package me.furkandgn.physicsdemo.common;

public record Point(double x, double y) {

  public static Point of(double x, double y) {
    return new Point(x, y);
  }
}