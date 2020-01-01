package org.jogamp.glg2d.gl3.backend;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;

public final class NewtGLApp implements GLApp {
  private GLWindow window;
  private GLWindowManager windowManager;

  @Override
  public void setup(GLConfig config, GLEventListener listener) {
    GLCapabilities caps = Backends.getGlCapabilities(config);

    window = GLWindow.create(caps);

    window.setTitle(config.title);
    window.setSize(config.width, config.height);

    window.addGLEventListener(new VSyncHelper());
    window.addGLEventListener(listener);

    window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DO_NOTHING_ON_CLOSE);
    windowManager = new GLWindowManager(window, false);
    window.addWindowListener(windowManager);

    window.addWindowListener(0, new WindowAdapter() {
      @Override
      public void windowResized(WindowEvent e) {
        windowManager.animatorManager.animator.pause();
        windowManager.scheduleResume();

        // window.setExclusiveContextThread(null);
        // windowManager.animatorManager.animator.pause();
        // windowManager.animatorManager.animator.setExclusiveContext(false);
        // new Thread(() -> {
        //   try {
        //     Thread.sleep(1000);
        //   } catch (InterruptedException ex) {
        //     Thread.currentThread().interrupt();
        //   }
        //   windowManager.animatorManager.animator.resume();
        // }).start();

        // System.out.println(e);
      }
    });

    window.setVisible(true);
  }

  @Override
  public void addKeyListener(KeyListener keyListener) {
    window.addKeyListener(keyListener);
  }

  @Override
  public void destroy() {
    window.destroy();
  }
}
