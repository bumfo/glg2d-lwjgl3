#version 330

uniform sampler2D u_tex;
uniform vec4 u_color;

in vec2 v_texCoord;
out vec4 fragColor;

void main() {
  vec4 texel;

  texel = texture(u_tex, v_texCoord);
  fragColor = vec4(u_color.rgb * texel.rgb, texel.a);
//  fragColor = vec4(1, 1, 1, 1);
}