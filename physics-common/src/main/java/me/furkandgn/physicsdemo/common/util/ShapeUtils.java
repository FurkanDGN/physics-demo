package me.furkandgn.physicsdemo.common.util;

import me.furkandgn.physicsdemo.common.body.Transform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Furkan Doğan
 */
public class ShapeUtils {

  public static List<Point> createRectanglePoints(double width, double height) {
    List<Point> points = new ArrayList<>();

    double halfWidth = width / 2.0;
    double halfHeight = height / 2.0;

    points.add(Point.of(-halfWidth, -halfHeight)); // Left bottom
    points.add(Point.of(-halfWidth, halfHeight));  // Left Top
    points.add(Point.of(halfWidth, halfHeight));   // Right Top
    points.add(Point.of(halfWidth, -halfHeight));  // Right bottom

    return points;
  }

  public static List<Point> createCirclePoints(double radius, int pointCount) {
    List<Point> points = new ArrayList<>();

    points.add(Point.of(0, 0));
    double angleStep = 2 * Math.PI / pointCount;

    for (int i = 0; i < pointCount; i++) {
      double pointX = radius * Math.cos(i * angleStep);
      double pointY = radius * Math.sin(i * angleStep);
      points.add(Point.of(pointX, pointY));
    }

    return points;
  }

  public static List<Point> transformPoints(List<Point> points, Transform transform) {
    float rotation = transform.rotation();

    return points.stream()
      .map(point -> {
        double rotatedX = point.x() * Math.cos(rotation) - point.y() * Math.sin(rotation);
        double rotatedY = point.x() * Math.sin(rotation) + point.y() * Math.cos(rotation);

        return new Point((transform.position().x + rotatedX) * transform.scale().x, (transform.position().y + rotatedY) * transform.scale().y);
      })
      .toList();
  }
}
