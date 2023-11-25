package me.furkandgn.physicsdemo.opengl.component.factory;

import earcut4j.Earcut;
import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.util.Point;
import me.furkandgn.physicsdemo.opengl.util.ArrayUtil;
import me.furkandgn.physicsdemo.opengl.util.PointUtil;

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
