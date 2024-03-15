#version 450 core

uniform mat4 projection;

in vec3 gravity;
in vec2 uvs;

out vec2 pass_uvs;

void main(void){
	gl_Position = projection * vec4(gravity, 1.0);
	pass_uvs = uvs;
}