#version 410
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 texCoord;
uniform mat4 transform;

out vec2 tex;

void main()
{
    tex = texCoord;
    gl_Position = transform * vec4(aPos, 1.0);
}