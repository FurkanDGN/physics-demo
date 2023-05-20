package me.furkandgn.physicsdemo.opengl.window.component.factory;

import earcut4j.Earcut;
import me.furkandgn.physicsdemo.common.Body;
import me.furkandgn.physicsdemo.common.Point;
import me.furkandgn.physicsdemo.opengl.window.util.ArrayUtil;
import me.furkandgn.physicsdemo.opengl.window.util.PointUtil;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public class PolygonIndicesFactory implements IndicesFactory {

  @Override
  public int[] createIndices(Body body) {
    List<Point> points = body.points();
    double[] pointsToDoubleArray = PointUtil.pointsToDoubleArray(points);
    List<Integer> indices = Earcut.earcut(pointsToDoubleArray, null, 2);
    return ArrayUtil.integerListToArray(indices);
  }
}
