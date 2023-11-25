package me.furkandgn.physicsdemo.opengl.font.load;

import me.furkandgn.physicsdemo.opengl.font.domain.FontCharacter;

import java.io.InputStream;
import java.util.Map;

/**
 * @author Furkan Doğan
 */
public interface FontLoader {

  Map<Character, FontCharacter> load(InputStream inputStream, int fontSize);
}
