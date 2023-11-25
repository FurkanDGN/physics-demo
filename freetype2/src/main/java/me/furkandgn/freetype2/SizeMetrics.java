package me.furkandgn.freetype2;

import me.furkandgn.freetype2.Utils.Pointer;

public class SizeMetrics extends Pointer {

  public SizeMetrics(long pointer) {
    super(pointer);
  }

  public int getAscender() {
    return FreeType.FT_Size_Metrics_Get_ascender(this.pointer);
  }

  public int getDescender() {
    return FreeType.FT_Size_Metrics_Get_descender(this.pointer);
  }

  public int getHeight() {
    return FreeType.FT_Size_Metrics_Get_height(this.pointer);
  }

  public int getMaxAdvance() {
    return FreeType.FT_Size_Metrics_Get_max_advance(this.pointer);
  }

  public int getXppem() {
    return FreeType.FT_Size_Metrics_Get_x_ppem(this.pointer);
  }

  public int getYppem() {
    return FreeType.FT_Size_Metrics_Get_y_ppem(this.pointer);
  }

  public int getXScale() {
    return FreeType.FT_Size_Metrics_Get_x_scale(this.pointer);
  }

  public int getYScale() {
    return FreeType.FT_Size_Metrics_Get_y_scale(this.pointer);
  }
}