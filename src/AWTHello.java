import org.lwjgl.glfw.GLFW;
import org.lwjgl.glg2d.GLGraphics2D;
import org.lwjgl.glg2d.Lwjgl3GL20;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;
import org.lwjgl.opengl.awt.MyPlatformMacOSXGLCanvas;
import org.lwjgl.system.MemoryStack;

import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;

import static org.lwjgl.glg2d.bridge.GL20.GL_ARRAY_BUFFER;
import static org.lwjgl.glg2d.bridge.GL20.GL_FLOAT;
import static org.lwjgl.glg2d.bridge.GL20.GL_STATIC_DRAW;
import static org.lwjgl.glg2d.bridge.GL20.GL_TRIANGLES;
import static org.lwjgl.glg2d.bridge.GL20.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.system.MemoryStack.stackMallocFloat;
import static org.lwjgl.system.MemoryStack.stackPush;

public final class AWTHello {
  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;


  private int logicalWidth = 800;
  private int logicalHeight = 600;

  private GLGraphics2D g;
  private Lwjgl3GL20 gl;

  private void run() {
    JFrame frame = new JFrame("AWT test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    GLData data = new GLData();
    // data.majorVersion = 2;
    // data.minorVersion = 0;
    data.swapInterval = 1;
    data.profile = GLData.Profile.COMPATIBILITY;
    data.samples = 4;

    AWTGLCanvas canvas = new AWTGLCanvas(data) {
      {
        platformCanvas = new MyPlatformMacOSXGLCanvas();
      }

      double dx = 0.;
      double angle = 0.;
      Stroke stroke = new BasicStroke(3);

      public void initGL() {
        System.out.println("initGL");

        gl = new Lwjgl3GL20();
        g = new GLGraphics2D(gl, WIDTH, HEIGHT);
        g.setDefaultState();

        GL.createCapabilities();

        g.active();

        // Set the clear color
        // glClearColor(0.0f, 0f, 0f, 0.0f);
        //
        // Stroke stroke = new BasicStroke(3);
        //
        // double dx = 0.;
        // double angle = 0.;
        //
        // GLFW.glfwGetFramebufferSize(window, tmpBuffer, tmpBuffer2);
        // int backBufferWidth = tmpBuffer.get(0);
        // int backBufferHeight = tmpBuffer2.get(0);
        //
        // GLFW.glfwGetWindowSize(window, tmpBuffer, tmpBuffer2);
        // logicalWidth = tmpBuffer.get(0);
        // logicalHeight = tmpBuffer2.get(0);
        //
        // glViewport(0, 0, backBufferWidth, backBufferHeight);
        //
        // GL20.glMatrixMode(GL20.GL_PROJECTION);
        // GL20.glLoadIdentity();
        // GL20.glOrtho(0, logicalWidth, 0, logicalHeight, -1, 1);
      }

      public void paintGL() {
        // System.out.println("paintGL");

        // logicalWidth = tmpBuffer.get(0);
        // logicalHeight = tmpBuffer2.get(0);

        // int backBufferWidth = 1600;
        // int backBufferHeight = 1200;
        //
        // glViewport(0, 0, backBufferWidth, backBufferHeight);
        // glViewport(0, 0, 400, 300);
        // glViewport(0, 0, WIDTH, HEIGHT);
        // glViewport(0, 0, 800, 600);

        GL20.glMatrixMode(GL20.GL_PROJECTION);
        GL20.glLoadIdentity();
        GL20.glOrtho(0, logicalWidth, 0, logicalHeight, -1, 1);


        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer


        // try (MemoryStack ignore = stackPush()) {
        //   FloatBuffer buffer = stackMallocFloat(3 * 2);
        //   buffer.put(-0.5f).put(-0.5f);
        //   buffer.put(+0.5f).put(-0.5f);
        //   buffer.put(+0.0f).put(+0.5f);
        //   buffer.flip();
        //   int vbo = GL15C.glGenBuffers();
        //   GL15C.glBindBuffer(GL_ARRAY_BUFFER, vbo);
        //   GL15C.glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        // }
        //
        // glEnableClientState(GL_VERTEX_ARRAY);
        // glVertexPointer(2, GL_FLOAT, 0, 0L);
        //
        // GL11C.glDrawArrays(GL_TRIANGLES, 0, 3);


        drawRotatedRectangle(angle, 100, 100, 50, 50);
        angle += 1f / 60f;

        // g.setColor(Color.WHITE);
        g.setStroke(stroke);
        // g.drawRect(100 + (int) dx, 100, 50, 50);
        g.draw(new Rectangle2D.Double(75 + dx, 75, 50, 50));
        dx += 1f / 60f * 8;
        // g.fillRect(100, 100, 50, 50);

        g.draw(new Ellipse2D.Double(200, 200, 500, 500));

        drawRotatedRectangle(angle * 2., 100 + dx, 100, 200, 200);

        swapBuffers();
      }
    };

    canvas.setPreferredSize(new Dimension(WIDTH+1, HEIGHT+1));
    frame.add(canvas);

    // frame.setBackground(Color.BLACK);

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    frame.pack();
    // frame.setLocationRelativeTo(null);

    while (true) {
      canvas.render();
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
    new AWTHello().run();
  }
}
