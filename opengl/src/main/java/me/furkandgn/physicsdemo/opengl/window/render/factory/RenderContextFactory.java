package me.furkandgn.physicsdemo.opengl.window.render.factory;

import me.furkandgn.physicsdemo.opengl.window.component.Component;
import me.furkandgn.physicsdemo.opengl.window.render.RenderContext;

/**
 * @author Furkan Doğan
 */
public interface RenderContextFactory {

  RenderContext createRenderContext(Component component, int index);
}
