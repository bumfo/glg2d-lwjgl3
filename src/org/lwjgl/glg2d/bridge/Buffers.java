/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved.
 * Copyright (c) 2010 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 *
 * Sun gratefully acknowledges that this software was originally authored
 * and developed by Kenneth Bradley Russell and Christopher John Kline.
 */
package org.lwjgl.glg2d.bridge;


import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/**
 * Utility methods allowing easy {@link Buffer} manipulations.
 *
 * @author Kenneth Russel
 * @author Sven Gothel
 * @author Michael Bien
 */
public class Buffers {

  public static final int SIZEOF_SHORT = 2;
  public static final int SIZEOF_CHAR = 2;
  public static final int SIZEOF_INT = 4;
  public static final int SIZEOF_FLOAT = 4;
  public static final int SIZEOF_LONG = 8;
  public static final int SIZEOF_DOUBLE = 8;

  /**
   * Allocates a new direct ByteBuffer with the specified number of
   * elements. The returned buffer will have its byte order set to
   * the host platform's native byte order.
   */
  public static ByteBuffer newDirectByteBuffer(final int numElements) {
    return nativeOrder(ByteBuffer.allocateDirect(numElements));
  }

  public static ByteBuffer newDirectByteBuffer(final byte[] values, final int offset, final int length) {
    return (ByteBuffer) newDirectByteBuffer(length).put(values, offset, length).rewind();
  }

  /**
   * Allocates a new direct DoubleBuffer with the specified number of
   * elements. The returned buffer will have its byte order set to
   * the host platform's native byte order.
   */
  public static DoubleBuffer newDirectDoubleBuffer(final int numElements) {
    return newDirectByteBuffer(numElements * SIZEOF_DOUBLE).asDoubleBuffer();
  }

  public static DoubleBuffer newDirectDoubleBuffer(final double[] values, final int offset, final int length) {
    return (DoubleBuffer) newDirectDoubleBuffer(length).put(values, offset, length).rewind();
  }

  /**
   * Allocates a new direct FloatBuffer with the specified number of
   * elements. The returned buffer will have its byte order set to
   * the host platform's native byte order.
   */
  public static FloatBuffer newDirectFloatBuffer(final int numElements) {
    return newDirectByteBuffer(numElements * SIZEOF_FLOAT).asFloatBuffer();
  }

  public static FloatBuffer newDirectFloatBuffer(final float[] values, final int offset, final int length) {
    return (FloatBuffer) newDirectFloatBuffer(length).put(values, offset, length).rewind();
  }

  /**
   * Allocates a new direct IntBuffer with the specified number of
   * elements. The returned buffer will have its byte order set to
   * the host platform's native byte order.
   */
  public static IntBuffer newDirectIntBuffer(final int numElements) {
    return newDirectByteBuffer(numElements * SIZEOF_INT).asIntBuffer();
  }

  public static IntBuffer newDirectIntBuffer(final int[] values, final int offset, final int length) {
    return (IntBuffer) newDirectIntBuffer(length).put(values, offset, length).rewind();
  }

  /**
   * Allocates a new direct LongBuffer with the specified number of
   * elements. The returned buffer will have its byte order set to
   * the host platform's native byte order.
   */
  public static LongBuffer newDirectLongBuffer(final int numElements) {
    return newDirectByteBuffer(numElements * SIZEOF_LONG).asLongBuffer();
  }

  public static LongBuffer newDirectLongBuffer(final long[] values, final int offset, final int length) {
    return (LongBuffer) newDirectLongBuffer(length).put(values, offset, length).rewind();
  }

  /**
   * Allocates a new direct ShortBuffer with the specified number of
   * elements. The returned buffer will have its byte order set to
   * the host platform's native byte order.
   */
  public static ShortBuffer newDirectShortBuffer(final int numElements) {
    return newDirectByteBuffer(numElements * SIZEOF_SHORT).asShortBuffer();
  }

  public static ShortBuffer newDirectShortBuffer(final short[] values, final int offset, final int length) {
    return (ShortBuffer) newDirectShortBuffer(length).put(values, offset, length).rewind();
  }

  /**
   * Allocates a new direct CharBuffer with the specified number of
   * elements. The returned buffer will have its byte order set to
   * the host platform's native byte order.
   */
  public static CharBuffer newDirectCharBuffer(final int numElements) {
    return newDirectByteBuffer(numElements * SIZEOF_SHORT).asCharBuffer();
  }

  public static CharBuffer newDirectCharBuffer(final char[] values, final int offset, final int length) {
    return (CharBuffer) newDirectCharBuffer(length).put(values, offset, length).rewind();
  }


  /**
   * Helper routine to set a ByteBuffer to the native byte order, if
   * that operation is supported by the underlying NIO
   * implementation.
   */
  public static ByteBuffer nativeOrder(final ByteBuffer buf) {
    return buf.order(ByteOrder.nativeOrder());
  }


  //----------------------------------------------------------------------
  // Copy routines (type-to-type)
  //

  //----------------------------------------------------------------------
  // Copy routines (type-to-ByteBuffer)
  //

  //----------------------------------------------------------------------
  // Conversion routines
  //


  //----------------------------------------------------------------------
  // Convenient put methods with generic target Buffer w/o value range conversion, i.e. normalization
  //

  //----------------------------------------------------------------------
  // Convenient put methods with generic target Buffer and value range conversion, i.e. normalization
  //

  //----------------------------------------------------------------------
  // Range check methods
  //

}
