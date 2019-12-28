/**
 * Copyright (c) 2001-2019 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://robocode.sourceforge.io/license/epl-v10.html
 */
package org.jogamp.glg2d.robot;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;


/**
 * An image that can be rendered.
 *
 * @author Flemming N. Larsen (original)
 */
public class RenderImageRegion extends RenderObject {

  /**
   * Image
   */
  protected final Image image;
  protected final int sx;
  protected final int sy;
  protected final int w;
  protected final int h;
  protected final double scale;

  /**
   * Area containing the bounds of the image to paint
   */
  protected final Area boundArea;

  /**
   * Constructs a new {@code RenderImageRegion}, which has it's origin in the center
   * of the image.
   *
   * @param image the image to be rendered
   */
  public RenderImageRegion(Image image, int sx, int sy, int w, int h, double scale) {
    this(image, sx, sy, w, h, scale, .5 * w, .5 * h);
  }

  /**
   * Constructs a new {@code RenderImageRegion}
   *
   * @param image   the image to be rendered
   * @param originX the x coordinate of the origin for the rendered image
   * @param originY the y coordinate of the origin for the rendered image
   */
  public RenderImageRegion(Image image, int sx, int sy, int w, int h, double scale, double originX, double originY) {
    super();

    this.image = image;
    this.sx = sx;
    this.sy = sy;
    this.w = w;
    this.h = h;
    this.scale = scale;

    AffineTransform Tx = new AffineTransform();
    Tx.scale(scale, scale);
    Tx.translate(-originX, -originY);
    baseTransform = Tx;

    boundArea = new Area(new Rectangle(0, 0, w, h));
  }

  /**
   * Constructs a new {@code RenderImageRegion} that is a copy of another
   * {@code RenderImageRegion}.
   *
   * @param ri the {@code RenderImageRegion} to copy
   */
  public RenderImageRegion(RenderImageRegion ri) {
    super(ri);

    image = ri.image;

    boundArea = new Area(ri.boundArea);

    sx = ri.sx;
    sy = ri.sy;
    w = ri.w;
    h = ri.h;
    scale = ri.scale;
  }

  @Override
  public void paint(Graphics2D g) {
    AffineTransform saved = g.getTransform();
    g.transform(transform);
    g.drawImage(image, 0, 0, w, h, sx, sy, sx + w, sy + h, null);
    g.setTransform(saved);
  }

  @Override
  public Rectangle getBounds() {
    return boundArea.createTransformedArea(transform).getBounds();
  }

  @Override
  public RenderObject copy() {
    return new RenderImageRegion(this);
  }
}
