package me.furkandgn.physicsdemo.opengl.font.domain;

import org.joml.Vector2i;

import java.util.Map;

/**
 * @author Furkan Doğan
 */
public record FontAtlasInfo(int textureId, Vector2i size, Map<Character, FontCharacter> characters) {
}
