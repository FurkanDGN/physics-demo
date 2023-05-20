package me.furkandgn.physicsdemo.opengl;

/**
 * @author Furkan Doğan
 */
public class Constants {

  public static final int FPS_LIMIT = 60;
  public static final int FPS_TIME_OFFSET = 7500;

  public static final int SAMPLING = 8;

  public static final int MAX_BATCH_SIZE = 10;

  public static final int POS_SIZE = 2;
  public static final int COLOR_SIZE = 4;
  public static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;
  public static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

  public static final int POS_OFFSET = 0;
  public static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
}
