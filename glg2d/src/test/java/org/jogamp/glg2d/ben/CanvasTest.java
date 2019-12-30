package org.jogamp.glg2d.ben;

import com.jogamp.opengl.util.Animator;
import org.jogamp.glg2d.GLG2DCanvas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

public final class CanvasTest extends JFrame {
  private double t = 0.;

  private CanvasTest() {
    JPanel panel = new JPanel() {
      @Override
      public void paint(Graphics g) {
        super.paint(g);
        // g.drawRect(100, 100, 50, 50);
        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(0f));
        // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g2.setColor(new Color(1f, 0f, 1f, .9f));
        g2.draw(new Rectangle2D.Double(100. + 50. * Math.sin(Math.PI * t / 60.), 100., 50., 50.));

        t += 1.;
      }
    };
    panel.setPreferredSize(new Dimension(800, 600));
    GLG2DCanvas canvas = new GLG2DCanvas(panel);
    canvas.setGLDrawing(true);

    this.add(canvas);

    this.pack();
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    Animator animator = new Animator();
    animator.add(canvas.getGLDrawable());
    animator.start();
  }

  public static void main(String[] args) {
    new CanvasTest().setVisible(true);
  }
}
