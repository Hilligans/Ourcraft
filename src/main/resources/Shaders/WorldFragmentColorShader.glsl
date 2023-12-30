#version 330 core
out vec4 FragColor;
in vec4 rgba;
void main() {
   if(rgba.a < 0.1) {
       discard;
   }
   FragColor = rgba;
}