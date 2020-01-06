package org.jogamp.glg2d.reflect;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class MacMenuTest {
  private MacMenuTest() {
  }

  @SuppressWarnings("unused")
  public void handleAbout(Object e) {
    JOptionPane.showMessageDialog(null, "About dialog");
  }

  @SuppressWarnings("unused")
  public void handlePreferences(Object e) {
    JOptionPane.showMessageDialog(null, "Preferences dialog");
  }

  @SuppressWarnings("unused")
  public void handleQuitRequestWith(Object e, Object r) {
    JOptionPane.showMessageDialog(null, "Quit dialog");
    System.exit(0);
  }

  public static void main(String[] args) {
    try {
      initMacMenu();
    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("java.awt.Desktop");
      frame.setSize(new Dimension(600, 400));
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.setVisible(true);
    });
  }

  private static void initMacMenu() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Class<?> desktopClass = Class.forName("java.awt.Desktop");
    Method getDesktopMethod = desktopClass.getMethod("getDesktop");

    // Desktop desktop = Desktop.getDesktop();
    Object desktopInstance = getDesktopMethod.invoke(null);

    Class<?> aboutHandlerClass = Class.forName("java.awt.desktop.AboutHandler");
    Class<?> preferencesHandlerClass = Class.forName("java.awt.desktop.PreferencesHandler");
    Class<?> quitHandlerClass = Class.forName("java.awt.desktop.QuitHandler");

    Method setAboutHandlerMethod = desktopClass.getMethod("setAboutHandler", aboutHandlerClass);
    Method setPreferencesHandlerMethod = desktopClass.getMethod("setPreferencesHandler", preferencesHandlerClass);
    Method setQuitHandlerMethod = desktopClass.getMethod("setQuitHandler", quitHandlerClass);

    MacMenuTest handler = new MacMenuTest();

    Object o = DynamicImplement.makeImplement(new Class<?>[]{
        aboutHandlerClass,
        preferencesHandlerClass,
        quitHandlerClass
    }, handler);

    // desktop.setAboutHandler(new AboutHandler() {
    //   @Override
    //   public void handleAbout(AboutEvent e) {
    //     handler.handleAbout(e);
    //   }
    // });
    // noinspection JavaReflectionInvocation
    setAboutHandlerMethod.invoke(desktopInstance, o);

    // desktop.setPreferencesHandler(new PreferencesHandler() {
    //   @Override
    //   public void handlePreferences(PreferencesEvent e) {
    //     handler.handlePreferences(e);
    //   }
    // });
    // noinspection JavaReflectionInvocation
    setPreferencesHandlerMethod.invoke(desktopInstance, o);

    // desktop.setQuitHandler(new QuitHandler() {
    //   @Override
    //   public void handleQuitRequestWith(QuitEvent e, QuitResponse r) {
    //     handler.handleQuitRequestWith(e, r);
    //   }
    // });
    // noinspection JavaReflectionInvocation
    setQuitHandlerMethod.invoke(desktopInstance, o);
  }
}