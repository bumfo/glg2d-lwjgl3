package org.lwjgl.glg2d;

import org.lwjgl.glg2d.bridge.GL;

public final class GLUtils {
  public static int getViewportHeight(GL gl) {
    int[] viewportDimensions = new int[4];
    gl.glGetIntegerv(GL.GL_VIEWPORT, viewportDimensions, 0);
    int canvasHeight = viewportDimensions[3];
    return canvasHeight;
  }
}
