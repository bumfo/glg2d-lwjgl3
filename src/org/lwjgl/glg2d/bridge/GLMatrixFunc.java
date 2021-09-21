package org.lwjgl.glg2d.bridge;

public interface GLMatrixFunc {

  /** MatrixMode */
  int
      GL_MODELVIEW  = 0x1700,
      GL_PROJECTION = 0x1701,
      GL_TEXTURE    = 0x1702;

  void glMatrixMode(int mode);

  void glPushMatrix();

  void glPopMatrix();
}
