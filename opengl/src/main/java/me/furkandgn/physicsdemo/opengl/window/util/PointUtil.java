package me.furkandgn.physicsdemo.opengl.window.util;

import me.furkandgn.physicsdemo.common.Point;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public class PointUtil {

  public static double[] pointsToDoubleArray(List<Point> points) {
    double[] arr = new double[points.size() * 2];
    for (int i = 0; i < points.size(); i++) {
      arr[i * 2] = points.get(i).x();
      arr[i * 2 + 1] = points.get(i).y();
    }

    return arr;
  }
}
