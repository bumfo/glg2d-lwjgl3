package org.jogamp.glg2d.ben;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.awt.TextRenderer2;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.lang.reflect.Method;

public final class TextTest {
  private void run() {
    JFrame frame = new JFrame("Hello Text");

    TextRenderer2 textRenderer = new TextRenderer2(new Font("Arial", Font.PLAIN, 16), true, false);
    TextRenderer2 textRenderer2 = new TextRenderer2(new Font("Arial", Font.PLAIN, 32), true, false);

    try {
      Class<?> util = Class.forName("com.apple.eawt.FullScreenUtilities");
      Method method = util.getMethod("setWindowCanFullScreen", Window.class, Boolean.TYPE);
      method.invoke(util, frame, true);
    } catch (Throwable ignore) {
    }

    frame.setBackground(Color.BLACK);
    GLWindow glWindow = GLWindow.create(new GLCapabilities(GLProfile.get("GL2")));
    int width = 800;
    int height = 600;
    glWindow.setSize(width, height);

    NewtCanvasAWT canvas = new NewtCanvasAWT(glWindow);
    frame.add(canvas);

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    Animator animator = new Animator(glWindow);

    glWindow.addGLEventListener(new GLEventListener() {
      private double theta = 0.0f;  // rotational angle

      @Override
      public void init(GLAutoDrawable drawable) {
        drawable.getGL().setSwapInterval(1);
        System.out.println("VSync: " + drawable.getGL().getSwapInterval());
      }

      @Override
      public void dispose(GLAutoDrawable drawable) {
        animator.stop();
        animator.remove(drawable);
        System.out.println("dispose");
      }


      @Override
      public void display(GLAutoDrawable drawable) {
        // System.out.println("display");
        GL2 gl = drawable.getGL().getGL2();   // get the OpenGL graphics context

        // gl.glClearColor(0, 1, 1, 0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);    // clear background


        // Rendering code - draw a triangle
        float sine = (float) Math.sin(theta);
        float cosine = (float) Math.cos(theta);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(1, 0, 0);
        gl.glVertex2d(100. + 10. * -cosine, 100. + 10. * -cosine);
        gl.glColor3f(0, 1, 0);
        gl.glVertex2d(100., 100. + 10. * cosine);
        gl.glColor3f(0, 0, 1);
        gl.glVertex2d(100. + 10. * sine, 100. + 10. * -sine);
        gl.glEnd();


        textRenderer.begin3DRendering();
        textRenderer.draw3D("Hello World", 100, 100 + 10 * cosine, 0, 1);
        textRenderer.end3DRendering();

        textRenderer2.begin3DRendering();
        textRenderer2.draw3D("Hello World", 100 + 100 * sine, 100 + 10 * cosine, 0, .5f);
        textRenderer2.end3DRendering();

        theta += 0.01;
      }

      @Override
      public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();   // get the OpenGL graphics context

        gl.glMatrixMode(gl.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 800, 0, 600, -1, 1);
      }
    });

    frame.setVisible(true);
    animator.start();

    frame.setSize(width + 1, height);
    frame.setSize(width, height);
  }

  public static void main(String[] args) {
    new TextTest().run();
  }
}
