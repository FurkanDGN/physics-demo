package me.furkandgn.freetype2;

import me.furkandgn.freetype2.FreeTypeConstants.FT_Render_Mode;
import me.furkandgn.freetype2.Utils.Pointer;

public class GlyphSlot extends Pointer {

  public GlyphSlot(long pointer) {
    super(pointer);
  }

  public Bitmap getBitmap() {
    long bitmap = FreeType.FT_GlyphSlot_Get_bitmap(this.pointer);
    if (bitmap <= 0)
      return null;
    return new Bitmap(bitmap);
  }

  public long getLinearHoriAdvance() {
    return FreeType.FT_GlyphSlot_Get_linearHoriAdvance(this.pointer);
  }

  public long getLinearVertAdvance() {
    return FreeType.FT_GlyphSlot_Get_linearVertAdvance(this.pointer);
  }

  public Advance getAdvance() {
    return FreeType.FT_GlyphSlot_Get_advance(this.pointer);
  }

  public int getFormat() {
    return FreeType.FT_GlyphSlot_Get_format(this.pointer);
  }

  public int getBitmapLeft() {
    return FreeType.FT_GlyphSlot_Get_bitmap_left(this.pointer);
  }

  public int getBitmapTop() {
    return FreeType.FT_GlyphSlot_Get_bitmap_top(this.pointer);
  }

  public GlyphMetrics getMetrics() {
    long metrics = FreeType.FT_GlyphSlot_Get_metrics(this.pointer);
    if (metrics <= 0)
      return null;
    return new GlyphMetrics(metrics);
  }

  public boolean renderGlyph(FT_Render_Mode renderMode) {
    return FreeType.FT_Render_Glyph(this.pointer, renderMode.ordinal());
  }

  public static class Advance {

    private final int x, y;

    public Advance(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return this.x;
    }

    public int getY() {
      return this.y;
    }

    @Override
    public String toString() {
      return "(" + this.x + "," + this.y + ")";
    }
  }
}