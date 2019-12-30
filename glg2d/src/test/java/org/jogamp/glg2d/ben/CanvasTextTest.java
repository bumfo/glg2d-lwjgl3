package org.jogamp.glg2d.ben;

import com.jogamp.opengl.util.Animator;
import org.jogamp.glg2d.GLG2DCanvas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public final class CanvasTextTest extends JFrame {
  private double t = 0.;

  private CanvasTextTest() {
    JPanel panel = new JPanel() {
      @Override
      public void paint(Graphics g) {
        super.paint(g);
        // g.drawRect(100, 100, 50, 50);
        Graphics2D g2 = (Graphics2D) g;

        // g2.setFont(new Font("Arial", Font.PLAIN, 32));

        g2.draw(new Rectangle2D.Double(100. + 50. * Math.sin(Math.PI * t / 60.), 100., 50., 50.));

        g2.drawString("Hello World", 100f + 50f * (float) Math.sin(Math.PI * t / 60.), 100f);

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

    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          if (animator.isPaused()) {
            animator.resume();
          } else {
            animator.pause();
          }
        }
      }
    });
  }

  public static void main(String[] args) {
    new CanvasTextTest().setVisible(true);
  }
}
