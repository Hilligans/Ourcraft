#version 150
precision mediump float;

#ifdef OPENGL
uniform sampler2D Texture;
in vec2 Frag_UV;
in vec4 Frag_Color;

out vec4 Out_Color;
#else
layout(binding = 1) uniform sampler2D Texture;
layout (location = 0) in vec2 Frag_UV;
layout (location = 1) in vec4 Frag_Color;

layout (location = 0) out vec4 Out_Color;
#endif

void main() {
    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);
}