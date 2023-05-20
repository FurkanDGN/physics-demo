package me.furkandgn.physicsdemo.opengl.window.util;

import java.util.List;

/**
 * @author Furkan Doğan
 */
public class ArrayUtil {

  public static int[] integerListToArray(List<Integer> list) {
    int[] array = new int[list.size()];
    for (int i = 0; i < list.size(); i++) {
      array[i] = list.get(i);
    }
    return array;
  }

  public static float[] floatListToArray(List<Float> list) {
    float[] array = new float[list.size()];
    for (int i = 0; i < list.size(); i++) {
      array[i] = list.get(i);
    }
    return array;
  }

  public static float[] removeRange(float[] original, int from, int to) {
    float[] result = new float[original.length - (to - from)];
    System.arraycopy(original, 0, result, 0, from);
    System.arraycopy(original, to, result, from, original.length - to);
    return result;
  }
}
