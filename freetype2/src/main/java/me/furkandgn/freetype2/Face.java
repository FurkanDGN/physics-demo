package me.furkandgn.freetype2;

import me.furkandgn.freetype2.FreeTypeConstants.FT_Kerning_Mode;
import me.furkandgn.freetype2.Utils.Pointer;

import java.nio.ByteBuffer;

/**
 * A handle to a given typographic face object. A face object models a given typeface, in a given style.
 */
public class Face extends Pointer {

  private ByteBuffer data; // Save to delete later

  public Face(long pointer) {
    super(pointer);
  }

  public Face(long pointer, ByteBuffer data) {
    super(pointer);
    this.data = data;
  }

  public boolean delete() {
    if (this.data != null)
      Utils.deleteBuffer(this.data);
    return FreeType.FT_Done_Face(this.pointer);
  }

  public int getAscender() {
    return FreeType.FT_Face_Get_ascender(this.pointer);
  }

  public int getDescender() {
    return FreeType.FT_Face_Get_descender(this.pointer);
  }

  public long getFaceFlags() {
    return FreeType.FT_Face_Get_face_flags(this.pointer);
  }

  public int getFaceIndex() {
    return FreeType.FT_Face_Get_face_index(this.pointer);
  }

  public String getFamilyName() {
    return FreeType.FT_Face_Get_family_name(this.pointer);
  }

  public int getHeight() {
    return FreeType.FT_Face_Get_heigth(this.pointer);
  }

  public int getMaxAdvanceHeight() {
    return FreeType.FT_Face_Get_max_advance_height(this.pointer);
  }

  public int getMaxAdvanceWidth() {
    return FreeType.FT_Face_Get_max_advance_width(this.pointer);
  }

  public int getNumFaces() {
    return FreeType.FT_Face_Get_num_faces(this.pointer);
  }

  public int getNumGlyphs() {
    return FreeType.FT_Face_Get_num_glyphs(this.pointer);
  }

  public long getStyleFlags() {
    return FreeType.FT_Face_Get_style_flags(this.pointer);
  }

  public String getStyleName() {
    return FreeType.FT_Face_Get_style_name(this.pointer);
  }

  public int getUnderlinePosition() {
    return FreeType.FT_Face_Get_underline_position(this.pointer);
  }

  public int getUnderlineThickness() {
    return FreeType.FT_Face_Get_underline_thickness(this.pointer);
  }

  public int getUnitsPerEM() {
    return FreeType.FT_Face_Get_units_per_EM(this.pointer);
  }

  public int getCharIndex(int code) {
    return FreeType.FT_Get_Char_Index(this.pointer, code);
  }

  public boolean hasKerning() {
    return FreeType.FT_HAS_KERNING(this.pointer);
  }

  public boolean selectSize(int strikeIndex) {
    return FreeType.FT_Select_Size(this.pointer, strikeIndex);
  }

  public boolean setCharSize(int char_width, int char_height, int horz_resolution, int vert_resolution) {
    return FreeType.FT_Set_Char_Size(this.pointer, char_width, char_height, horz_resolution, vert_resolution);
  }

  public boolean loadGlyph(int glyphIndex, int flags) {
    return FreeType.FT_Load_Glyph(this.pointer, glyphIndex, flags);
  }

  public boolean loadChar(char c, int flags) {
    return FreeType.FT_Load_Char(this.pointer, c, flags);
  }

  public Kerning getKerning(char left, char right) {
    return this.getKerning(left, right, FT_Kerning_Mode.FT_KERNING_DEFAULT);
  }

  public Kerning getKerning(char left, char right, FT_Kerning_Mode mode) {
    return FreeType.FT_Face_Get_Kerning(this.pointer, left, right, mode.ordinal());
  }

  public boolean setPixelSizes(float width, float height) {
    return FreeType.FT_Set_Pixel_Sizes(this.pointer, width, height);
  }

  public GlyphSlot getGlyphSlot() {
    long glyph = FreeType.FT_Face_Get_glyph(this.pointer);
    if (glyph <= 0)
      return null;
    return new GlyphSlot(glyph);
  }

  public Size getSize() {
    long size = FreeType.FT_Face_Get_size(this.pointer);
    if (size <= 0)
      return null;
    return new Size(size);
  }

  public boolean checkTrueTypePatents() {
    return FreeType.FT_Face_CheckTrueTypePatents(this.pointer);
  }

  public boolean setUnpatentedHinting(boolean newValue) {
    return FreeType.FT_Face_SetUnpatentedHinting(this.pointer, newValue);
  }

  public boolean referenceFace() {
    return FreeType.FT_Reference_Face(this.pointer);
  }

  public boolean requestSize(SizeRequest sr) {
    return FreeType.FT_Request_Size(this.pointer, sr);
  }

  public int[] getFirstChar() {
    return FreeType.FT_Get_First_Char(this.pointer);
  }

  public int getFirstCharAsCharcode() {
    return this.getFirstChar()[0];
  }

  public int getFirstCharAsGlyphIndex() {
    return this.getFirstChar()[1];
  }

  public int[] getNextChar(long charcode) {
    return FreeType.FT_Get_Next_Char(this.pointer, charcode);
  }

  public int getGlyphIndexByName(String name) {
    return FreeType.FT_Get_Name_Index(this.pointer, name);
  }

  public long getTrackKerning(int point_size, int degree) {
    return FreeType.FT_Get_Track_Kerning(this.pointer, point_size, degree);
  }

  public String getGlyphName(int glyphIndex) {
    return FreeType.FT_Get_Glyph_Name(this.pointer, glyphIndex);
  }

  public String getPostscriptName() {
    return FreeType.FT_Get_Postscript_Name(this.pointer);
  }

  public boolean selectCharmap(int encoding) {
    return FreeType.FT_Select_Charmap(this.pointer, encoding);
  }

  public boolean setCharmap(CharMap charmap) {
    return FreeType.FT_Set_Charmap(this.pointer, charmap.getPointer());
  }

  public short getFSTypeFlags() {
    return FreeType.FT_Get_FSType_Flags(this.pointer);
  }
}