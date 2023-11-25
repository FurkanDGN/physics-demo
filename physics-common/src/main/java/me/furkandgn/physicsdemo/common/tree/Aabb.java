package me.furkandgn.physicsdemo.common.tree;

/**
 * @author Furkan Doğan
 */
public class Aabb {

  /**
   * The minimum x-coordinate of the bounding box.
   */
  private double x1;

  /**
   * The minimum y-coordinate of the bounding box.
   */
  private double y1;

  /**
   * The maximum x-coordinate of the bounding box.
   */
  private double x2;

  /**
   * The maximum y-coordinate of the bounding box.
   */
  private double y2;

  /**
   * Constructs an Axis-Aligned Bounding Box given the four coordinates.
   * @param x1 Minimum x-coordinate.
   * @param y1 Minimum y-coordinate.
   * @param x2 Maximum x-coordinate.
   * @param y2 Maximum y-coordinate.
   */
  public Aabb(double x1, double y1, double x2, double y2) {
    this.x1 = Math.min(x1, x2);
    this.y1 = Math.min(y1, y2);
    this.x2 = Math.max(x1, x2);
    this.y2 = Math.max(y1, y2);
  }

  /**
   * Determines if a point intersects with this bounding box.
   * @param x The x-coordinate of the point.
   * @param y The y-coordinate of the point.
   * @return True if the point is within the bounding box, false otherwise.
   */
  public boolean intersect(double x, double y) {
    return x >= this.x1
      && y >= this.y1
      && x <= this.x2
      && y <= this.y2;
  }

  /**
   * Expands this bounding box to include another bounding box.
   * @param aabb The other bounding box that this bounding box should now include.
   */
  public void expand(Aabb aabb) {
    this.x1 = Math.min(aabb.x1, this.x1);
    this.y1 = Math.min(aabb.y1, this.y1);
    this.x2 = Math.max(aabb.x2, this.x2);
    this.y2 = Math.max(aabb.y2, this.y2);
  }

  public double getCenterX() {
    return this.x1 + Math.abs(this.x2 - this.x1) / 2;
  }

  public double getCenterY() {
    return this.y1 + Math.abs(this.y2 - this.y1) / 2;
  }

  public double width() {
    return this.x2 - this.x1;
  }

  public double height() {
    return this.y2 - this.y1;
  }

  public void set(Aabb aabb) {
    this.x1 = aabb.x1;
    this.y1 = aabb.y1;
    this.x2 = aabb.x2;
    this.y2 = aabb.y2;
  }

  public double x1() {
    return this.x1;
  }

  public void x1(double x1) {
    this.x1 = x1;
  }

  public double y1() {
    return this.y1;
  }

  public void y1(double y1) {
    this.y1 = y1;
  }

  public double x2() {
    return this.x2;
  }

  public void x2(double x2) {
    this.x2 = x2;
  }

  public double y2() {
    return this.y2;
  }

  public void y2(double y2) {
    this.y2 = y2;
  }

  public Aabb copy() {
    return new Aabb(this.x1, this.y1, this.x2, this.y2);
  }
}
