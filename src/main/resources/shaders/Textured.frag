#version 450 core

in vec3 ourColor;
in vec2 TexCoord;

out vec4 fragColor;

uniform sampler2D textureSampler;

void main() {
	fragColor = texture(textureSampler,TexCoord) * vec4(ourColor, 1.0);
}