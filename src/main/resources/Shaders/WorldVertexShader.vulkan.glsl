#version 450

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 rgb;
layout (location = 2) in vec2 aTexCoord;

layout(binding = 0) uniform UniformBufferObject {
   mat4 transform;
   vec4 color;
} ubo;

layout (location = 0) out vec2 Tex;
layout (location = 1) out vec4 rgba;

void main() {
   gl_Position = ubo.transform * vec4(aPos, 1.0);
   Tex = aTexCoord;
   rgba = ubo.color * rgb;
}