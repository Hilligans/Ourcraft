#version 330

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
uniform mat4 transform;
uniform vec4 color;
out vec2 Tex;
out vec4 rgba;

void main() {
    gl_Position = transform * vec4(aPos, 1.0);
    Tex = aTexCoord;
    rgba = color;
}
