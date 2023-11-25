package me.furkandgn.physicsdemo.opengl.util;

import me.furkandgn.physicsdemo.opengl.Constants;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author Furkan Doğan
 */
public class OpenGlUtil {

  public static long initOpenGl(OpenGlOptions openGlOptions) {
    int width = openGlOptions.width();
    int height = openGlOptions.height();
    String title = openGlOptions.title();
    OpenGlOptions.FocusCallback focusCallback = openGlOptions.focusCallback();

    GLFWErrorCallback.createPrint(System.err).set();

    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW");

    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    glfwWindowHint(GLFW_SAMPLES, Constants.SAMPLING);

    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    long window = glfwCreateWindow(width, height, title, 0, 0);
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window");

    glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(w, true);
        System.exit(0);
      }
    });

    try (MemoryStack stack = stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1);
      IntBuffer pHeight = stack.mallocInt(1);

      glfwGetWindowSize(window, pWidth, pHeight);

      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
      if (vidmode != null) {
        glfwSetWindowPos(
          window,
          (vidmode.width() - pWidth.get(0)) / 2,
          (vidmode.height() - pHeight.get(0)) / 2
        );
      }
    }

    GLFWWindowFocusCallback.create((l, b) -> focusCallback.handle(b)).set(window);

    glfwMakeContextCurrent(window);
    GL.createCapabilities();
    glfwSwapInterval(0);

    return window;
  }
}
