/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.lwjgl.glg2d.bridge;

/**
 * Utility and fast math functions.
 * <p>
 * Thanks to Riven on JavaGaming.org for the basis of sin/cos/floor/ceil.
 *
 * @author Nathan Sweet
 */
public final class MathUtils {

  // ---
  static public final float FLOAT_ROUNDING_ERROR = 0.000001f; // 32 bits
  static public final float PI = 3.1415927f;

  static private final int SIN_BITS = 14; // 16KB. Adjust for accuracy.
  static private final int SIN_MASK = ~(-1 << SIN_BITS);
  static private final int SIN_COUNT = SIN_MASK + 1;

  static private final float radFull = PI * 2;
  static private final float degFull = 360;
  static private final float degToIndex = SIN_COUNT / degFull;

  /**
   * multiply by this to convert from radians to degrees
   */
  static public final float radiansToDegrees = 180f / PI;
  /**
   * multiply by this to convert from degrees to radians
   */
  static public final float degreesToRadians = PI / 180;

  static private class Sin {
    static final float[] table = new float[SIN_COUNT];

    static {
      for (int i = 0; i < SIN_COUNT; i++)
        table[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * radFull);
      for (int i = 0; i < 360; i += 90)
        table[(int) (i * degToIndex) & SIN_MASK] = (float) Math.sin(i * degreesToRadians);
    }
  }

  /**
   * Returns the sine in radians from a lookup table.
   */
  static public float sinDeg(float degrees) {
    return Sin.table[(int) (degrees * degToIndex) & SIN_MASK];
  }

  /**
   * Returns the cosine in radians from a lookup table.
   */
  static public float cosDeg(float degrees) {
    return Sin.table[(int) ((degrees + 90) * degToIndex) & SIN_MASK];
  }

  /**
   * Returns true if the value is zero (using the default tolerance as upper bound)
   */
  static public boolean isZero(float value) {
    return Math.abs(value) <= FLOAT_ROUNDING_ERROR;
  }

  /** Returns true if the value is zero.
   * @param tolerance represent an upper bound below which the value is considered zero. */
  static public boolean isZero (float value, float tolerance) {
    return Math.abs(value) <= tolerance;
  }

  /**
   * Returns true if a is nearly equal to b. The function uses the default floating error tolerance.
   *
   * @param a the first value.
   * @param b the second value.
   */
  static public boolean isEqual(float a, float b) {
    return Math.abs(a - b) <= FLOAT_ROUNDING_ERROR;
  }

}
