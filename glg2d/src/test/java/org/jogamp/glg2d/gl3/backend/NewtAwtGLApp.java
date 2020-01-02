package org.jogamp.glg2d.gl3.backend;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Window;
import java.lang.reflect.Method;

public final class NewtAwtGLApp implements GLApp {
  private JFrame frame;
  private GLWindow window;

  @Override
  public void setup(GLConfig config, GLEventListener listener) {
    frame = new JFrame(config.title);

    initJFrame(frame);

    GLCapabilities caps = Backends.getGlCapabilities(config);

    window = GLWindow.create(caps);
    window.setSize(config.width + 1, config.height);

    NewtCanvasAWT canvas = new NewtCanvasAWT(window);

    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    menuBar.add(fileMenu);

    fileMenu.add(new JMenuItem("Open"));
    fileMenu.add(new JSeparator());
    fileMenu.add(new JMenuItem("Close"));

    frame.setJMenuBar(menuBar);

    frame.add(canvas);

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    frame.setVisible(true);

    window.addGLEventListener(new GLDrawableHelper());
    window.addGLEventListener(listener);

    window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DO_NOTHING_ON_CLOSE);
    window.addWindowListener(new GLWindowManager(window, true));

    window.setSize(config.width, config.height);
    frame.pack();
  }

  @Override
  public void addKeyListener(KeyListener keyListener) {
    window.addKeyListener(keyListener);
  }

  @Override
  public void destroy() {
    frame.dispose();
  }

  private static void initJFrame(JFrame frame) {
    frame.setBackground(Color.BLACK);

    System.setProperty("apple.laf.useScreenMenuBar", "true");
    try {
      Class<?> util = Class.forName("com.apple.eawt.FullScreenUtilities");
      Method method = util.getMethod("setWindowCanFullScreen", Window.class, Boolean.TYPE);
      method.invoke(util, frame, true);
    } catch (Throwable ignore) {
    }
  }
}
