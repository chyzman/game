#version 330 core

uniform mat4 uProjection;

in vec3 aPos;
in vec4 aColor;

out vec4 vColor;

void main() {
    gl_Position = uProjection * vec4(aPos.xyz, 1.0);
    vColor = aColor;
}