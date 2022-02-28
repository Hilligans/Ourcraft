#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 2) in vec2 aTexCoord;
layout (location = 1) in int rgb;

uniform mat4 transform;
uniform vec4 color;
out vec2 Tex;
out vec4 rgba;

void main() {
    gl_Position = transform * vec4(aPos, 1.0);
    Tex = aTexCoord;

    vec4 rgbaIn = vec4(255 / (rgb & 0xFF), 255 / ((rgb >> 8) & 0xFF), 255 / ((rgb >> 16) & 0xFF), 255 / ((rgb >> 24) & 0xFF));

    rgba = color * rgbaIn;
}