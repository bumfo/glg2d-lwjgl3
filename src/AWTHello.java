import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;
import org.lwjgl.glg2d.GLGraphics2D;
import org.lwjgl.glg2d.GLUtils;
import org.lwjgl.glg2d.bridge.Lwjgl3GL2;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;
import org.lwjgl.opengl.awt.MyPlatformMacOSXGLCanvas;

import javax.swing.JFrame;
import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

public final class AWTHello {
  private static final int InitialWidth = 800;
  private static final int InitialHeight = 600;
  private static final boolean AWT_ONLY = false;

  private float scaleX = 1f;
  private float scaleY = 1f;

  private int logicalWidth = 800;
  private int logicalHeight = 600;

  private GLGraphics2D g;
  private Lwjgl3GL2 gl;

  private boolean shown;
  private boolean running = false;

  private double angle;
  private SVGDiagram diagram;

  private void run() {
    Frame frame = new JFrame("AWT test");
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    GLData data = new GLData();
    // data.majorVersion = 2;
    // data.minorVersion = 0;
    data.swapInterval = 1;
    data.profile = GLData.Profile.COMPATIBILITY;
    data.samples = 4;


    SVGUniverse universe = new SVGUniverse();
    URI svg = universe.loadSVG(getURL("robot.svg"));
    diagram = universe.getDiagram(svg);
    diagram.setIgnoringClipHeuristic(true);

    // frame.add(new Component() {
    //   @Override
    //   public Dimension getPreferredSize() {
    //     return new Dimension(800, 600);
    //   }
    //
    //   @Override
    //   public void paint(Graphics g0) {
    //     Graphics2D g = (Graphics2D) g0;
    //     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    //         RenderingHints.VALUE_ANTIALIAS_ON);
    //
    //
    //     drawRotatedRectangle(g, angle, 100, 100, 50, 50);
    //     angle += 1f / 60f;
    //
    //
    //     try {
    //       diagram.render(g);
    //     } catch (SVGException e) {
    //       e.printStackTrace();
    //     }
    //   }
    // });

    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        running = false;
        frame.dispose();
        // System.exit(0);
      }
    });

    if (AWT_ONLY) {
      // Component panel = frame.add(new Component() {
      // });
      // panel.setPreferredSize(new Dimension(800, 600));
      frame.setPreferredSize(new Dimension(800, 600));

      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);

      BufferCapabilities caps = GraphicsEnvironment.getLocalGraphicsEnvironment()
          .getDefaultScreenDevice()
          .getDefaultConfiguration()
          .getBufferCapabilities();

      try {
        frame.createBufferStrategy(2, caps);
      } catch (AWTException e) {
        throw new RuntimeException(e);
      }
      BufferStrategy strategy = frame.getBufferStrategy();
      running = true;

      Runnable loop = new Runnable() {
        public void run() {
          if (!running) return;

          do {

            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
            g.setClip(0, 0, frame.getWidth(), frame.getHeight());

            drawRotatedRectangle(g, angle, 400, 300, 50, 50);

            AffineTransform af = new AffineTransform();
            af.translate(100, 100);
            af.rotate(angle, 100, 100);
            g.setTransform(af);

            try {
              diagram.render(g);
              //
              // af.translate(100, 100);
              // g.setTransform(af);
              //
              // diagram.render(g);
            } catch (SVGException e) {
              e.printStackTrace();
            }


            // g.setColor(Color.GRAY);
            // g.drawRect(0, 0, 500, 500);
            // g.setColor(Color.BLACK);
            // g.drawLine(50, 50, 200, 50);

            // Dispose the graphics
            g.dispose();

          } while (running && strategy.contentsRestored());


          angle += 1f / 60f;

          // Display the buffer
          strategy.show();

          EventQueue.invokeLater(this);
        }
      };
      EventQueue.invokeLater(loop);

      // new Thread(() -> {
      //   while (running) {
      //     // Prepare for rendering the next frame
      //     // ...
      //
      //     // Render single frame
      //     do {
      //       // The following loop ensures that the contents of the drawing buffer
      //       // are consistent in case the underlying surface was recreated
      //       do {
      //         // Get a new graphics context every time through the loop
      //         // to make sure the strategy is validated
      //         Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
      //
      //         g.clearRect(0, 0, panel.getWidth(), panel.getHeight());
      //
      //         g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      //             RenderingHints.VALUE_ANTIALIAS_ON);
      //
      //         drawRotatedRectangle(g, angle, 100, 100, 50, 50);
      //         angle += 1f / 60f;
      //
      //         try {
      //           diagram.render(g);
      //         } catch (SVGException e) {
      //           e.printStackTrace();
      //         }
      //
      //
      //         // g.setColor(Color.GRAY);
      //         // g.drawRect(0, 0, 500, 500);
      //         // g.setColor(Color.BLACK);
      //         // g.drawLine(50, 50, 200, 50);
      //
      //         // Dispose the graphics
      //         g.dispose();
      //
      //         // Repeat the rendering if the drawing buffer contents
      //         // were restored
      //       } while (running && strategy.contentsRestored());
      //
      //       // Display the buffer
      //       strategy.show();
      //
      //       // Repeat the rendering if the drawing buffer was lost
      //     } while (running && strategy.contentsLost());
      //   }
      // }).start();

      return;
    }

    MyAWTGLCanvas canvas = new MyAWTGLCanvas(data);

    canvas.setPreferredSize(new Dimension(InitialWidth, InitialHeight));
    // canvas.setPreferredSize(new Dimension(WIDTH + 1, HEIGHT + 1));
    frame.add(canvas);


    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        // System.out.println("windowOpened " + Thread.currentThread().getId());

        canvas.render();
      }

      // @Override
      // public void windowStateChanged(WindowEvent e) {
      //   System.out.println(e);
      //   canvas.reset();
      // }
    });

    frame.addComponentListener(new ComponentAdapter() {

      @Override
      public void componentResized(ComponentEvent e) {
        canvas.render();
      }

      @Override
      public void componentShown(ComponentEvent e) {
        System.out.println("componentShown " + Thread.currentThread().getId());

        canvas.render();
      }

      @Override
      public void componentHidden(ComponentEvent e) {
        shown = false;
      }
    });

    frame.setBackground(Color.BLACK);

    frame.pack();
    frame.setLocationRelativeTo(null);

    try {
      EventQueue.invokeAndWait(() -> {
        frame.setVisible(true);
      });
    } catch (InterruptedException | InvocationTargetException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    frame.transferFocus();
    // canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    // frame.pack();
    // frame.setLocationRelativeTo(null);

    System.out.println("main " + Thread.currentThread().getId());

    Runnable renderLoop = new Runnable() {
      public void run() {
        if (!canvas.isValid()) {
          return;
        }
        canvas.render();
        EventQueue.invokeLater(this);
      }
    };
    EventQueue.invokeLater(renderLoop);

    // while (true) {
    //   canvas.render();
    // }
  }

  private static void drawRotatedRectangle(Graphics2D g, double angle, double x, double y, double w, double h) {
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

  private final class MyAWTGLCanvas extends AWTGLCanvas {

    double dx;
    double angle;
    Stroke stroke;

    public MyAWTGLCanvas(GLData data) {
      super(data);

      platformCanvas = new MyPlatformMacOSXGLCanvas();

      float[] scale = GLUtils.getScale(new float[2]);
      scaleX = scale[0];
      scaleY = scale[1];

      MyPlatformMacOSXGLCanvas.scaleX = scaleX;
      MyPlatformMacOSXGLCanvas.scaleY = scaleY;

      // frame.addKeyListener(new KeyAdapter() {
      //   @Override
      //   public void keyPressed(KeyEvent e) {
      //     context = 0;
      //     initCalled = false;
      //   }
      // });

      addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
          // context = 0;
          // render();
        }

        @Override
        public void componentShown(ComponentEvent e) {
          // render();
        }
      });
      dx = 0.;
      angle = 0.;
      stroke = new BasicStroke(3);
    }

    public void initGL() {
      System.out.println("initGL");
      GL.createCapabilities();

      gl = new Lwjgl3GL2();
      g = new GLGraphics2D(gl, WIDTH, HEIGHT);

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

      logicalWidth = getWidth();
      logicalHeight = getHeight();
      // System.out.println(logicalWidth + ":" + logicalHeight);

      int surfaceWidth = (int) (logicalWidth * scaleX + 0.5);
      int surfaceHeight = (int) (logicalHeight * scaleY + 0.5);
      glViewport(0, 0, surfaceWidth, surfaceHeight);

      g.setSize(logicalWidth, logicalHeight, surfaceWidth, surfaceHeight);
      g.active();
      g.setDefaultState();
      g.setClip(0, 0, logicalWidth, logicalHeight);

      // glViewport(0, 0, logicalWidth, logicalHeight);

      // GL20.glMatrixMode(GL20.GL_PROJECTION);
      // GL20.glLoadIdentity();
      // GL20.glOrtho(0, logicalWidth, 0, logicalHeight, -1, 1);


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


      drawRotatedRectangle(g, angle, 100, 100, 50, 50);
      angle += 1f / 60f;


      // g.setColor(Color.WHITE);
      g.setStroke(stroke);
      // g.drawRect(100 + (int) dx, 100, 50, 50);
      g.draw(new Rectangle2D.Double(75 + dx, 75, 50, 50));
      dx += 1f / 60f * 8;
      // g.fillRect(100, 100, 50, 50);

      g.draw(new Ellipse2D.Double(200, 200, 500, 500));

      drawRotatedRectangle(g, angle * 2., 100 + dx, 100, 200, 200);

      drawSVG(g);

      // g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
      // g.drawString("Hello OpenGL", 100, 100);

      swapBuffers();
    }

    public void reset() {
      context = 0;
    }
  }

  private void drawSVG(GLGraphics2D g) {
    try {
      diagram.render(g);
    } catch (SVGException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns an image resource.
   *
   * @param filename the filename of the image to load
   * @return the loaded image
   */
  public static URL getURL(String filename) {
    URL url = AWTHello.class.getClassLoader().getResource(filename);

    if (url == null) {
      throw new RuntimeException("Could not load image because of invalid filename: " + filename);
    }

    return url;
  }
}
