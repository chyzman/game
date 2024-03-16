#version 450 core

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 uvs;

out vec2 pass_uvs;

void main(void){
	gl_Position = projection * view * model * vec4(position, 1.0);
	pass_uvs = uvs;
}