package org.lwjgl.glg2d.bridge;

import java.awt.Color;
import java.awt.Font;

public class TextRenderer {
  private boolean smoothing;
  private Color color;

  public TextRenderer(final Font font, final boolean antialiased,
                      final boolean useFractionalMetrics) {}

  public void setSmoothing(boolean smoothing) {
    this.smoothing = smoothing;
  }

  public boolean getSmoothing() {
    return smoothing;
  }

  public void dispose() {
  }

  public void end3DRendering() {
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public Color getColor() {
    return color;
  }

  public void begin3DRendering() {
  }

  public void draw3D(final String str, final float x, final float y, final float z, final float scaleFactor) {
  }
}
