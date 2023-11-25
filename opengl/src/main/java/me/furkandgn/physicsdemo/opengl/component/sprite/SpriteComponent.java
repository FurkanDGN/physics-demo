package me.furkandgn.physicsdemo.opengl.component.sprite;

import me.furkandgn.physicsdemo.common.body.Body;
import me.furkandgn.physicsdemo.common.body.Transform;
import me.furkandgn.physicsdemo.opengl.component.factory.VerticesFactory;
import me.furkandgn.physicsdemo.opengl.component.Component;
import me.furkandgn.physicsdemo.opengl.component.factory.IndicesFactory;
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
  public void update(double dt) {
    this.updateMoved();
  }

  public boolean shouldUpdate() {
    return this.moved;
  }

  public boolean shouldDestroy() {
    return this.body.transform().position().y < -this.body.height();
  }

  private void updateMoved() {
    Transform transform = this.body.transform();
    Vector2d position = transform.position();
    this.moved = !transform.lastPosition().equals(position);
  }
}
