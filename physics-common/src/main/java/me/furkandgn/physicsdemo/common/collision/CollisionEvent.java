package me.furkandgn.physicsdemo.common.collision;

import me.furkandgn.physicsdemo.common.body.Body;

import java.util.Objects;

/**
 * @author Furkan Doğan
 */
public record CollisionEvent(Body body1, Body body2) {

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    CollisionEvent that = (CollisionEvent) o;
    int hashCode1 = this.body1.uniqueId().hashCode();
    int hashCode2 = this.body2.uniqueId().hashCode();

    int hashCode1That = that.body1.uniqueId().hashCode();
    int hashCode2That = that.body2.uniqueId().hashCode();

    return Math.min(hashCode1, hashCode2) == Math.min(hashCode1That, hashCode2That) &&
      Math.max(hashCode1, hashCode2) == Math.max(hashCode1That, hashCode2That);
  }

  @Override
  public int hashCode() {
    int hashCode1 = this.body1.uniqueId().hashCode();
    int hashCode2 = this.body2.uniqueId().hashCode();

    return Objects.hash(Math.min(hashCode1, hashCode2), Math.max(hashCode1, hashCode2));
  }
}
