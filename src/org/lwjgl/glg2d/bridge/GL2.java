package org.lwjgl.glg2d.bridge;

public interface GL2 extends GL20 {

  /** Entry point to C language function: <code> void {@native glCopyPixels}(GLint x, GLint y, GLsizei width, GLsizei height, GLenum type) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  void glCopyPixels(int x, int y, int width, int height, int type);

  // /** Entry point to C language function: <code> void {@native glRasterPos2d}(GLdouble x, GLdouble y) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos2d(double x, double y);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos2f}(GLfloat x, GLfloat y) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos2f(float x, float y);

  /** Entry point to C language function: <code> void {@native glRasterPos2i}(GLint x, GLint y) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  void glRasterPos2i(int x, int y);

  // /** Entry point to C language function: <code> void {@native glRasterPos2s}(GLshort x, GLshort y) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos2s(short x, short y);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3d}(GLdouble x, GLdouble y, GLdouble z) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos3d(double x, double y, double z);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3f}(GLfloat x, GLfloat y, GLfloat z) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos3f(float x, float y, float z);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3i}(GLint x, GLint y, GLint z) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos3i(int x, int y, int z);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3s}(GLshort x, GLshort y, GLshort z) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos3s(short x, short y, short z);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4d}(GLdouble x, GLdouble y, GLdouble z, GLdouble w) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos4d(double x, double y, double z, double w);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4f}(GLfloat x, GLfloat y, GLfloat z, GLfloat w) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos4f(float x, float y, float z, float w);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4i}(GLint x, GLint y, GLint z, GLint w) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos4i(int x, int y, int z, int w);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4s}(GLshort x, GLshort y, GLshort z, GLshort w) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos4s(short x, short y, short z, short w);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos2dv}(const GLdouble *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.DoubleBuffer}   */
  // public void glRasterPos2dv(DoubleBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos2dv}(const GLdouble *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos2dv(double[] v, int v_offset);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos2fv}(const GLfloat *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.FloatBuffer}   */
  // public void glRasterPos2fv(FloatBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos2fv}(const GLfloat *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos2fv(float[] v, int v_offset);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos2iv}(const GLint *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.IntBuffer}   */
  // public void glRasterPos2iv(IntBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos2iv}(const GLint *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos2iv(int[] v, int v_offset);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos2sv}(const GLshort *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.ShortBuffer}   */
  // public void glRasterPos2sv(ShortBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos2sv}(const GLshort *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos2sv(short[] v, int v_offset);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3dv}(const GLdouble *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.DoubleBuffer}   */
  // public void glRasterPos3dv(DoubleBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3dv}(const GLdouble *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos3dv(double[] v, int v_offset);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3fv}(const GLfloat *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.FloatBuffer}   */
  // public void glRasterPos3fv(FloatBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3fv}(const GLfloat *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos3fv(float[] v, int v_offset);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3iv}(const GLint *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.IntBuffer}   */
  // public void glRasterPos3iv(IntBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3iv}(const GLint *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos3iv(int[] v, int v_offset);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3sv}(const GLshort *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.ShortBuffer}   */
  // public void glRasterPos3sv(ShortBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos3sv}(const GLshort *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos3sv(short[] v, int v_offset);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4dv}(const GLdouble *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.DoubleBuffer}   */
  // public void glRasterPos4dv(DoubleBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4dv}(const GLdouble *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos4dv(double[] v, int v_offset);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4fv}(const GLfloat *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.FloatBuffer}   */
  // public void glRasterPos4fv(FloatBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4fv}(const GLfloat *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos4fv(float[] v, int v_offset);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4iv}(const GLint *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.IntBuffer}   */
  // public void glRasterPos4iv(IntBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4iv}(const GLint *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos4iv(int[] v, int v_offset);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4sv}(const GLshort *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>
  //  @param v a direct or array-backed {@link java.nio.ShortBuffer}   */
  // public void glRasterPos4sv(ShortBuffer v);
  //
  // /** Entry point to C language function: <code> void {@native glRasterPos4sv}(const GLshort *  v) </code> <br>Part of <code>GL_VERSION_1_0</code><br>   */
  // public void glRasterPos4sv(short[] v, int v_offset);

}
