package me.furkandgn.physicsdemo.opengl.shader;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

  private final String vertexFile;
  private final String fragmentFile;

  private int programID, vertexID, fragmentID;
  private boolean beingUsed = false;

  public Shader() {
    this.vertexFile = "/vertex.glsl";
    this.fragmentFile = "/fragment.glsl";
  }

  public Shader(String vertexFile, String fragmentFile) {
    this.vertexFile = vertexFile;
    this.fragmentFile = fragmentFile;
  }

  public void create() {
    this.programID = glCreateProgram();

    this.vertexID = this.loadShader(GL_VERTEX_SHADER, this.vertexFile);
    this.fragmentID = this.loadShader(GL_FRAGMENT_SHADER, this.fragmentFile);

    glAttachShader(this.programID, this.vertexID);
    glAttachShader(this.programID, this.fragmentID);
    glLinkProgram(this.programID);
    glValidateProgram(this.programID);
  }

  public void stop() {
    glUseProgram(0);
  }

  public void delete() {
    this.stop();
    glDetachShader(this.programID, this.vertexID);
    glDetachShader(this.programID, this.fragmentID);
    glDeleteShader(this.vertexID);
    glDeleteShader(this.fragmentID);
    glDeleteProgram(this.programID);
  }

  public void use() {
    if (!this.beingUsed) {
      // Bind shader program
      glUseProgram(this.programID);
      this.beingUsed = true;
    }
  }

  public void detach() {
    glUseProgram(0);
    this.beingUsed = false;
  }

  public void uploadMat4f(String varName, Matrix4f mat4) {
    int varLocation = glGetUniformLocation(this.programID, varName);
    this.use();
    FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
    mat4.get(matBuffer);
    glUniformMatrix4fv(varLocation, false, matBuffer);
  }

  public void uploadMat3f(String varName, Matrix3f mat3) {
    int varLocation = glGetUniformLocation(this.programID, varName);
    this.use();
    FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
    mat3.get(matBuffer);
    glUniformMatrix3fv(varLocation, false, matBuffer);
  }

  public void uploadVec4f(String varName, Vector4f vec) {
    int varLocation = glGetUniformLocation(this.programID, varName);
    this.use();
    glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
  }

  public void uploadVec3f(String varName, Vector3f vec) {
    int varLocation = glGetUniformLocation(this.programID, varName);
    this.use();
    glUniform3f(varLocation, vec.x, vec.y, vec.z);
  }

  public void uploadVec2f(String varName, Vector2f vec) {
    int varLocation = glGetUniformLocation(this.programID, varName);
    this.use();
    glUniform2f(varLocation, vec.x, vec.y);
  }

  public void uploadFloat(String varName, float val) {
    int varLocation = glGetUniformLocation(this.programID, varName);
    this.use();
    glUniform1f(varLocation, val);
  }

  public void uploadInt(String varName, int val) {
    int varLocation = glGetUniformLocation(this.programID, varName);
    this.use();
    glUniform1i(varLocation, val);
  }

  public void uploadTexture(String varName, int slot) {
    int varLocation = glGetUniformLocation(this.programID, varName);
    this.use();
    glUniform1i(varLocation, slot);
  }

  private int loadShader(int type, String file) {
    int id = glCreateShader(type);
    glShaderSource(id, this.readFile(file));
    glCompileShader(id);

    if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
      System.out.println("Could Not Compile " + file);
      System.out.println(glGetShaderInfoLog(id));
    }

    return id;
  }

  private String readFile(String fileName) {
    InputStream resourceAsStream = Objects.requireNonNull(this.getClass().getResourceAsStream(fileName), "file");

    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
      StringBuilder stringBuilder = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line).append("\n");
      }

      return stringBuilder.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}