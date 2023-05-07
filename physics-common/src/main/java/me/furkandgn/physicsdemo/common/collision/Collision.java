package me.furkandgn.physicsdemo.common.collision;

import me.furkandgn.physicsdemo.common.Body;
import org.joml.Vector2d;

public record Collision(Body bodyA, Body bodyB, Vector2d normal, double penetrationDepth) {
}