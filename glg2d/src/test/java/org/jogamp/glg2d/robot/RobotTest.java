package org.jogamp.glg2d.robot;

import com.jogamp.opengl.util.Animator;
import org.jogamp.glg2d.GLG2DCanvas;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public final class RobotTest extends JFrame {
  private static final int FIELD_WIDTH = 800;
  private static final int FIELD_HEIGHT = 600;
  private double t = 0.;

  private RobotTest() {
    super("Robot Test");

    BufferedImage robotImg = getImage("robot.png");

    RenderImageRegion body = new RenderImageRegion(robotImg, 125, 88, 197, 214, .18);

    JPanel panel = new JPanel() {
      @Override
      public void paint(Graphics g0) {
        super.paint(g0);
        Graphics2D g = (Graphics2D) g0;

        AffineTransform at = AffineTransform.getTranslateInstance(.5 * FIELD_WIDTH, .5 * FIELD_HEIGHT);
        at.rotate(Math.PI * t / 60.);
        body.setTransform(at);
        body.paint(g);

        t += 1.;
      }
    };
    panel.setBackground(Color.BLACK);

    panel.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
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

  /**
   * Returns an image resource.
   *
   * @param filename the filename of the image to load
   * @return the loaded image
   */
  public static BufferedImage getImage(String filename) {
    URL url = RobotTest.class.getClassLoader().getResource(filename);

    if (url == null) {
      throw new RuntimeException("Could not load image because of invalid filename: " + filename);
    }

    try {
      final BufferedImage result = ImageIO.read(url);

      if (result == null) {
        throw new RuntimeException("Could not load image: " + filename);
      }
      return result;
    } catch (IOException e) {
      throw new RuntimeException("Could not load image: " + filename);
    }
  }

  public static void main(String[] args) {
    new RobotTest().setVisible(true);
  }
}
