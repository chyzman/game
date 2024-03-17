#version 330 core

uniform mat4 uProjection;

in vec3 aPos;
in vec4 aColor;
in vec2 aTexCoord;

out vec4 vColor;
out vec2 vTexCoord;

void main() {
    gl_Position = uProjection * vec4(aPos.xyz, 1.0);
    vColor = aColor;
}