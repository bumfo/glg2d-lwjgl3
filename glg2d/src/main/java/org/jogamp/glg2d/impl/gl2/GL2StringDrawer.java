/*
 * Copyright 2015 Brandon Borkholder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jogamp.glg2d.impl.gl2;


import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.awt.TextRenderer;
import org.jogamp.glg2d.impl.AbstractTextDrawer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.text.AttributedCharacterIterator;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Draws text for the {@code GLGraphics2D} class.
 */
public class GL2StringDrawer extends AbstractTextDrawer {
  protected final float[] testMatrix = new float[16];
  protected final float[] testMatrix2 = new float[16];
  protected final float[] vIn = new float[4];
  protected final float[] vOut = new float[4];
  protected FontRenderCache cache = new FontRenderCache();

  @Override
  public void dispose() {
    cache.dispose();
  }

  @Override
  public void drawString(AttributedCharacterIterator iterator, int x, int y) {
    drawString(iterator, (float) x, (float) y);
  }

  @Override
  public void drawString(AttributedCharacterIterator iterator, float x, float y) {
    StringBuilder builder = new StringBuilder(iterator.getEndIndex() - iterator.getBeginIndex());
    while (iterator.next() != AttributedCharacterIterator.DONE) {
      builder.append(iterator.current());
    }

    drawString(builder.toString(), x, y);
  }

  @Override
  public void drawString(String string, int x, int y) {
    drawString(string, (float) x, (float) y);
  }

  @Override
  public void drawString(String string, float x, float y) {
    Font font = getFont();
    float surfaceScale = peek().surfaceScale;
    if (surfaceScale != 1f) {
      font = font.deriveFont(font.getSize() * surfaceScale);
    }
    TextRenderer renderer = getRenderer(font);

    begin(renderer);


    float drawX = x;
    float drawY = g2d.getSurfaceHeight() - y;

    if (peek().alignPixel) {
      GL2 gl = g2d.getGLContext().getGL().getGL2();
      gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, testMatrix, 0);

      vIn[0] = drawX;
      vIn[1] = drawY;
      vIn[2] = 0f;
      vIn[3] = 0f;
      FloatUtil.multMatrixVec(testMatrix, vIn, vOut);

      vIn[0] = Math.round(vOut[0]);
      vIn[1] = Math.round(vOut[1]);
      vIn[2] = 0f;
      vIn[3] = 0f;

      FloatUtil.invertMatrix(testMatrix, testMatrix2);

      FloatUtil.multMatrixVec(testMatrix2, vIn, vOut);

      System.out.println(Arrays.toString(vOut));

      drawX = vOut[0];
      drawY = vOut[1];
    }

    renderer.draw3D(string, drawX, drawY, 0, 1f / surfaceScale);
    end(renderer);
  }

  protected TextRenderer getRenderer(Font font) {
    return cache.getRenderer(font, peek().antiAlias);
  }

  /**
   * Sets the font color, respecting the AlphaComposite if it wants to
   * pre-multiply an alpha.
   */
  protected void setTextColorRespectComposite(TextRenderer renderer) {
    Color color = g2d.getColor();
    if (g2d.getComposite() instanceof AlphaComposite) {
      float alpha = ((AlphaComposite) g2d.getComposite()).getAlpha();
      if (alpha < 1) {
        float[] rgba = color.getRGBComponents(null);
        color = new Color(rgba[0], rgba[1], rgba[2], alpha * rgba[3]);
      }
    }

    renderer.setColor(color);
  }

  protected void begin(TextRenderer renderer) {
    setTextColorRespectComposite(renderer);

    GL2 gl = g2d.getGLContext().getGL().getGL2();
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    gl.glPushMatrix();
    gl.glScalef(1, -1, 1);
    gl.glTranslatef(0, -g2d.getSurfaceHeight(), 0);

    renderer.begin3DRendering();
  }

  protected void end(TextRenderer renderer) {
    renderer.end3DRendering();

    GL2 gl = g2d.getGLContext().getGL().getGL2();
    gl.glPopMatrix();
  }

  @SuppressWarnings("serial")
  public static class FontRenderCache extends HashMap<Font, TextRenderer[]> {
    public TextRenderer getRenderer(Font font, boolean antiAlias) {
      TextRenderer[] renderers = get(font);
      if (renderers == null) {
        renderers = new TextRenderer[2];
        put(font, renderers);
      }

      TextRenderer renderer = renderers[antiAlias ? 1 : 0];

      if (renderer == null) {
        renderer = new TextRenderer(font, antiAlias, false);
        renderers[antiAlias ? 1 : 0] = renderer;
      }

      return renderer;
    }

    public void dispose() {
      for (TextRenderer[] value : values()) {
        if (value[0] != null) {
          value[0].dispose();
        }
        if (value[1] != null) {
          value[1].dispose();
        }
      }
    }
  }
}
