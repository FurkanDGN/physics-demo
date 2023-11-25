package me.furkandgn.physicsdemo.opengl.render;

/**
 * @author Furkan Doğan
 */
public class RenderContext {

  private volatile int vaoId;
  private volatile int vboId;
  private volatile int eboId;
  private volatile int[] indices;
  private volatile float[] vertices;

  public RenderContext() {
  }

  public int vaoId() {
    return this.vaoId;
  }

  public void vaoId(int vaoId) {
    this.vaoId = vaoId;
  }

  public int vboId() {
    return this.vboId;
  }

  public void vboId(int vboId) {
    this.vboId = vboId;
  }

  public int eboId() {
    return this.eboId;
  }

  public void eboId(int edoId) {
    this.eboId = edoId;
  }

  public int[] indices() {
    return this.indices;
  }

  public void indices(int[] indices) {
    this.indices = indices;
  }

  public float[] vertices() {
    return this.vertices;
  }

  public void vertices(float[] vertices) {
    this.vertices = vertices;
  }
}
