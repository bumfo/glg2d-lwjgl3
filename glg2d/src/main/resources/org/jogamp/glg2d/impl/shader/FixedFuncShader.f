#version 330

uniform vec4 u_color;
out vec4 fragColor;

void main() {
  fragColor = u_color;
}