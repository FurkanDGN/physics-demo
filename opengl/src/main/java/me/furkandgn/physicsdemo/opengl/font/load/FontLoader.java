package me.furkandgn.physicsdemo.opengl.font.load;

import me.furkandgn.physicsdemo.opengl.font.domain.FontAtlasInfo;

import java.io.InputStream;

/**
 * @author Furkan Doğan
 */
public interface FontLoader {

  FontAtlasInfo load(InputStream inputStream, int fontSize);
}
