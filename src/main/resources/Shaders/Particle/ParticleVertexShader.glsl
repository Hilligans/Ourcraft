#version 410

uniform mat4 modelview;
uniform mat4 projection;
uniform vec2 screenSize;

layout(location = 0) in vec4 position;
layout(location = 1) in float size;

void main()
{
    vec4 eyePos = modelview * position;
    vec4 projVoxel = projection * vec4(size,size,eyePos.z,eyePos.w);
    vec2 projSize = screenSize * projVoxel.xy / projVoxel.w;
    //gl_PointSize = 0.25 * (projSize.x+projSize.y);
    gl_PointSize = 100;
    gl_Position = projection * eyePos;
}