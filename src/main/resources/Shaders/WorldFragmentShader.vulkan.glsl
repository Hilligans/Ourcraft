#version 450 core

layout (location = 0) in vec2 Tex;
layout (location = 1) in vec4 rgba;

layout(binding = 1) uniform sampler2D texSampler;

layout (location = 0) out vec4 FragColor;

void main() {
   vec4 texColor = texture(texSampler, Tex);
   if(texColor.a < 0.1) {
       discard;
   }
   FragColor = texColor * rgba;
}