#version 330 core
in vec3 aPos;
in vec4 aColor;
in vec2 aTexCoords;
in vec3 aNormal;

out vec3 vPos;
out vec4 vColor;
out vec2 vTexCoords;
out vec3 vNormal;

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uModel;

void main()
{
    vPos = aPos;
    vColor = aColor;
    vNormal = aNormal;
    vTexCoords = aTexCoords;
    gl_Position = uProjection * uView * uModel * vec4(aPos, 1.0);
}