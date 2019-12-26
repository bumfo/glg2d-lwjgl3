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

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Color;

public final class BridgeTest {
  private void run() {
    JFrame frame = new JFrame("Hello Bridge");
    frame.setBackground(Color.BLACK);
    GLWindow glWindow = GLWindow.create(new GLCapabilities(GLProfile.get("GL2")));
    glWindow.setSize(800, 600);

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

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);    // clear background
        gl.glLoadIdentity();                   // reset the model-view matrix

        // Rendering code - draw a triangle
        float sine = (float) Math.sin(theta);
        float cosine = (float) Math.cos(theta);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(1, 0, 0);
        gl.glVertex2d(-cosine, -cosine);
        gl.glColor3f(0, 1, 0);
        gl.glVertex2d(0, cosine);
        gl.glColor3f(0, 0, 1);
        gl.glVertex2d(sine, -sine);
        gl.glEnd();

        theta += 0.01;
      }

      @Override
      public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      }
    });

    frame.setVisible(true);
    animator.start();
  }

  public static void main(String[] args) {
    new BridgeTest().run();
  }
}
