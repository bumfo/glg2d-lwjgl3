package org.jogamp.glg2d.ben;

import org.jogamp.glg2d.GLG2DCanvas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public final class FontTest extends JFrame {
  private static final boolean USE_GL = true;
  private float t = 0f;

  private FontTest() {
    super("FontTest");

    JPanel panel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        g.setFont(getFont().deriveFont(10f * (float) (2. + Math.sin(t / 60f * Math.PI))));

        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g.drawString("Hello", 100, 100);

        t += 1f;
      }
    };

    panel.setBackground(Color.BLACK);
    panel.setPreferredSize(new Dimension(640, 480));

    GLG2DCanvas canvas;
    if (USE_GL) {
      canvas = new GLG2DCanvas(panel);
      canvas.setGLDrawing(true);
      this.add(canvas);
    } else {
      this.add(panel);
    }

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
  }

  public static void main(String[] args) {
    new FontTest().setVisible(true);
  }
}
