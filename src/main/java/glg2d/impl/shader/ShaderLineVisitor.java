/**************************************************************************
   Copyright 2012 Brandon Borkholder

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 ***************************************************************************/

package glg2d.impl.shader;

import glg2d.SimplePathVisitor;
import glg2d.VertexBuffer;

import java.awt.BasicStroke;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;

public class ShaderLineVisitor extends SimplePathVisitor implements ShaderPathVisitor {
  protected VertexBuffer buffer = VertexBuffer.getSharedBuffer();

  protected BasicStroke stroke;
  protected float[] color;
  protected FloatBuffer matrixBuffer;

  protected float[] lastV = new float[2];

  protected GL2ES2 gl;

  protected GL2ES2StrokeLinePipeline pipeline;

  @Override
  public void setGLContext(GL context) {
    gl = context.getGL2ES2();
    if (pipeline != null) {
      pipeline.delete(gl);
    }

    // if (pipeline == null) {
    pipeline = new GL2ES2StrokeLinePipeline();
    pipeline.setup(gl);
    // }
  }

  @Override
  public void setColor(float[] rgba) {
    color = rgba;
  }

  @Override
  public void setTransform(FloatBuffer glMatrixBuffer) {
    matrixBuffer = glMatrixBuffer;
  }

  @Override
  public void setStroke(BasicStroke stroke) {
    this.stroke = stroke;
  }

  @Override
  public void moveTo(float[] vertex) {
    draw(false);

    lastV[0] = vertex[0];
    lastV[1] = vertex[1];
  }

  @Override
  public void lineTo(float[] vertex) {
    // no 0-length lines
    if (vertex[0] == lastV[0] && vertex[1] == lastV[1]) {
      return;
    }

    buffer.addVertex(vertex, 0, 1);
    lastV[0] = vertex[0];
    lastV[1] = vertex[1];
  }

  @Override
  public void closeLine() {
    /*
     * Sometimes shapes will set the last point to be the same as the first and
     * then close the line. That can be confusing. So we discard the last point
     * if it's the same as the first. Now no 2 consecutive points are the same.
     */
    FloatBuffer buf = buffer.getBuffer();
    if (buf.get(0) == lastV[0] && buf.get(1) == lastV[1]) {
      buf.position(buf.position() - 2);
    }

    draw(true);
  }

  @Override
  public void beginPoly(int windingRule) {
    pipeline.use(gl, true);

    pipeline.setColor(gl, color);
    pipeline.setTransform(gl, matrixBuffer);
    pipeline.setStroke(gl, stroke);

    buffer.clear();
  }

  @Override
  public void endPoly() {
    draw(false);
    pipeline.use(gl, false);
  }

  protected void draw(boolean close) {
    FloatBuffer buf = buffer.getBuffer();
    if (buf.position() == 0) {
      return;
    }

    buf.flip();
    pipeline.draw(gl, buf, close);

    buffer.clear();
  }
}
