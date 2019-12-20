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

import java.awt.RenderingHints;

/**
 * Assists in the drawing of a particular aspect of the Graphics2D object. This
 * allows the drawing to be segregated into certain aspects, such as image, text
 * or shape drawing.
 */
public interface G2DDrawingHelper {
  /**
   * Sets the current {@code GLGraphics2D} parent. The current {@code GL} and
   * {@code GLContext} objects can be accessed from this. This should clear all
   * internal stacks in the helper object because previous painting iterations
   * may not have called dispose() for each time they called create().
   * 
   * @param g2d
   *          The parent context for subsequent drawing operations.
   */
  void setG2D(GLGraphics2D g2d);

  /**
   * Sets the new {@code GLGraphics2D} context in a stack. This is called when
   * {@code Graphics2D.create()} is called and each helper is given notice to
   * push any necessary information onto the stack. This is used in conjunction
   * with {@link #pop(GLGraphics2D)}.
   * 
   * @param newG2d
   *          The new context, top of the stack.
   */
  void push(GLGraphics2D newG2d);

  /**
   * Sets the new {@code GLGraphics2D} context in a stack after a pop. This is
   * called when {@code Graphics2D.dispose()} is called and each helper is given
   * notice to pop any necessary information off the stack. This is used in
   * conjunction with {@link #push(GLGraphics2D)}.
   * 
   * @param parentG2d
   *          The new context, top of the stack - which is actually the parent
   *          of what was popped.
   */
  void pop(GLGraphics2D parentG2d);

  /**
   * Sets a new rendering hint. The state of all rendering hints is kept by the
   * {@code GLGraphics2D} object, but all new state changes are propagated to
   * all listeners.
   * 
   * @param key
   *          The rendering hint key.
   * @param value
   *          The new hint value.
   */
  void setHint(RenderingHints.Key key, Object value);

  /**
   * Clears all hints back to their default states.
   */
  void resetHints();

  /**
   * Disposes the helper object. This is not called during the dispose operation
   * of the {@code Graphics2D} object. This should dispose all GL resources when
   * all drawing is finished and no more calls will be executing on this OpenGL
   * context and these resources.
   */
  void dispose();
}
