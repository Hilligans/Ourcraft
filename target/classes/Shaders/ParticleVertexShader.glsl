#version 410
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 texCoord;
layout (location = 2) in float size;
//uniform mat4 transform;

out VertexData {
    vec4 tex;
    float size;
} VertexOut;

void main()
{
    VertexOut.tex = texCoord;
    VertexOut.size = size;
    gl_Position = vec4(aPos, 1.0);
}