package org.lwjgl.glg2d.bridge;

public class Platform {

  private static final char[] NEWLINE = {'\n'};

  public static char[] getNewline() {
    return NEWLINE;
  }

  public static long currentTimeMillis() {
    return System.currentTimeMillis();
  }
}
