#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;

#ifdef OPENGL
uniform mat4 transform;
uniform vec4 color;
out vec2 Tex;
out vec4 rgba;

#define xTransform transform
#define xColor color
#else //VULKAN
layout(binding = 0) uniform UniformBufferObject {
   mat4 transform;
   vec4 color;
} ubo;

#define xTransform ubo.transform
#define xColor ubo.color

layout (location = 0) out vec2 Tex;
layout (location = 1) out vec4 rgba;
#endif

void main() {
   gl_Position = xTransform * vec4(aPos, 1.0);
   Tex = aTexCoord;
   rgba = xColor;
}