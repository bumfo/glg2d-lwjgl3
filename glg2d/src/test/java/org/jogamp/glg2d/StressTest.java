package org.jogamp.glg2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

import org.jogamp.glg2d.util.CustomPainter;
import org.jogamp.glg2d.util.TestWindow;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class StressTest {
  static final long TESTINGTIME = 10000;
  static final boolean USE_ANTIALIAS = true;

  static TestWindow tester;

  static Random rand = new Random();

  @BeforeClass
  public static void initialize() {
    tester = new TestWindow();
  }

  @AfterClass
  public static void close() {
    tester.finish();
  }

  @Test
  public void shapeTest() throws Exception {
    final int numshapes = 1000;
    TimedPainter painter = new TimedPainter() {
      @Override
      protected void paint(Graphics2D g2d) {
        g2d.setColor(Color.red);
        Rectangle2D.Float rect = new Rectangle2D.Float();
        float w = 20;
        float h = 40;
        float x = 300;
        float y = 400;
        for (int i = 0; i < numshapes; i++) {
          rect.setRect(rand.nextFloat() * x, rand.nextFloat() * y, rand.nextFloat() * w, rand.nextFloat() * h);
          g2d.fill(rect);
        }
      }
    };

    tester.setPainter(painter);
    painter.waitAndLogTimes("shapes");
  }

  @Test
  public void lineTest() throws Exception {
    final int numlines = 100;
    TimedPainter painter = new TimedPainter() {
      @Override
      protected void paint(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        int x = 300;
        int y = 400;
        int numpoints = 5;
        int[] xarray = new int[numpoints];
        int[] yarray = new int[numpoints];
        for (int i = 0; i < numlines; i++) {
          for (int j = 0; j < numpoints; j++) {
            xarray[j] = rand.nextInt(x);
            yarray[j] = rand.nextInt(y);
          }

          g2d.drawPolyline(xarray, yarray, numpoints);
        }
      }
    };

    tester.setPainter(painter);
    painter.waitAndLogTimes("lines");
  }

  @Test
  public void quadCurvedLineTest() throws Exception {
    final int numcurves = 100;
    TimedPainter painter = new TimedPainter() {
      @Override
      protected void paint(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        int x = 300;
        int y = 400;
        for (int i = 0; i < numcurves; i++) {
          g2d.draw(new QuadCurve2D.Double(
              rand.nextDouble() * x, rand.nextDouble() * y,
              rand.nextDouble() * x, rand.nextDouble() * y,
              rand.nextDouble() * x, rand.nextDouble() * y));
        }
      }
    };

    tester.setPainter(painter);
    painter.waitAndLogTimes("quad curves");
  }

  @Test
  public void arcTest() throws Exception {
    final int numarcs = 1000;
    TimedPainter painter = new TimedPainter() {
      @Override
      protected void paint(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        int x = 300;
        int y = 400;
        for (int i = 0; i < numarcs; i++) {
          g2d.drawArc(rand.nextInt(x), rand.nextInt(y),
              rand.nextInt(x / 2), rand.nextInt(y / 2),
              rand.nextInt(180), rand.nextInt(360));
        }
      }
    };

    tester.setPainter(painter);
    painter.waitAndLogTimes("arcs");
  }

  @Test
  public void cubicCurvedLineTest() throws Exception {
    final int numcurves = 100;
    TimedPainter painter = new TimedPainter() {
      @Override
      protected void paint(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        int x = 300;
        int y = 400;
        for (int i = 0; i < numcurves; i++) {
          g2d.draw(new CubicCurve2D.Double(
              rand.nextDouble() * x, rand.nextDouble() * y,
              rand.nextDouble() * x, rand.nextDouble() * y,
              rand.nextDouble() * x, rand.nextDouble() * y,
              rand.nextDouble() * x, rand.nextDouble() * y));
        }
      }
    };

    tester.setPainter(painter);
    painter.waitAndLogTimes("cubic curves");
  }

  @Test
  public void imageTest() throws Exception {
    final int numimages = 100;
    final Image image = ImageIO.read(StressTest.class.getClassLoader().getResource("duke.gif"));
    TimedPainter painter = new TimedPainter() {
      @Override
      protected void paint(Graphics2D g2d) {
        int x = 300;
        int y = 400;
        for (int i = 0; i < numimages; i++) {
          g2d.drawImage(image, rand.nextInt(x), rand.nextInt(y), rand.nextInt(x), rand.nextInt(y), null);
        }
      }
    };

    tester.setPainter(painter);
    painter.waitAndLogTimes("images");
  }

  @Test
  @Ignore
  public void newImageMemoryTest() throws Exception {
    TimedPainter painter = new TimedPainter() {
      Random r = new Random();

      @Override
      public void paint(Graphics2D g2d) {
        int w = 500;
        int h = 500;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D ig2d = img.createGraphics();
        ig2d.setStroke(new BasicStroke(5));
        ig2d.setColor(Color.white);
        for (int i = 0; i < 10; i++) {
          ig2d.drawArc(r.nextInt(w), r.nextInt(h), r.nextInt(w / 2), r.nextInt(h / 2), r.nextInt(360), r.nextInt(360));
        }
        ig2d.dispose();

        g2d.drawImage(img, null, null);
      }
    };

    tester.setPainter(painter);
    painter.waitIndefinitely("icon memory");
  }

  static abstract class TimedPainter implements CustomPainter {
    long[] times = new long[2];

    int[] calls = new int[2];

    @Override
    public void paint(Graphics2D g2d, boolean jogl) {
      if (USE_ANTIALIAS) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      }

      long start = System.nanoTime();
      paint(g2d);
      long end = System.nanoTime();

      int index = jogl ? 0 : 1;
      times[index] += end - start;
      calls[index]++;
    }

    protected abstract void paint(Graphics2D g2d);

    public void waitAndLogTimes(String type) throws InterruptedException {
      Thread.sleep(TESTINGTIME);

      System.out.println(String.format("JOGL for %s took an average of %.3f ms", type, times[0] / 1e6 / calls[0]));
      System.out.println(String.format("Java2D for %s took an average of %.3f ms", type, times[1] / 1e6 / calls[1]));
    }

    public void waitIndefinitely(String type) throws InterruptedException {
      while (true) {
        waitAndLogTimes(type);
      }
    }
  }
}
