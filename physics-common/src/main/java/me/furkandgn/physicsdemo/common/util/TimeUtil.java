package me.furkandgn.physicsdemo.common.util;

public class TimeUtil {

  public static final double SECOND_TO_NANO = 1_000_000_000d;

  public static long getTime() {
    return System.nanoTime();
  }

  public static long deltaTime(long time) {
    long now = System.nanoTime();
    return now - time;
  }
}