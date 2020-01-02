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
package org.jogamp.glg2d;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.JComponent;

/**
 * Wraps a {@code JComponent} and paints it using a {@code GLGraphics2D}. This
 * object will paint the entire component fully for each frame.
 *
 * <p>
 * {@link GLG2DHeadlessListener} may also be used to listen for reshapes and
 * update the size and layout of the painted Swing component.
 * </p>
 */
public class GLG2DSimpleEventListener implements GLEventListener {
  private static final boolean LEGACY_HI_DPI = System.getProperty("java.specification.version").startsWith("1.");

  private final float[] scale = new float[2];

  private float manualScale = 1f;

  private int logicWidth;
  private int logicHeight;

  /**
   * The cached object.
   */
  protected GLGraphics2D g2d;

  /**
   * The component to paint.
   */
  protected JComponent comp;

  public GLG2DSimpleEventListener(JComponent component) {
    if (component == null) {
      throw new NullPointerException("component is null");
    }

    this.comp = component;
  }

  void setManualScale(float manualScale) {
    this.manualScale = manualScale;
  }

  @Override
  public void display(GLAutoDrawable drawable) {
    prePaint(drawable);
    paintGL(g2d);
    postPaint(drawable);
  }

  /**
   * Called before any painting is done. This should setup the matrices and ask
   * the {@code GLGraphics2D} object to setup any client state.
   */
  protected void prePaint(GLAutoDrawable drawable) {
    setupViewport(drawable);
    if (LEGACY_HI_DPI) {
      g2d.prePaint(drawable.getContext(), logicWidth, logicHeight, scale[0], scale[1]);
    } else {
      g2d.prePaint(drawable.getContext(), logicWidth, logicHeight, manualScale, manualScale);
    }

    // clip to only the component we're painting

    // int surfaceX = SurfaceScaleUtils.scale(comp.getX(), scale[0]);
    // int surfaceY = SurfaceScaleUtils.scale(comp.getY(), scale[1]);
    // int surfaceWidth = SurfaceScaleUtils.scale(comp.getWidth(), scale[0]);
    // int surfaceHeight = SurfaceScaleUtils.scale(comp.getHeight(), scale[1]);

    // g2d.translate(comp.getX(), comp.getY());
    // g2d.clipRect(0, 0, comp.getWidth(), comp.getHeight());

    // g2d.translate(surfaceX, surfaceY);
    // g2d.clipRect(0, 0, surfaceWidth, surfaceHeight);
  }

  /**
   * Defines the viewport to paint into.
   */
  protected void setupViewport(GLAutoDrawable drawable) {
    if (LEGACY_HI_DPI) {
      drawable.getGL().glViewport(0, 0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
    } else {
      // drawable.getGL().glViewport(0, 0, (int) (logicWidth * scale[0] * manualScale), (int) (logicHeight * scale[1] * manualScale));
      drawable.getGL().glViewport(0, 0,
          (int) (logicWidth * manualScale + 0.5),
          (int) (logicHeight * manualScale + 0.5));
    }
  }

  /**
   * Called after all Java2D painting is complete.
   */
  protected void postPaint(GLAutoDrawable drawable) {
    g2d.postPaint();
  }

  /**
   * Paints using the {@code GLGraphics2D} object. This could be forwarded to
   * any code that expects to draw using the Java2D framework.
   * <p>
   * Currently is paints the component provided, turning off double-buffering in
   * the {@code RepaintManager} to force drawing directly to the
   * {@code Graphics2D} object.
   * </p>
   */
  protected void paintGL(GLGraphics2D g2d) {
    boolean wasDoubleBuffered = comp.isDoubleBuffered();
    comp.setDoubleBuffered(false);

    comp.paint(g2d);

    comp.setDoubleBuffered(wasDoubleBuffered);
  }

  @Override
  public void init(GLAutoDrawable drawable) {
    GL gl = drawable.getGL();
    gl.setSwapInterval(1);

    g2d = createGraphics2D(drawable);
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    if (drawable instanceof GLCanvas) {
      GLCanvas canvas = (GLCanvas) drawable;

      canvas.getCurrentSurfaceScale(scale);

      logicWidth = canvas.getWidth();
      logicHeight = canvas.getHeight();
    } else {
      System.err.println(drawable.getClass().getName() + " not recognized. ");
    }
  }

  /**
   * Creates the {@code Graphics2D} object that forwards Java2D calls to OpenGL
   * calls.
   */
  protected GLGraphics2D createGraphics2D(GLAutoDrawable drawable) {
    return new GLGraphics2D();
  }

  @Override
  public void dispose(GLAutoDrawable arg0) {
    if (g2d != null) {
      g2d.glDispose();
      g2d = null;

      System.exit(0);
    }
  }
}
