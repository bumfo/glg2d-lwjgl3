package org.jogamp.glg2d.gl3.backend;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

final class GLDrawableHelper implements GLEventListener {
  @Override
  public void init(GLAutoDrawable drawable) {
    GL gl = drawable.getGL();
    gl.setSwapInterval(1);
    System.out.println("VSync: " + gl.getSwapInterval());
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
    System.out.println("dispose");
    System.exit(0);
  }

  @Override
  public void display(GLAutoDrawable drawable) {
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    // System.out.println("reshape " + width + ", " + height);
  }
}
