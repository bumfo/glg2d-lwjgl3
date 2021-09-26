package org.jogamp.glg2d.svg;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import com.kitfox.svg.A;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;
import org.jogamp.glg2d.GLG2DCanvas;
import org.jogamp.glg2d.GLGraphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.net.URI;
import java.net.URL;

public final class SvgTest extends JFrame {
  private double t = 0.;

  public static void main(String[] args) {
    new SvgTest().run();
  }

  public SvgTest() {
    super("Svg Test");
  }

  public void run() {
    SVGUniverse universe = new SVGUniverse();
    URI svg = universe.loadSVG(getURL("robot.svg"));
    SVGDiagram diagram = universe.getDiagram(svg);
    // diagram.setIgnoringClipHeuristic(true);

    JPanel panel = new JPanel() {
      @Override
      public void paint(Graphics g0) {
        super.paint(g0);
        Graphics2D g = (Graphics2D) g0;

        // g.clearRect(0, 0, 800, 600);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, 800, 600);

        g.setColor(Color.BLACK);

        // ((GLGraphics2D) g).setForcePremultipliedAlpha(true);

        AffineTransform at = AffineTransform.getTranslateInstance(.5 * getWidth(), .5 * getHeight());
        at.rotate(Math.PI * t / 60.);

        double s = 1. + 9. * (1. + Math.sin(t / 60));
        at.scale(s, s);

        g.setClip(0, 0, 800, 600);

        AffineTransform af = new AffineTransform();
        af.rotate(Math.PI * t / 60., 100, 100);
        g.setTransform(af);

        try {
          diagram.render(g);
        } catch (SVGException e) {
          throw new RuntimeException(e);
        }

        // t += 1.;
      }
    };
    panel.setBackground(Color.BLACK);

    panel.setPreferredSize(new Dimension(800, 600));
    // GLG2DCanvas canvas = new GLG2DCanvas(new GLCapabilities(GLProfile.get(GLProfile.GL3)), panel);
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

    setVisible(true);
  }

  public static URL getURL(String filename) {
    URL url = SvgTest.class.getClassLoader().getResource(filename);

    if (url == null) {
      throw new RuntimeException("Could not load because of invalid filename: " + filename);
    }

    return url;
  }
}
