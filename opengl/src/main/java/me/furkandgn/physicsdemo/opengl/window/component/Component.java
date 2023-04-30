package me.furkandgn.physicsdemo.opengl.window.component;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.opengl.window.component.factory.IndicesFactory;
import me.furkandgn.physicsdemo.opengl.window.component.factory.VerticesFactory;

/**
 * @author Furkan Doğan
 */
public abstract class Component {

  protected final Body body;
  private final int dotCount;
  protected final IndicesFactory indicesFactory;
  protected final VerticesFactory verticesFactory;

  public Component(Body body,
                   int dotCount,
                   IndicesFactory indicesFactory,
                   VerticesFactory verticesFactory) {
    this.body = body;
    this.dotCount = dotCount;
    this.indicesFactory = indicesFactory;
    this.verticesFactory = verticesFactory;
  }

  public void init() {
  }

  public abstract void update(double dt);

  public abstract boolean shouldDestroy();

  public abstract boolean shouldUpdate();

  public Body body() {
    return this.body;
  }

  public IndicesFactory indicesFactory() {
    return this.indicesFactory;
  }

  public VerticesFactory verticesFactory() {
    return this.verticesFactory;
  }

  public int dotCount() {
    return this.dotCount;
  }
}
