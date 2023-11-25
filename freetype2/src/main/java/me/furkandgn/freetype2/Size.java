package me.furkandgn.freetype2;

import me.furkandgn.freetype2.Utils.Pointer;

public class Size extends Pointer {

  public Size(long pointer) {
    super(pointer);
  }

  public SizeMetrics getMetrics() {
    long sizeMetrics = FreeType.FT_Size_Get_metrics(this.pointer);
    if (sizeMetrics <= 0)
      return null;
    return new SizeMetrics(sizeMetrics);
  }
}