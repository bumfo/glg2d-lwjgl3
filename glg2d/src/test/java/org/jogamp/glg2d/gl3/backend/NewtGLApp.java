package org.jogamp.glg2d.gl3.backend;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;

public final class NewtGLApp implements GLApp {
  private GLWindow window;
  private Animator animator;

  @Override
  public void setup(GLConfig config, GLEventListener listener) {
    GLCapabilities caps = config.glCapabilities;
    if (caps == null) {
      GLProfile glProfile = GLProfile.get(GLProfile.GL3);
      caps = new GLCapabilities(glProfile);
      caps.setRedBits(8);
      caps.setGreenBits(8);
      caps.setBlueBits(8);
      caps.setAlphaBits(8);
      caps.setNumSamples(4);
      caps.setSampleBuffers(true);
      caps.setDoubleBuffered(true);
      caps.setHardwareAccelerated(true);
      caps.setBackgroundOpaque(false);
    }

    window = GLWindow.create(caps);

    window.setTitle(config.title);
    window.setSize(config.width, config.height);

    window.setVisible(true);

    window.addGLEventListener(listener);

    animator = new Animator(window);
    animator.start();

    window.addWindowListener(new WindowAdapter() {
      @Override
      public void windowDestroyNotify(WindowEvent e) {
        animator.stop();
        System.exit(0);
      }
    });
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
