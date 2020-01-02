package org.jogamp.glg2d.gl3.backend;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

final class Backends {
  static GLCapabilities getGlCapabilities(GLConfig config) {
    GLCapabilities caps = config.glCapabilities;
    if (caps == null) {
      GLProfile glProfile = GLProfile.get(config.USE_GL3 ? GLProfile.GL3 : GLProfile.GL2);
      caps = new GLCapabilities(glProfile);
      caps.setRedBits(8);
      caps.setGreenBits(8);
      caps.setBlueBits(8);
      caps.setAlphaBits(8);
      caps.setNumSamples(4);
      caps.setSampleBuffers(true);
      caps.setDoubleBuffered(true);
      caps.setHardwareAccelerated(true);
      // caps.setBackgroundOpaque(false);
    }
    return caps;
  }
}
