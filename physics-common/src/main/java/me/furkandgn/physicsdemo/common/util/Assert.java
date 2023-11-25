package me.furkandgn.physicsdemo.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author Burak Helvacı
 */
public abstract class Assert {

  public static void notNull(Object object, String message) {
    if (object == null) {
      throw new NullPointerException(message);
    }
  }

  public static void notBlank(String string, String message) {
    if (string == null || string.isBlank()) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notEmpty(Object[] array, String message) {
    if (array == null || array.length == 0) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notEmpty(Collection<?> collection, String message) {
    if (collection == null || collection.isEmpty()) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notEmpty(Map<?, ?> map, String message) {
    if (map == null || map.isEmpty()) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void isTrue(boolean condition, String message) {
    if (condition) {
      throw new RuntimeException(message);
    }
  }

  public static void notTrue(boolean condition, String message) {
    if (!condition) {
      throw new RuntimeException(message);
    }
  }

  public static <T> T requireNonNull(T object, String message) {
    if (object == null) {
      throw new NullPointerException(message);
    }

    return object;
  }

  public static String requireNonBlank(String string, String message) {
    if (string == null || string.isBlank()) {
      throw new IllegalArgumentException(message);
    }

    return string;
  }

  public static <T> T[] requireNonEmpty(T[] array, String message) {
    if (array == null || array.length == 0) {
      throw new IllegalArgumentException(message);
    }

    return array;
  }

  public static <T> Collection<T> requireNonEmpty(Collection<T> collection, String message) {
    if (collection == null || collection.isEmpty()) {
      throw new IllegalArgumentException(message);
    }

    return collection;
  }

  public static <K, V> Map<K, V> requireNonEmpty(Map<K, V> map, String message) {
    if (map == null || map.isEmpty()) {
      throw new IllegalArgumentException(message);
    }

    return map;
  }
}
