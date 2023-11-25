package me.furkandgn.physicsdemo.common.util;

public record Point(double x, double y) {

  public static Point of(double x, double y) {
    return new Point(x, y);
  }
}