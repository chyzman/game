#version 450 core

in vec2 pass_uvs;

out vec4 out_Color;

uniform sampler2D textureSampler;

void main(){
	out_Color = vec4(1, 0, 0, 1);//texture(textureSampler,pass_uvs);
}