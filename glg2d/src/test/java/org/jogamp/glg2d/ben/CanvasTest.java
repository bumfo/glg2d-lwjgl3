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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public final class CanvasTest extends JFrame {
  private static final boolean USE_GL = false;

  private double t = 0.;

  private Path2D.Double path = new Path2D.Double();
  private final JPanel panel;

  private CanvasTest() {
    super("Canvas Test");

    panel = new JPanel() {
      @Override
      public void paint(Graphics g0) {
        super.paint(g0);
        // g0.drawRect(100, 100, 50, 50);
        Graphics2D g = (Graphics2D) g0;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setStroke(new BasicStroke(0f));
        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g.setColor(new Color(1f, 0f, 1f, .9f));
        g.draw(new Rectangle2D.Double(100. + 50. * Math.sin(Math.PI * t / 60.), 100., 50., 50.));


        g.setStroke(new BasicStroke(1f));

        g.draw(path);

        t += 1.;
      }
    };
    panel.setPreferredSize(new Dimension(800, 600));

    GLG2DCanvas canvas;
    if (USE_GL) {
      canvas = new GLG2DCanvas(panel);
      canvas.setGLDrawing(true);
      this.add(canvas);
    } else {
      this.add(panel);
    }

    this.pack();
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    if (USE_GL) {
      Animator animator = new Animator();
      animator.add(canvas.getGLDrawable());
      animator.start();
    }

    MouseAdapter mouseAdapter = new MyMouseMotionAdapter();
    panel.addMouseListener(mouseAdapter);
    panel.addMouseMotionListener(mouseAdapter);
  }

  public static void main(String[] args) {
    new CanvasTest().setVisible(true);
  }

  private final class MyMouseMotionAdapter extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      path.moveTo(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      path.lineTo(e.getX(), e.getY());

      if (!USE_GL) {
        panel.repaint();
      }
    }
  }
}
