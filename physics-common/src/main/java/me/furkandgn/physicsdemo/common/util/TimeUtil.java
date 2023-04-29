package me.furkandgn.physicsdemo.common.util;

public class TimeUtil {

  public static double getTime() {
    return (double) System.nanoTime();
  }

  public static double deltaTime(double time) {
    double now = System.nanoTime();
    return (now - time) / 1_000_000_000d;
  }
}