package org.jogamp.glg2d.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;

public final class ImageAtlas {
  private static final class Region {
    final String name;
    int sx;
    int sy;
    int w;
    int h;

    private Region(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return "Region{" +
          "name='" + name + '\'' +
          ", sx=" + sx +
          ", sy=" + sy +
          ", w=" + w +
          ", h=" + h +
          '}';
    }
  }

  public static void parse(String filename) throws IOException {
    InputStream stream = ImageAtlas.class.getClassLoader().getResourceAsStream(filename);
    if (stream == null) {
      throw new IOException("Invalid filename: " + filename);
    }
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      try {
        ArrayDeque<Region> stack = new ArrayDeque<>();
        int[] pair = new int[2];

        int skip = 5;
        while (reader.ready()) {
          String line = reader.readLine();
          if (line == null) break;
          if (line.trim().length() == 0) {
            skip = 5;
          } else if (skip > 0) {
            --skip;
            // System.out.println("skipped: " + line);
          } else {
            if (line.startsWith("  ")) {
              String[] split = line.trim().split(":");

              if (split.length == 2) {
                String key = split[0].trim();
                String value = split[1].trim();

                // System.out.println("property: " + key + ": " + value);

                Region region = stack.peekLast();
                if (region == null) throw new IllegalStateException();

                switch (key) {
                  case "xy":
                    parsePair(value, pair);
                    region.sx = pair[0];
                    region.sy = pair[1];
                    break;
                  case "size":
                    parsePair(value, pair);
                    region.w = pair[0];
                    region.h = pair[1];
                    break;
                }
              } else {
                System.out.println("unknown: " + line);
              }
            } else {
              // System.out.println("region: " + line);

              stack.add(new Region(line.trim()));
            }
          }
        }

        System.out.println(stack);
      } finally {
        reader.close();
      }
    } finally {
      stream.close();
    }
  }

  private static void parsePair(String str, int[] pair) {
    String[] values = str.split(",");
    if (values.length == 2) {
      pair[0] = Integer.parseInt(values[0].trim());
      pair[1] = Integer.parseInt(values[1].trim());
    } else {
      throw new IllegalStateException("pair expected");
    }
  }

  public static void main(String[] args) throws IOException {
    parse("robot.atlas");
  }
}
