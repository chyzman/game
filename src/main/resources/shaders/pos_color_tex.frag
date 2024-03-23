#version 450 core

in vec4 ourColor;
in vec2 TexCoord;

out vec4 fragColor;

uniform sampler2D textureSampler;

void main() {
	fragColor = texture(textureSampler,TexCoord) * ourColor;
}