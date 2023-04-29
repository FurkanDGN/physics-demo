package me.furkandgn.physicsdemo.opengl.component;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.opengl.component.factory.IndicesFactory;
import me.furkandgn.physicsdemo.opengl.component.factory.VerticesFactory;

/**
 * @author Furkan Doğan
 */
public abstract class Component {

  protected final Body body;
  protected final IndicesFactory indicesFactory;
  protected final VerticesFactory verticesFactory;

  public Component(Body body,
                   IndicesFactory indicesFactory,
                   VerticesFactory verticesFactory) {
    this.body = body;
    this.indicesFactory = indicesFactory;
    this.verticesFactory = verticesFactory;
  }

  public void init() {
  }

  public abstract void update(double dt);

  public Body body() {
    return this.body;
  }

  public IndicesFactory indicesFactory() {
    return this.indicesFactory;
  }

  public VerticesFactory verticesFactory() {
    return this.verticesFactory;
  }
}
