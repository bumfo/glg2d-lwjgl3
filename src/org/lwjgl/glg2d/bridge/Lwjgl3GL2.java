package org.lwjgl.glg2d.bridge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;


public class Lwjgl3GL2 extends Lwjgl3GL20 implements GL {
  public void glCopyPixels(int x, int y, int width, int height, int type) {
    GL20.glCopyPixels(x, y, width, height, type);
  }

  public void glRasterPos2i(int x, int y) {
    GL20.glRasterPos2i(x, y);
  }

  @Override
  public void glOrtho(double left, double right, double bottom, double top, double near_val, double far_val) {
    GL11.glOrtho(left, right, bottom, top, near_val, far_val);
  }
}
