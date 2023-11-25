package me.furkandgn.freetype2;

import me.furkandgn.freetype2.Utils.Pointer;

public class CharMap extends Pointer {

  public CharMap(long pointer) {
    super(pointer);
  }

  public static int getCharMapIndex(CharMap charmap) {
    return FreeType.FT_Get_Charmap_Index(charmap.getPointer());
  }
}