package me.furkandgn.physicsdemo.opengl.window.component.sprite;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.shapes.CircleBody;
import me.furkandgn.physicsdemo.common.body.shapes.RectBody;
import me.furkandgn.physicsdemo.common.body.attribute.Transform;
import me.furkandgn.physicsdemo.common.gui.GuiConstants;
import me.furkandgn.physicsdemo.opengl.Constants;
import me.furkandgn.physicsdemo.opengl.window.component.Component;
import me.furkandgn.physicsdemo.opengl.window.component.factory.IndicesFactory;
import me.furkandgn.physicsdemo.opengl.window.component.factory.VerticesFactory;
import org.joml.Vector2d;

/**
 * @author Furkan Doğan
 */
public class SpriteComponent extends Component {

  private volatile boolean dirty;
  private volatile boolean shouldDestroy = false;

  public SpriteComponent(Body body,
                         IndicesFactory indicesFactory,
                         VerticesFactory verticesFactory) {
    super(body, indicesFactory, verticesFactory);
  }

  @Override
  public void init() {
    Transform transform = this.body.transform();
    Vector2d position = transform.position();
    transform.lastPosition().set(position);
  }

  @Override
  public void update(double dt) {
    Transform transform = this.body.transform();
    Vector2d position = transform.position();
    boolean equals = transform.lastPosition().equals(position);
    if (!equals) {
      this.dirty = true;
    }

    if (position.y < -GuiConstants.HEIGHT) {
      this.shouldDestroy = true;
    }
  }

  public boolean dirty() {
    return this.dirty;
  }

  public boolean shouldDestroy() {
    return this.shouldDestroy;
  }

  public void clean() {
    this.dirty = false;
  }

  public int cornerCount() {
    if (this.body instanceof RectBody) {
      return 4;
    }
    if (this.body instanceof CircleBody) {
      return Constants.CIRCLE_CORNERS + 2;
    }

    return 0;
  }
}
