#version 410 core
out vec4 FragColor;
uniform sampler2D ourTexture;
void main()
{
    FragColor = texture(ourTexture, gl_PointCoord);
}