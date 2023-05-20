package me.furkandgn.physicsdemo.common.util;

import me.furkandgn.physicsdemo.common.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Furkan Doğan
 */
public class ShapeUtils {

  public static List<Point> createRectanglePoints(int width, int height) {
    List<Point> points = new ArrayList<>();

    int halfWidth = (int) Math.round(width / 2.0);
    int halfHeight = (int) Math.round(height / 2.0);

    points.add(Point.of(-halfWidth, -halfHeight));
    points.add(Point.of(halfWidth, -halfHeight));
    points.add(Point.of(halfWidth, halfHeight));
    points.add(Point.of(-halfWidth, halfHeight));

    return points;
  }

  public static List<Point> createCirclePoints(int radius, int pointCount) {
    List<Point> points = new ArrayList<>();

    points.add(Point.of(0, 0));
    double angleStep = 2 * Math.PI / pointCount;

    for (int i = 0; i < pointCount; i++) {
      float pointX = (float) (radius * Math.cos(i * angleStep));
      float pointY = (float) (radius * Math.sin(i * angleStep));
      points.add(Point.of(pointX, pointY));
    }

    return points;
  }
}
