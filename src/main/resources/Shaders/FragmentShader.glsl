#version 330

out vec4 FragColor;
in vec2 Tex;
in vec4 rgba;
uniform sampler2D ourTexture;

void main() {
    vec4 texColor = texture(ourTexture, Tex);
    if(texColor.a < 0.1) {
        discard;
    }
    FragColor = texColor * rgba;
}
