#version 450 core

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uModel;

in vec3 aPos;
in vec4 aColor;
in vec2 aTexCoord;

out vec4 ourColor;
out vec2 TexCoord;

void main(void){
	gl_Position = uProjection * uView * uModel * vec4(aPos, 1.0);
	ourColor = aColor;
	TexCoord = aTexCoord;
}