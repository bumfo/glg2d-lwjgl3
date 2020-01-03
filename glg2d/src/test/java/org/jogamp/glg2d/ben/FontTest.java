package org.jogamp.glg2d.ben;

import org.jogamp.glg2d.GLG2DCanvas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.Map;

public final class FontTest extends JFrame {
  private static final boolean USE_GL = true;
  private float t = 0f;
  private Map<?, ?> desktopHints;

  private FontTest() {
    super("FontTest");

    desktopHints = getDesktopHints();

    JPanel panel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        // g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        //
        // Map<?, ?> hints = getDesktopHints();
        // if (!Objects.deepEquals(hints, desktopHints)) {
        //   desktopHints = hints;
        //   System.out.println(desktopHints);
        // }


        g.setFont(getFont().deriveFont(10f * Math.min(getWidth() / 400f, getHeight() / 400f)));
        // g.setFont(getFont().deriveFont(10f * (float) (2. + Math.sin(t / 60f * Math.PI))));

        g.setColor(Color.WHITE);
        // g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        // g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);

        if (desktopHints != null) {
          g.addRenderingHints(desktopHints);
        }
        // g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawString("Hello " + g.getFont().getSize(), getWidth() / 2f, getHeight() / 2f - 50);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g.drawString("Hello " + g.getFont().getSize(), getWidth() / 2f, getHeight() / 2f + 50);

        t += 1f;
      }
    };

    panel.setBackground(Color.BLACK);
    panel.setPreferredSize(new Dimension(400, 400));

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

  private Map<?, ?> getDesktopHints() {
    return (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
  }

  public static void main(String[] args) {
    new FontTest().setVisible(true);
  }
}
