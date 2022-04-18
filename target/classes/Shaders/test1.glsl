#version 420
layout (points) in;
layout (triangle_strip, max_vertices = 4) out;


uniform mat4 transform;


in VertexData {
    vec4 tex;
    float size;
} VertexIn[];

out vec2 texCoord;

void main()
{
    gl_Position = transform * (vec4(-VertexIn[0].size/2.0, -VertexIn[0].size/2.0, 0.0, 0.0) + gl_in[0].gl_Position);
    texCoord = vec2(VertexIn[0].tex.x, VertexIn[0].tex.y);
    EmitVertex();
    gl_Position = transform * (vec4(-VertexIn[0].size/2.0,VertexIn[0].size/2.0, 0.0, 0.0) + gl_in[0].gl_Position);
    texCoord = vec2(VertexIn[0].tex.x, VertexIn[0].tex.w);
    EmitVertex();
    gl_Position = transform * (vec4(VertexIn[0].size/2.0, -VertexIn[0].size/2.0, 0.0, 0.0) + gl_in[0].gl_Position);
    texCoord = vec2(VertexIn[0].tex.z, VertexIn[0].tex.y);
    EmitVertex();
    gl_Position = transform * (vec4(VertexIn[0].size/2.0, VertexIn[0].size/2.0, 0.0, 0.0) + gl_in[0].gl_Position);
    texCoord = vec2(VertexIn[0].tex.z, VertexIn[0].tex.w);
    EmitVertex();
    EndPrimitive();
}