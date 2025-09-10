#version 330 core

#ifdef OPENGL
in vec4 rgba;
out vec4 FragColor;
#else //VULKAN
layout (location = 0) in vec4 rgba;
layout (location = 0) out vec4 FragColor;
#endif

void main() {
    if(rgba.a < 0.1) {
        discard;
    }
    FragColor = rgba;
}