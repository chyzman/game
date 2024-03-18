#version 330 core

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uModel;

in vec3 aPos;
in vec4 aColor;

out vec4 vColor;

void main() {
    gl_Position = uProjection * uView * uModel * vec4(aPos.xyz, 1.0);
    vColor = aColor;
}