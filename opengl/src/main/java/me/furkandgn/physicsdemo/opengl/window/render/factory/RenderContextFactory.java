package me.furkandgn.physicsdemo.opengl.window.render.factory;

import me.furkandgn.physicsdemo.opengl.window.component.sprite.SpriteComponent;
import me.furkandgn.physicsdemo.opengl.window.render.RenderContext;

/**
 * @author Furkan Doğan
 */
public interface RenderContextFactory {

  RenderContext createRenderContext(SpriteComponent spriteComponent, int index);
}
