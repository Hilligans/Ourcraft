#version 120
layout (points) in;
layout (triangle_strip, max_vertices = 4) out;
in vec4 texOut[1];
in mat4 transformOut[1];
in float sizeOut [1];
out vec2 Tex;
void main() {
    gl_Position = (gl_in[0].gl_Position + vec4(-size[0]/2, -size[0]/2, 0.0, 0.0)) * transform[0];
    Tex = vec2(tex[0].x, tex[0].y);
    EmitVertex();
    gl_Position = (gl_in[0].gl_Position + vec4(-size[0]/2, size[0]/2, 0.0, 0.0)) * transform[0];
    Tex = vec2(tex[0].x, tex[0].w);
    EmitVertex();
    gl_Position = (gl_in[0].gl_Position + vec4(size[0]/2, -size[0]/2, 0.0, 0.0)) * transform[0];
    Tex = vec2(tex[0].z, tex[0].y);
    EmitVertex();
    gl_Position = (gl_in[0].gl_Position + vec4(size[0]/2, size[0]/2, 0.0, 0.0)) * transform[0];
    Tex = vec2(tex[0].z, tex[0].w);
    EmitVertex();
}