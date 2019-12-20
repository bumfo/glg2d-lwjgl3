# GLG2D-LWJGL3

(This is a fork that ports [original project](https://github.com/brandonborkholder/glg2d) to LWJGL3.)

GLG2D-LWJGL3 is an effort to translate Graphics2D calls directly into OpenGL calls
and accelerate the Java2D drawing functionality.  The existing OpenGL pipeline
in the Oracle JVM is minimal at best and doesn't use higher-level OpenGL
primitives, like GL_POLYGON and GLU tesselation that make drawing in OpenGL so
fast.

Find more information on https://bumfo.github.com/glg2d-lwjgl3/

Use cases:
 * Port Java2D drawings to OpenGL application

This library is licensed under the Apache 2.0 license and LWJGL3 is licensed and
distributed separately.

## How to build

(Todo)
