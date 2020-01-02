package org.jogamp.glg2d.gl3.backend;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;

public final class AwtCanvasGLApp implements GLApp {
  private GLCanvas canvas;
  private AnimatorManager animatorManager;

  private GLConfig config;
  private JFrame frame;

  @Override
  public void setup(GLConfig config, GLEventListener listener) {
    GLCapabilities caps = Backends.getGlCapabilities(config);

    this.config = config;

    canvas = new GLCanvas(caps);
    canvas.setPreferredSize(new Dimension(config.width, config.height));

    animatorManager = new AnimatorManager();

    canvas.addGLEventListener(new GLDrawableHelper());
    canvas.addGLEventListener(listener);
  }

  @Override
  public void addKeyListener(KeyListener keyListener) {
    new AWTKeyAdapter(keyListener, canvas).addTo(canvas);
  }

  @Override
  public void destroy() {
    if (frame == null) {
      throw new UnsupportedOperationException();
    }

    frame.dispose();
  }

  public GLCanvas getCanvas() {
    return canvas;
  }

  public void showAsJFrame() {
    if (frame != null) {
      frame.setVisible(true);
      return;
    }

    if (config == null) {
      throw new IllegalStateException();
    }

    frame = new JFrame(config.title);

    frame.add(canvas);

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setVisible(true);

    finishSetup();
  }

  public void showAsCanvas() {
    if (config == null) {
      throw new IllegalStateException();
    }

    finishSetup();
  }

  private void finishSetup() {
    config = null;
    animatorManager.init(canvas);
  }
}
