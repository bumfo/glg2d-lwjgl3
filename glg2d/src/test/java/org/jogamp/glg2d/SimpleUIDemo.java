package org.jogamp.glg2d;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import java.awt.Dimension;

public final class SimpleUIDemo extends JPanel {
  public SimpleUIDemo() {
    add(new JButton("Hello World"));
  }

  public static void main(String[] args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    JFrame frame = new JFrame("SimpleUIDemo");

    JPopupMenu.setDefaultLightWeightPopupEnabled(false);

    // frame.setContentPane(new SimpleUIDemo());
    frame.setContentPane(new GLG2DPanel(new SimpleUIDemo()));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(800, 600));
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
