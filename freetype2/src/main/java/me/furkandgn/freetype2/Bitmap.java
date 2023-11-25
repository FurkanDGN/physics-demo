package me.furkandgn.freetype2;

import me.furkandgn.freetype2.Utils.Pointer;

import java.nio.ByteBuffer;

public class Bitmap extends Pointer {

  public Bitmap(long pointer) {
    super(pointer);
  }

  public int getWidth() {
    return FreeType.FT_Bitmap_Get_width(this.pointer);
  }

  public int getRows() {
    return FreeType.FT_Bitmap_Get_rows(this.pointer);
  }

  public int getPitch() {
    return FreeType.FT_Bitmap_Get_pitch(this.pointer);
  }

  public short getNumGrays() {
    return FreeType.FT_Bitmap_Get_num_grays(this.pointer);
  }

  public char getPaletteMode() {
    return FreeType.FT_Bitmap_Get_palette_mode(this.pointer);
  }

  public char getPixelMode() {
    return FreeType.FT_Bitmap_Get_pixel_mode(this.pointer);
  }

  public ByteBuffer getBuffer() {
    return FreeType.FT_Bitmap_Get_buffer(this.pointer);
  }
}