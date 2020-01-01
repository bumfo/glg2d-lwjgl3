package org.jogamp.glg2d.gl3.backend;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GLEventListener;

public interface GLApp {
  void setup(GLConfig config, GLEventListener listener);

  void addKeyListener(KeyListener keyListener);

  void destroy();
}
