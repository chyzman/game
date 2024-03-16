#version 330 core

uniform mat4 projection;

in vec3 aPos;

void main() {
    gl_Position = projection * vec4(aPos, 1.0);
}