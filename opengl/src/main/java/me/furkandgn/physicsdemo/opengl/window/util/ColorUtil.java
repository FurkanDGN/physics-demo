package me.furkandgn.physicsdemo.opengl.window.util;

import org.joml.Vector4f;

import java.util.Random;

/**
 * @author Furkan Doğan
 */
public class ColorUtil {

  private static final Random secureRandom = new Random();

  public static Vector4f getColor(float red, float green, float blue) {
    return getColor(red, green, blue, 1.0f);
  }

  public static Vector4f getColor(float red, float green, float blue, float alpha) {
    return new Vector4f(red / 255f, green / 255f, blue / 255f, alpha);
  }

  public static Vector4f randomColor() {
    float r = secureRandom.nextInt(255);
    float g = secureRandom.nextInt(255);
    float b = secureRandom.nextInt(255);
    return new Vector4f(r / 255f, g / 255f, b / 255f, 1);
  }
}
