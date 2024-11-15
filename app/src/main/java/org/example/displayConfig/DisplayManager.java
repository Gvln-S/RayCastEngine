package org.example.displayConfig;

import org.example.renderEngine.Inputs;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class DisplayManager {
  private int displayWidth = 1024, displayHeight = 680;
  private String displayTitle = "RayCast2D";
  private long display;

  public int frames;
  public static long time;

  public Inputs inputs;

  public DisplayManager(int width, int height, String title) {
    this.displayWidth = width;
    this.displayHeight = height;
    this.displayTitle = title;
  }

  public void createDisplay() {
    if (!GLFW.glfwInit()) {
      System.err.println("Error: could not initiallize GLFW");
      System.exit(-1);
    }

    // object of class Inputs
    inputs = new Inputs();

    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

    // args4: fullscreen or other. args5: multiple monitors
    display = GLFW.glfwCreateWindow(displayWidth, displayHeight, displayTitle, 0, 0);

    // display is a long
    if (display == 0) {
      System.err.println("displaycould not be created");
      System.exit(-1);
    }

    // position of the window "where is the monitor
    // 1: get my monitor
    // 2: set position
    // 3: (set SwapInterval) at this moment all the render operations will be aplied to the window
    GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
    GLFW.glfwSetWindowPos(display, (vidMode.width() - displayWidth) / 2,  (vidMode.height() - displayHeight) / 2);
    GLFW.glfwMakeContextCurrent(display);


    // use the getter of the keyboard, mouseMove and mouseButtons
    GLFW.glfwSetKeyCallback(display, inputs.getKeyboardCallBack());
    GLFW.glfwSetCursorPosCallback(display, inputs.getMouseMoveCallBack());
    GLFW.glfwSetMouseButtonCallback(display, inputs.getMouseButtonsCallBack());

    // show the window it open and close at this point
    GLFW.glfwShowWindow(display);

    // frame rate: 1 = 60 fps
    GLFW.glfwSwapInterval(1);

    // get the time
    time = System.currentTimeMillis();

    // create OpenGl
    GL.createCapabilities();

    // draw cords 
    GL11.glViewport(0, 0, displayWidth, displayHeight);

    // 1. how to proyect cords
    // 2. new []
    // 3. change 0,0
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GL11.glOrtho(0, displayWidth, 0, displayHeight, -1, 1); // Invertir el eje Y para que (0,0) esté en la esquina inferior izquierda

  }

  // method to know when windowShouldClose for a loop
  public boolean closeDisplay() {
    return GLFW.glfwWindowShouldClose(display);
  }

  // to set on ESP close the window
  public void destroyFree() {
    inputs.destroyFree();
    GLFW.glfwWindowShouldClose(display);
    GLFW.glfwDestroyWindow(display);
    GLFW.glfwTerminate();
  }

  public void updateDisplay() {
    // color on screeen (0.0 to 1.0. GlCrear use GlCrearColor to set the color
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

    // interración user/window
    GLFW.glfwPollEvents();

    // start to add frames and if there is more tham 1000 millis print frames
    frames++;
    if (System.currentTimeMillis() > time + 1000) {
      GLFW.glfwSetWindowTitle(display, displayTitle + " | FPS: " + frames);
      time = System.currentTimeMillis();
      frames = 0;
    }
  }

  public void swapBuffers() {
    // el contenido del buffer oculto se intercambia con el buffer visible, imagen más suave
    GLFW.glfwSwapBuffers(display);
  }

  public long getDisplay() { return display; }
}
