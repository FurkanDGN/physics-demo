package me.furkandgn.physicsdemo.opengl.util;

import java.nio.FloatBuffer;

/**
 * @author Furkan Doğan
 */
public class BufferUtils {

  public static FloatBuffer convert(float[][] floatArr, int capacity) {
    FloatBuffer floatBuffer = org.lwjgl.BufferUtils.createFloatBuffer(capacity);
    for (float[] floats : floatArr) {
      floatBuffer.put(floats);
    }
    floatBuffer.flip();
    return floatBuffer;
  }
}
