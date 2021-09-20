package org.lwjgl.glg2d.bridge;

public interface GLPointerFunc {
  int GL_VERTEX_ARRAY = 0x8074;
  int GL_NORMAL_ARRAY = 0x8075;
  int GL_COLOR_ARRAY = 0x8076;
  int GL_TEXTURE_COORD_ARRAY = 0x8078;

  void glEnableClientState(int arrayName);
  void glDisableClientState(int arrayName);

  // void glVertexPointer(GLArrayData array);
  // void glVertexPointer(int size, int type, int stride, java.nio.Buffer pointer);
  void glVertexPointer(int size, int type, int stride, long pointer_buffer_offset);

  // void glColorPointer(GLArrayData array);
  // void glColorPointer(int size, int type, int stride, java.nio.Buffer pointer);
  void glColorPointer(int size, int type, int stride, long pointer_buffer_offset);
  void glColor4f(float red, float green, float blue, float alpha);

  // void glNormalPointer(GLArrayData array);
  // void glNormalPointer(int type, int stride, java.nio.Buffer pointer);
  void glNormalPointer(int type, int stride, long pointer_buffer_offset);

  // void glTexCoordPointer(GLArrayData array);
  // void glTexCoordPointer(int size, int type, int stride, java.nio.Buffer pointer);
  void glTexCoordPointer(int size, int type, int stride, long pointer_buffer_offset);

}

