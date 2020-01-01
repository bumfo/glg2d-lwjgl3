package org.jogamp.glg2d.gl3.backend;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;

final class GLWindowManager extends WindowAdapter {
  private boolean destroyed = false;
  private GLWindow window;

  private AnimatorManager animatorManager;

  private final boolean isAWT;

  GLWindowManager(GLWindow window, boolean isAWT) {
    this.window = window;
    this.isAWT = isAWT;

    animatorManager = new AnimatorManager();
    animatorManager.init(window);
  }

  @Override
  public void windowDestroyNotify(WindowEvent e) {
    if (destroyed) return;
    destroyed = true;

    System.out.println("onDestroy");
    animatorManager.dispose(window);

    if (!isAWT) {
      window.setVisible(false, false);

      new Thread(() -> {
        window.destroy();
      }).start();
    }
  }
}
