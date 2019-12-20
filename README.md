# GLG2D-LWJGL3

(This is a fork that ports [original project](https://github.com/brandonborkholder/glg2d) to LWJGL3.)

GLG2D-LWJGL3 is an effort to translate Graphics2D calls directly into OpenGL calls
and accelerate the Java2D drawing functionality.  The existing OpenGL pipeline
in the Oracle JVM is minimal at best and doesn't use higher-level OpenGL
primitives, like GL_POLYGON and GLU tesselation that make drawing in OpenGL so
fast.

Find more information on https://bumfo.github.com/glg2d-lwjgl3/

Use cases:
 * use as a drop-in replacement for a JPanel and all Swing children will be
    accelerated
 * draw Swing components in an GLCanvas in your existing application

This library is licensed under the Apache 2.0 license and JOGL is licensed and
distributed separately.

How to build

This project uses maven, run mvn package to build the jar in the ./target/ dir
or add the following to your pom.xml
<dependency>
 <groupId>org.jogamp.glg2d</groupId>
 <artifactId>glg2d-lwjgl3</artifactId>
 <version>${glg2d_lwjgl3.version}</version>
</dependency>

Make sure you also add the GLG2D repository at
https://bumfo.github.com/glg2d-lwjgl3/maven2/

