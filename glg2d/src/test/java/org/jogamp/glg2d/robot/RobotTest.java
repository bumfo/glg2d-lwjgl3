package org.jogamp.glg2d.robot;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import org.jogamp.glg2d.GLG2DCanvas;
import org.jogamp.glg2d.GLGraphics2D;

import javax.imageio.ImageIO;
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public final class RobotTest extends JFrame {
  private double t = 0.;

  private RobotTest() {
    super("Robot Test");

    BufferedImage robotImg = getImage("robot.png");

    ImageAtlas atlas;
    try {
      atlas = ImageAtlas.parse("robot.atlas");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    double texScale = .18;

    RenderImageRegion body = atlas.findRegion("body-large1").toImageRegion(robotImg, texScale);
    RenderImageRegion gun = atlas.findRegion("turret9").toImageRegion(robotImg, texScale);
    RenderImageRegion radar = atlas.findRegion("radar11").toImageRegion(robotImg, texScale);

    JPanel panel = new JPanel() {
      @Override
      public void paint(Graphics g0) {
        super.paint(g0);
        Graphics2D g = (Graphics2D) g0;

        g.clearRect(0, 0, 800, 600);

        ((GLGraphics2D) g).setForcePremultipliedAlpha(true);

        AffineTransform at = AffineTransform.getTranslateInstance(.5 * getWidth(), .5 * getHeight());
        at.rotate(Math.PI * t / 60.);

        double s = 1. + 9. * (1. + Math.sin(t / 60));
        at.scale(s, s);

        body.setTransform(at);
        body.paint(g);

        gun.setTransform(at);
        gun.paint(g);

        radar.setTransform(at);
        radar.paint(g);

        t += 1.;
      }
    };
    panel.setBackground(Color.BLACK);

    panel.setPreferredSize(new Dimension(800, 600));
    GLG2DCanvas canvas = new GLG2DCanvas(new GLCapabilities(GLProfile.get(GLProfile.GL3)), panel);
    // GLG2DCanvas canvas = new GLG2DCanvas(panel);
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
