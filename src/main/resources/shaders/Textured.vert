#version 450 core

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

in vec3 aPos;
in vec4 aColor;
in vec2 aTexCoord;

out vec4 ourColor;
out vec2 TexCoord;

void main(void){
	gl_Position = projection * view * model * vec4(aPos, 1.0);
	ourColor = aColor;
	TexCoord = aTexCoord;
}