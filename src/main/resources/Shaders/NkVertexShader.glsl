#version 150

#ifdef OPENGL
uniform mat4 transform;
in vec2 Position;
in vec2 TexCoord;
in vec4 Color;

out vec2 Frag_UV;
out vec4 Frag_Color;

#define xTransform transform
#else
layout(binding = 0) uniform UniformBufferObject {
    mat4 transform;
} ubo;

layout (location = 0) in vec2 Position;
layout (location = 1) in vec2 TexCoord;
layout (location = 2) in vec4 Color;

layout (location = 0) out vec2 Frag_UV;
layout (location = 1) out vec4 Frag_Color;

#define xTransform ubo.transform
#endif


void main() {
    Frag_UV = TexCoord;
    Frag_Color = Color;
    gl_Position = xTransform * vec4(Position.xy, 0, 1);
}