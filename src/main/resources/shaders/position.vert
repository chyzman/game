#version 330 core

uniform mat4 uProjection;

in vec3 aPos;

void main() {
    gl_Position = uProjection * vec4(aPos, 1.0);
}