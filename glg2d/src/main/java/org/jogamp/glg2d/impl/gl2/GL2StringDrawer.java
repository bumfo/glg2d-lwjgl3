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
import com.jogamp.opengl.util.awt.TextRenderer2;
import org.jogamp.glg2d.impl.AbstractTextDrawer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.util.HashMap;

/**
 * Draws text for the {@code GLGraphics2D} class.
 */
public class GL2StringDrawer extends AbstractTextDrawer {
  protected final float[] testMatrix = new float[16];
  protected final float[] testMatrix2 = new float[16];
  protected final float[] tmpV0 = new float[4];
  protected final float[] tmpV1 = new float[4];
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
    float toPixScale = peek().surfaceScale;

    final boolean alignPixel = peek().alignPixel;
    GL2 gl = null;
    if (alignPixel) {
      gl = g2d.getGLContext().getGL().getGL2();
      gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, testMatrix, 0);

      float scaleX = Math.abs(testMatrix[0]);
      float scaleY = Math.abs(testMatrix[5]);

      toPixScale *= Math.max(scaleX, scaleY);
    }

    if (toPixScale != 1f) {
      font = font.deriveFont(font.getSize() * toPixScale);
    }
    TextRenderer2 renderer = getRenderer(font);

    begin(renderer);

    float drawX = x;
    float drawY = g2d.getSurfaceHeight() - y;

    if (alignPixel) {
      gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, testMatrix, 0);
      FloatUtil.invertMatrix(testMatrix, testMatrix2);

      alignPixel(drawX, drawY);

      drawX = tmpV0[0];
      drawY = tmpV0[1];
    }

    renderer.draw3D(string, drawX, drawY, 0, 1f / toPixScale);
    end(renderer);
  }

  private void alignPixel(float drawX, float drawY) {
    tmpV0[0] = drawX;
    tmpV0[1] = drawY;
    tmpV0[2] = 0f;
    tmpV0[3] = 1f;

    FloatUtil.multMatrixVec(testMatrix, tmpV0, tmpV1);

    tmpV1[0] = Math.round(tmpV1[0]);
    tmpV1[1] = Math.round(tmpV1[1]);

    FloatUtil.multMatrixVec(testMatrix2, tmpV1, tmpV0);
  }

  protected TextRenderer2 getRenderer(Font font) {
    return cache.getRenderer(font, peek().antiAlias);
  }

  /**
   * Sets the font color, respecting the AlphaComposite if it wants to
   * pre-multiply an alpha.
   */
  protected void setTextColorRespectComposite(TextRenderer2 renderer) {
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

  protected void begin(TextRenderer2 renderer) {
    setTextColorRespectComposite(renderer);

    GL2 gl = g2d.getGLContext().getGL().getGL2();
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    gl.glPushMatrix();
    gl.glScalef(1, -1, 1);
    gl.glTranslatef(0, -g2d.getSurfaceHeight(), 0);

    renderer.begin3DRendering();
  }

  protected void end(TextRenderer2 renderer) {
    renderer.end3DRendering();

    GL2 gl = g2d.getGLContext().getGL().getGL2();
    gl.glPopMatrix();
  }

  @SuppressWarnings("serial")
  public static class FontRenderCache extends HashMap<Font, TextRenderer2[]> {
    public TextRenderer2 getRenderer(Font font, boolean antiAlias) {
      TextRenderer2[] renderers = get(font);
      if (renderers == null) {
        renderers = new TextRenderer2[2];
        put(font, renderers);
      }

      TextRenderer2 renderer = renderers[antiAlias ? 1 : 0];

      if (renderer == null) {
        renderer = new TextRenderer2(font, antiAlias, false);
        renderers[antiAlias ? 1 : 0] = renderer;
      }

      return renderer;
    }

    public void dispose() {
      for (TextRenderer2[] value : values()) {
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
