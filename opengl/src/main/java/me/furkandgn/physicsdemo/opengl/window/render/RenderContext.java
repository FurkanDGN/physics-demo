package me.furkandgn.physicsdemo.opengl.window.render;

import me.furkandgn.physicsdemo.common.body.Body;

/**
 * @author Furkan Doğan
 */
public class RenderContext {

  private final Class<? extends Body> clazz;
  private int vaoId;
  private int vboId;
  private int eboId;
  private int[] indices;
  private float[] vertices;
  private boolean refreshBufferData = true;

  public RenderContext(Class<? extends Body> clazz) {
    this.clazz = clazz;
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

  public boolean refreshBufferData() {
    return this.refreshBufferData;
  }

  public void refreshBufferData(boolean refreshBufferData) {
    this.refreshBufferData = refreshBufferData;
  }

  public Class<? extends Body> clazz() {
    return this.clazz;
  }
}
