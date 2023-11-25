package me.furkandgn.freetype2;

import me.furkandgn.freetype2.FreeTypeConstants.FT_Size_Request_Type;

public class SizeRequest {

  private int type;
  private long width, height;
  private int horiResolution, vertResolution;

  public SizeRequest(FT_Size_Request_Type type, long width, long height, int horiResolution, int vertResolution) {
    this.type = type.ordinal();
    this.width = width;
    this.height = height;
    this.horiResolution = horiResolution;
    this.vertResolution = vertResolution;
  }

  public FT_Size_Request_Type getType() {
    return FT_Size_Request_Type.values()[this.type];
  }

  public void setType(FT_Size_Request_Type type) {
    this.type = type.ordinal();
  }

  public long getWidth() {
    return this.width;
  }

  public void setWidth(long width) {
    this.width = width;
  }

  public long getHeight() {
    return this.height;
  }

  public void setHeight(long height) {
    this.height = height;
  }

  public int getVertResolution() {
    return this.vertResolution;
  }

  public void setVertResolution(int vertResolution) {
    this.vertResolution = vertResolution;
  }

  public int getHoriResolution() {
    return this.horiResolution;
  }

  public void setHoriResolution(int horiResolution) {
    this.horiResolution = horiResolution;
  }
}