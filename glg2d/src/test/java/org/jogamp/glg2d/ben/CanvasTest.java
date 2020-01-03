package org.jogamp.glg2d.ben;

import com.jogamp.opengl.util.Animator;
import org.jogamp.glg2d.GLG2DCanvas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public final class CanvasTest extends JFrame {
  private static final boolean USE_GL = true;

  private double t = 0.;

  private ArrayList<Path2D.Double> paths = new ArrayList<>();

  private Path2D.Double path = new Path2D.Double();
  private final JPanel panel;

  private Point pressed = null;

  private CanvasTest() {
    super("Canvas Test");

    panel = new JPanel() {
      @Override
      public void paint(Graphics g0) {
        float aaa = .5f * ((float) Math.sin(t / 600f * Math.PI / 2f) + 1f);

        panel.setBackground(new Color(aaa, aaa, aaa));

        super.paint(g0);

        // g0.drawRect(100, 100, 50, 50);
        Graphics2D g = (Graphics2D) g0;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setStroke(new BasicStroke(0f));
        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g.setColor(new Color(1f, 0f, 1f, .9f));
        g.draw(new Rectangle2D.Double(100. + 50. * Math.sin(Math.PI * t / 60.), 100., 50., 50.));


        g.setStroke(new BasicStroke(1f));

        g.setColor(new Color(0f, 0f, 0f, .1f));
        g.draw(new Line2D.Double(200, 100, 200, 150));
        g.setColor(new Color(0f, 0f, 0f, .3f));
        g.draw(new Line2D.Double(205, 100, 205, 150));
        g.setColor(new Color(0f, 0f, 0f, .5f));
        g.draw(new Line2D.Double(210, 100, 210, 150));
        g.setColor(new Color(0f, 0f, 0f, .7f));
        g.draw(new Line2D.Double(215, 100, 215, 150));
        g.setColor(new Color(0f, 0f, 0f, .9f));
        g.draw(new Line2D.Double(220, 100, 220, 150));
        g.setColor(new Color(0f, 0f, 0f, 1f));
        g.draw(new Line2D.Double(225, 100, 225, 150));


        g.setColor(new Color(1f, 0f, 0f, .5f));
        g.fill(new Rectangle2D.Double(200, 200, 50, 50));
        g.setColor(new Color(0f, 1f, 0f, .5f));
        g.fill(new Rectangle2D.Double(225, 200, 50, 50));

        g.draw(path);
        for (Path2D.Double path : paths) {
          g.draw(path);
        }

        t += 1.;
      }
    };
    panel.setBackground(new Color(0f, 1f, 1f));
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

    if (USE_GL) {
      Component comp = (Component) canvas.getGLDrawable();
      comp.setEnabled(true);
      comp.addMouseListener(mouseAdapter);
      comp.addMouseMotionListener(mouseAdapter);
    } else {
      panel.addMouseListener(mouseAdapter);
      panel.addMouseMotionListener(mouseAdapter);
    }
  }

  public static void main(String[] args) {
    new CanvasTest().setVisible(true);
  }

  private final class MyMouseMotionAdapter extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      pressed = e.getPoint();
      System.out.println("pressed");
      path.moveTo(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      path.reset();

      path.moveTo(pressed.x, pressed.y);

      path.lineTo(e.getX(), e.getY());

      if (!USE_GL) {
        panel.repaint();
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      paths.add(path);
      path = new Path2D.Double();
    }
  }
}
