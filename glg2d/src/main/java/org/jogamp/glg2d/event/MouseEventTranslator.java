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
package org.jogamp.glg2d.event;

import static java.lang.Math.abs;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

/**
 * Here is an explanation of terms used to name the multiple, interacting
 * components.
 * 
 * <ul>
 * <li>
 * origin - the component that originally received the event</li>
 * <li>
 * target - the top-most component that is getting redirected events, usually
 * also the top-most component that is being drawn in GLG2D</li>
 * <li>
 * source - the component that will receive the event, always a descendant of
 * the target component</li>
 */
public class MouseEventTranslator {
  protected EventQueue queue;

  protected AffineTransform originToTargetTransform;

  protected Component target;

  protected Component lastUnderCursor;
  protected Component lastSource;
  protected MouseEvent dragging;

  public MouseEventTranslator(Component target) {
    this.target = target;
    queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
    originToTargetTransform = new AffineTransform();
  }

  /**
   * Sets the {@code AffineTransform} that is used to transform a point relative
   * to the origin component into a point relative to the target component.
   */
  public void setOriginToTargetTransform(AffineTransform xform) {
    originToTargetTransform = new AffineTransform(xform);
  }

  /**
   * Gets the {@code AffineTransform} that is used to transform a point relative
   * to the origin component into a point relative to the target component.
   * 
   * <p>
   * Note: the returned {@code AffineTransform} is a copy.
   * </p>
   */
  public AffineTransform getOriginToTargetTransform() {
    return new AffineTransform(originToTargetTransform);
  }

  public void publishMouseEvent(Component underCursor, int id, long when, int modifiers, Point point, int clickCount, int button) {
    Point lastSrcPt = lastSource == null ? point : SwingUtilities.convertPoint(underCursor, point, lastSource);

    boolean published = false;

    // always publish mouse exiting event first
    if (id == MouseEvent.MOUSE_EXITED) {
      publish(new MouseEvent(underCursor, MouseEvent.MOUSE_EXITED, when, modifiers, point.x, point.y, 0, 0,
          clickCount, false, button));
      published = true;
    }

    // take special action if dragging
    if (dragging != null) {
      if (id != MouseEvent.MOUSE_DRAGGED) {
        // not dragging anymore
        publish(new MouseEvent(lastSource, MouseEvent.MOUSE_RELEASED, when, dragging.getModifiers(), lastSrcPt.x, lastSrcPt.y, 0, 0,
            dragging.getClickCount(), false, dragging.getButton()));
      } else if (lastUnderCursor == lastSource && lastUnderCursor != underCursor) {
        // mouse was on the last source, but isn't now
        publish(new MouseEvent(lastSource, MouseEvent.MOUSE_EXITED, when, modifiers, lastSrcPt.x, lastSrcPt.y, 0, 0,
            clickCount, false, button));
      } else if (underCursor == lastSource && lastUnderCursor != underCursor) {
        // mouse is now on the last source, but wasn't before
        publish(new MouseEvent(lastSource, MouseEvent.MOUSE_ENTERED, when, modifiers, lastSrcPt.x, lastSrcPt.y, 0, 0,
            clickCount, false, button));
      }

      published = true;
    }

    // publish to last source component
    if (id == MouseEvent.MOUSE_DRAGGED) {
      MouseEvent e = new MouseEvent(lastSource, MouseEvent.MOUSE_DRAGGED, when, modifiers, lastSrcPt.x, lastSrcPt.y, 0, 0,
          clickCount, false, button);
      publish(e);
      dragging = e;

      published = true;
    } else {
      dragging = null;
    }

    // not dragging, but mouse moved from one over one component to over another
    if (dragging == null && lastSource != null && lastSource != underCursor) {
      publish(new MouseEvent(lastSource, MouseEvent.MOUSE_EXITED, when, modifiers, lastSrcPt.x, lastSrcPt.y, 0, 0,
          clickCount, false, button));

      publish(new MouseEvent(underCursor, MouseEvent.MOUSE_ENTERED, when, modifiers, point.x, point.y, 0, 0,
          clickCount, false, button));
    }

    if (!published) {
      publish(new MouseEvent(underCursor, id, when, modifiers, point.x, point.y, 0, 0, clickCount, false, button));
    }

    assert dragging == null || lastUnderCursor != null;
    assert dragging == null || lastSource != null;
    assert (lastSource == null) == (lastUnderCursor == null);

    lastUnderCursor = underCursor;
    lastSource = id == MouseEvent.MOUSE_DRAGGED ? lastSource : underCursor;
  }

  public void publishMouseEvent(int id, long when, int modifiers, int clickCount, int button, Point clickedOnOrigin) {
    Point clickedOnTarget = originPointToTarget(clickedOnOrigin);

    Component source = getSourceComponent(id, clickedOnTarget);
    if (source == null) {
      return;
    }

    Point clickedOnSource = targetPointToSource(source, clickedOnTarget);

    publishMouseEvent(source, id, when, modifiers, clickedOnSource, clickCount, button);
  }

  public MouseWheelEvent publishMouseWheelEvent(Component source, int id, long when, int modifiers, Point sourcePoint, int clickCount,
      int scrollType, int scrollAmount, int wheelRotation) {
    // TODO how to determine this?
    boolean isPopupTrigger = false;
    MouseWheelEvent e = new MouseWheelEvent(source, id, when, modifiers, sourcePoint.x, sourcePoint.y, 0, 0, clickCount, isPopupTrigger,
        scrollType, scrollAmount, wheelRotation);
    publish(e);
    return e;
  }

  public MouseWheelEvent publishMouseWheelEvent(int id, long when, int modifiers, int wheelRotation, Point mouseOnOrigin) {
    Point clickedOnTarget = originPointToTarget(mouseOnOrigin);

    Component source = getSourceComponent(id, clickedOnTarget);
    if (source == null) {
      return null;
    }

    Point clickedOnSource = targetPointToSource(source, clickedOnTarget);

    return publishMouseWheelEvent(source, id, when, modifiers, clickedOnSource, 0, MouseWheelEvent.WHEEL_UNIT_SCROLL,
        abs(wheelRotation), wheelRotation);
  }

  /**
   * Converts a point that is relative to the origin component into a point that
   * is relative to the target component.
   */
  protected Point originPointToTarget(Point pt) {
    Point2D newPt = originToTargetTransform.transform(pt, new Point());
    return new Point((int) newPt.getX(), (int) newPt.getY());
  }

  /**
   * Converts a point that is relative to the target into a point that is
   * relative to the source component.
   */
  protected Point targetPointToSource(Component source, Point pt) {
    return SwingUtilities.convertPoint(target, pt, source);
  }

  /**
   * Gets the source component at the given target-relative point.
   */
  protected Component getSourceComponent(int eventId, Point pt) {
    return SwingUtilities.getDeepestComponentAt(target, pt.x, pt.y);
  }

  protected void publish(AWTEvent e) {
    queue.postEvent(e);
  }
}
