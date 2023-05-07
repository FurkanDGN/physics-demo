package me.furkandgn.physicsdemo.opengl.window.component.sprite;

import me.furkandgn.physicsdemo.common.Body;
import me.furkandgn.physicsdemo.common.body.attribute.Transform;
import me.furkandgn.physicsdemo.common.constants.GuiConstants;
import me.furkandgn.physicsdemo.opengl.window.component.Component;
import me.furkandgn.physicsdemo.opengl.window.component.factory.IndicesFactory;
import me.furkandgn.physicsdemo.opengl.window.component.factory.VerticesFactory;
import org.joml.Vector2d;

/**
 * @author Furkan Doğan
 */
public class SpriteComponent extends Component {

  private volatile boolean moved;

  public SpriteComponent(Body body,
                         IndicesFactory indicesFactory,
                         VerticesFactory verticesFactory,
                         int dotCount) {
    super(body, dotCount, indicesFactory, verticesFactory);
  }

  @Override
  public void init() {
  }

  @Override
  public void update(double dt) {
    this.updateMoved();
  }

  public boolean shouldUpdate() {
    return this.moved;
  }

  public boolean shouldDestroy() {
    return this.body.transform().position().y < -GuiConstants.HEIGHT;
  }

  private void updateMoved() {
    Transform transform = this.body.transform();
    Vector2d position = transform.position();
    this.moved = !transform.lastPosition().equals(position);
  }
}
