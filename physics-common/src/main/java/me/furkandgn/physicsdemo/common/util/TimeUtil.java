package me.furkandgn.physicsdemo.common.util;

public class TimeUtil {

  public static final double SECOND_TO_NANOS = 1.0e9;
  public static final double MILLISECOND_TO_NANOS = 1.0e6;

  public static long getCurrentTimeNanos() {
    return System.nanoTime();
  }

  public static long getDeltaTimeNanos(long startTimeNanos) {
    long currentTimeNanos = System.nanoTime();
    return currentTimeNanos - startTimeNanos;
  }
}