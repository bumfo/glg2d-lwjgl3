package org.jogamp.glg2d.gl3.backend;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.Animator;

final class AnimatorManager {
  private Animator animator;
  private boolean disposed;

  void init(GLAutoDrawable drawable) {
    disposed = false;

    animator = new Animator(drawable);
    animator.start();
  }

  void dispose(GLAutoDrawable drawable) {
    if (disposed) return;
    disposed = true;

    animator.stop();
    animator.remove(drawable);
    animator = null;
  }
}
