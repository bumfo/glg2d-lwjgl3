package org.jogamp.glg2d.examples;

import org.jogamp.glg2d.GLG2DPanel;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Dimension;

public class AWTExample {
  public static void main(String[] args) {
    JFrame frame = new JFrame("GLG2D AWT Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(300, 300));

    JComponent comp = Example.createComponent();

    // JPanel panel = new JPanel();
    // panel.setLayout(new BorderLayout());
    // panel.add(comp);
    // frame.setContentPane(panel);

    frame.setContentPane(new GLG2DPanel(comp));

    frame.pack();
    frame.setVisible(true);
  }
}
