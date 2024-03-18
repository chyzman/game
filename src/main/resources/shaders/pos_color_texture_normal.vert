#version 330 core
in vec3 aPos;
in vec4 aColor;
in vec3 aNormal;
in vec2 aTexCoords;

out VS_OUT {
    vec3 vPos;
    vec4 vColor;
    vec3 vNormal;
    vec2 vTexCoords;
} vs_out;

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uModel;

void main()
{
    vs_out.vPos = aPos;
    vs_out.vColor = aColor;
    vs_out.vNormal = aNormal;
    vs_out.vTexCoords = aTexCoords;
    gl_Position = uProjection * uView * uModel * vec4(aPos, 1.0);
}