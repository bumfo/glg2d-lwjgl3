package org.jogamp.glg2d.gl3.backend;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;

public final class NewtGLApp implements GLApp {
  private GLWindow window;


  @Override
  public void setup(GLConfig config, GLEventListener listener) {
    GLCapabilities caps = Backends.getGlCapabilities(config);

    window = GLWindow.create(caps);

    window.setTitle(config.title);
    window.setSize(config.width, config.height);

    window.addGLEventListener(new VSyncHelper());
    window.addGLEventListener(listener);

    window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DO_NOTHING_ON_CLOSE);
    window.addWindowListener(new GLWindowManager(window, false));

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
