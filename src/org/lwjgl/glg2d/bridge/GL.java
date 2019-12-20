package org.lwjgl.glg2d.bridge;

public interface GL extends GL2 {
  default GL2 getGL2() {
    return this;
  }
}
