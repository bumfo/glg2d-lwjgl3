package org.jogamp.glg2d.ben;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;

import java.awt.Color;

public class Main {
  Color color = Color.BLACK;

  public static void main(String[] args) {
    GLProfile glp = GLProfile.get("GL2");

    GLCapabilities caps = new GLCapabilities(glp);

    GLWindow glWindow = GLWindow.create(caps);
    glWindow.setSize(800, 600);
    // glWindow.setVisible(true);

    // final FPSAnimator animator = new FPSAnimator(glWindow, 60, true);
    final Animator animator = new Animator(glWindow);
    // animator.setRunAsFastAsPossible(true);

    glWindow.addWindowListener(new WindowAdapter() {
      public void windowDestroyNotify(WindowEvent e) {
        if (animator.isStarted()) {
          animator.stop();
        }
        System.exit(0);
      }
    });

    glWindow.addGLEventListener(new GLEventListener() {
      @Override
      public void init(GLAutoDrawable drawable) {
      }

      @Override
      public void dispose(GLAutoDrawable drawable) {
      }

      private double theta = 0.0f;  // rotational angle

      /**
       * Called back by the drawable to render OpenGL graphics
       */
      @Override
      public void display(GLAutoDrawable drawable) {
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

        update();
      }

      /**
       * Update the rotation angle after each frame refresh
       */
      private void update() {
        theta += 0.01;
      }

      @Override
      public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      }
    });
    glWindow.setVisible(true);
    animator.start();
  }
}