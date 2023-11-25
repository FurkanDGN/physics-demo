package me.furkandgn.physicsdemo.opengl.font.domain;

import org.joml.Vector2f;

/**
 * @author Furkan Doğan
 */
public record FontCharacter(int textureId, Vector2f size, Vector2f bearing, int advance) {
}
