import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glg2d.GLGraphics2D;
import org.lwjgl.glg2d.bridge.Lwjgl3GL2;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glg2d.bridge.GL20.GL_ARRAY_BUFFER;
import static org.lwjgl.glg2d.bridge.GL20.GL_FLOAT;
import static org.lwjgl.glg2d.bridge.GL20.GL_STATIC_DRAW;
import static org.lwjgl.glg2d.bridge.GL20.GL_TRIANGLES;
import static org.lwjgl.glg2d.bridge.GL20.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.system.MemoryStack.stackMallocFloat;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class HelloWorld {

  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;
  // The window handle
  private long window;
  private GLGraphics2D g;
  private Lwjgl3GL2 gl;

  private int logicalWidth;
  private int logicalHeight;


  private final IntBuffer tmpBuffer = BufferUtils.createIntBuffer(1);
  private final IntBuffer tmpBuffer2 = BufferUtils.createIntBuffer(1);


  private void run() {
    System.out.println("Hello LWJGL " + Version.getVersion() + "!");

    gl = new Lwjgl3GL2();

    g = new GLGraphics2D(gl, WIDTH, HEIGHT);

    g.setDefaultState();

    init();
    loop();

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);

    // Terminate GLFW and free the error callback
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default

    // glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);

    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
    glfwWindowHint(GLFW_SAMPLES, 4); // AA

    // Create the window
    window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
    if (window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
      }
    });

    // Get the thread stack and push a new frame
    try (MemoryStack stack = stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);

      // Get the resolution of the primary monitor
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center the window
      glfwSetWindowPos(
          window,
          (vidmode.width() - pWidth.get(0)) / 2,
          (vidmode.height() - pHeight.get(0)) / 2
      );
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
  }

  private void loop() {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

    g.active();

    // Set the clear color
    glClearColor(0.0f, 0f, 0f, 0.0f);

    Stroke stroke = new BasicStroke(3);

    double dx = 0.;
    double angle = 0.;

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window)) {

      GLFW.glfwGetFramebufferSize(window, tmpBuffer, tmpBuffer2);
      int backBufferWidth = tmpBuffer.get(0);
      int backBufferHeight = tmpBuffer2.get(0);

      // System.out.println(backBufferWidth + ", " + backBufferHeight);

      GLFW.glfwGetWindowSize(window, tmpBuffer, tmpBuffer2);
      logicalWidth = tmpBuffer.get(0);
      logicalHeight = tmpBuffer2.get(0);

      glViewport(0, 0, backBufferWidth, backBufferHeight);

      GL20.glMatrixMode(GL20.GL_PROJECTION);
      GL20.glLoadIdentity();
      GL20.glOrtho(0, logicalWidth, 0, logicalHeight, -1, 1);


      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer


      try (MemoryStack ignore = stackPush()) {
        FloatBuffer buffer = stackMallocFloat(3 * 2);
        buffer.put(-0.5f).put(-0.5f);
        buffer.put(+0.5f).put(-0.5f);
        buffer.put(+0.0f).put(+0.5f);
        buffer.flip();
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
      }

      glEnableClientState(GL_VERTEX_ARRAY);
      glVertexPointer(2, GL_FLOAT, 0, 0L);

      glDrawArrays(GL_TRIANGLES, 0, 3);


      drawRotatedRectangle(angle, 100, 100, 50, 50);
      angle += 1f / 60f;

      g.setColor(Color.CYAN);

      g.setStroke(stroke);
      // g.drawRect(100 + (int) dx, 100, 50, 50);
      g.draw(new Rectangle2D.Double(75 + dx, 75, 50, 50));
      dx += 1f / 60f * 8;
      // g.fillRect(100, 100, 50, 50);

      g.draw(new Ellipse2D.Double(200, 200, 500, 500));

      drawRotatedRectangle(angle * 2., 100 + dx, 100, 200, 200);

      glfwSwapBuffers(window); // swap the color buffers

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents();
    }
  }

  private void drawRotatedRectangle(double angle, double x, double y, double w, double h) {
    Rectangle2D r = new Rectangle2D.Double(-.5 * w, -.5 * h, w, h);
    Path2D.Double path = new Path2D.Double();
    path.append(r, false);

    AffineTransform t = new AffineTransform();
    t.translate(x, y);
    t.rotate(angle);
    path.transform(t);
    g.draw(path);
  }

  public static void main(String[] args) {
    new HelloWorld().run();
  }

}
