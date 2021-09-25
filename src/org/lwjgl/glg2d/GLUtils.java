package org.lwjgl.glg2d;

import org.lwjgl.glg2d.bridge.GL;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Method;

public final class GLUtils {
  public static int getViewportHeight(GL gl) {
    int[] viewportDimensions = new int[4];
    gl.glGetIntegerv(GL.GL_VIEWPORT, viewportDimensions, 0);
    int canvasHeight = viewportDimensions[3];
    return canvasHeight;
  }

  public static float[] getScale(float[] result) {
    float sx = 1f, sy = 1f;
    // --add-opens=java.desktop/sun.awt=ALL-UNNAMED

    final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    final Class<?> gdClass = gd.getClass();
    Method getScaleFactorMethod = null;
    try {
      getScaleFactorMethod = gdClass.getDeclaredMethod("getScaleFactor");
      getScaleFactorMethod.setAccessible(true);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    if (null != getScaleFactorMethod) {
      // Generic (?)
      try {
        final Object res = getScaleFactorMethod.invoke(gd);
        if (res instanceof Integer) {
          sx = ((Integer) res).floatValue();
        } else if (res instanceof Double) {
          sx = ((Double) res).floatValue();
        }
        sy = sx;
      } catch (final Throwable ignored) {
      }
    }

    System.out.println("scale=" + sx + "," + sy);

    result[0] = sx;
    result[1] = sy;

    return result;
  }
}
