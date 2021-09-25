package org.lwjgl.glg2d.bridge;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public interface GLMatrixFunc {

  int GL_MATRIX_MODE = 0x0BA0;
  /** Matrix mode modelview */
  int GL_MODELVIEW = 0x1700;
  /** Matrix mode projection */
  int GL_PROJECTION = 0x1701;
  // public static final int GL_TEXTURE = 0x1702; // Use GL.GL_TEXTURE due to ambiguous GL usage
  /** Matrix access name for modelview */
  int GL_MODELVIEW_MATRIX = 0x0BA6;
  /** Matrix access name for projection */
  int GL_PROJECTION_MATRIX = 0x0BA7;
  /** Matrix access name for texture */
  int GL_TEXTURE_MATRIX = 0x0BA8;

  /**
   * Copy the named matrix into the given storage.
   *
   * @param pname  {@link #GL_MODELVIEW_MATRIX}, {@link #GL_PROJECTION_MATRIX} or {@link #GL_TEXTURE_MATRIX}
   * @param params the FloatBuffer's position remains unchanged,
   *               which is the same behavior than the native JOGL GL impl
   */
  void glGetFloatv(int pname, FloatBuffer params);

  /**
   * Copy the named matrix to the given storage at offset.
   *
   * @param pname         {@link #GL_MODELVIEW_MATRIX}, {@link #GL_PROJECTION_MATRIX} or {@link #GL_TEXTURE_MATRIX}
   * @param params        storage
   * @param params_offset storage offset
   */
  void glGetFloatv(int pname, float[] params, int params_offset);

  /**
   * glGetIntegerv
   *
   * @param pname  {@link #GL_MATRIX_MODE} to receive the current matrix mode
   * @param params the FloatBuffer's position remains unchanged
   *               which is the same behavior than the native JOGL GL impl
   */
  void glGetIntegerv(int pname, IntBuffer params);

  void glGetIntegerv(int pname, int[] params, int params_offset);

  /**
   * Sets the current matrix mode.
   *
   * @param mode {@link #GL_MODELVIEW}, {@link #GL_PROJECTION} or {@link GL#GL_TEXTURE GL_TEXTURE}.
   */
  void glMatrixMode(int mode);

  /**
   * Push the current matrix to it's stack, while preserving it's values.
   * <p>
   * There exist one stack per matrix mode, i.e. {@link #GL_MODELVIEW}, {@link #GL_PROJECTION} and {@link GL#GL_TEXTURE GL_TEXTURE}.
   * </p>
   */
  void glPushMatrix();

  /**
   * Pop the current matrix from it's stack.
   *
   * @see #glPushMatrix()
   */
  void glPopMatrix();

  /**
   * Load the current matrix with the identity matrix
   */
  void glLoadIdentity();

  /**
   * Load the current matrix w/ the provided one.
   *
   * @param m the FloatBuffer's position remains unchanged,
   *          which is the same behavior than the native JOGL GL impl
   */
  void glLoadMatrixf(FloatBuffer m);

  /**
   * Load the current matrix w/ the provided one.
   */
  void glLoadMatrixf(float[] m, int m_offset);

  /**
   * Multiply the current matrix: [c] = [c] x [m]
   *
   * @param m the FloatBuffer's position remains unchanged,
   *          which is the same behavior than the native JOGL GL impl
   */
  void glMultMatrixf(FloatBuffer m);

  /**
   * Multiply the current matrix: [c] = [c] x [m]
   */
  void glMultMatrixf(float[] m, int m_offset);

  /**
   * Translate the current matrix.
   */
  void glTranslatef(float x, float y, float z);

  /**
   * Rotate the current matrix.
   */
  void glRotatef(float angle, float x, float y, float z);

  /**
   * Scale the current matrix.
   */
  void glScalef(float x, float y, float z);

  /**
   * {@link #glMultMatrixf(FloatBuffer) Multiply} the current matrix with the orthogonal matrix.
   */
  void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar);

  /**
   * {@link #glMultMatrixf(FloatBuffer) Multiply} the current matrix with the frustum matrix.
   */
  void glFrustumf(float left, float right, float bottom, float top, float zNear, float zFar);

}
