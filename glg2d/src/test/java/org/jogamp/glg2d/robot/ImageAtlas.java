package org.jogamp.glg2d.robot;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ImageAtlas {
  public static final class Region {
    private int sx;
    private int sy;
    private int w;
    private int h;

    @Override
    public String toString() {
      return "Region{" +
          "sx=" + sx +
          ", sy=" + sy +
          ", w=" + w +
          ", h=" + h +
          '}';
    }

    public RenderImageRegion toImageRegion(Image img, double scale) {
      return new RenderImageRegion(img, sx, sy, w, h, scale);
    }
  }

  private final Map<String, Region> map;

  private ImageAtlas(Map<String, Region> map) {
    this.map = map;
  }

  public Region findRegion(String name) {
    return map.get(name);
  }

  @Override
  public String toString() {
    return "ImageAtlas" + map;
  }

  @SuppressWarnings({"resource", "TryFinallyCanBeTryWithResources", "IOResourceOpenedButNotSafelyClosed"})
  public static ImageAtlas parse(String filename) throws IOException {
    InputStream stream = ImageAtlas.class.getClassLoader().getResourceAsStream(filename);
    if (stream == null) {
      throw new IOException("Invalid filename: " + filename);
    }
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      try {
        Map<String, Region> map = new LinkedHashMap<>();
        Region last = null;
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

                Region region = last;
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

              map.put(line.trim(), last = new Region());
            }
          }
        }

        return new ImageAtlas(map);
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
    ImageAtlas atlas = parse("robot.atlas");

    System.out.println(atlas);
  }
}
