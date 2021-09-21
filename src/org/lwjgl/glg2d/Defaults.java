package org.lwjgl.glg2d;

import javax.swing.JLabel;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.util.Map;

public final class Defaults {
  public static final Color BACKGROUND = Color.black;
  public static final Color COLOR = Color.white;
  // public static final Font FONT = getDefaultFont();
  public static final BasicStroke STROKE = new BasicStroke();
  public static final AlphaComposite COMPOSITE = AlphaComposite.SrcOver;
  public static final Shape CLIP = null;
  public static final Map<?, ?> RENDERING_HINTS = null;

  private static Font getDefaultFont() {
    Font font = Font.getFont(Font.SANS_SERIF);
    if (font != null) {
      return font;
    }
    return new JLabel().getFont();
  }
}
