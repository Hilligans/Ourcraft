#version 330 core

#ifdef OPENGL
in vec2 Tex;
in vec4 rgba;
uniform sampler2D texSampler;
out vec4 FragColor;
#else //VULKAN
layout (location = 0) in vec2 Tex;
layout (location = 1) in vec4 rgba;
layout(binding = 1) uniform sampler2D texSampler;
layout (location = 0) out vec4 FragColor;
#endif

void main() {
   vec4 texColor = texture(texSampler, Tex);
   if(texColor.a < 0.1) {
       discard;
   }
   FragColor = texColor * rgba;
}