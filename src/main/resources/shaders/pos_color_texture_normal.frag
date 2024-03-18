#version 330 core

uniform sampler2D uTexture;
uniform vec3 lightPos;
uniform vec3 viewPos;
uniform bool blinn;

in vec3 vPos;
in vec4 vColor;
in vec2 vTexCoord;
in vec3 vNormal;

out vec4 fragColor;

void main()
{
    vec3 color = texture(uTexture, vTexCoord).rgb;
    // ambient
    vec3 ambient = 0.05 * color;
    // diffuse
    vec3 lightDir = normalize(lightPos - vPos);
    vec3 normal = normalize(vNormal);
    float diff = max(dot(lightDir, normal), 0.0);
    vec3 diffuse = diff * color;
    // specular
    vec3 viewDir = normalize(viewPos - vPos);
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = 0.0;
    if(blinn)
    {
        vec3 halfwayDir = normalize(lightDir + viewDir);
        spec = pow(max(dot(normal, halfwayDir), 0.0), 32.0);
    }
    else
    {
        vec3 reflectDir = reflect(-lightDir, normal);
        spec = pow(max(dot(viewDir, reflectDir), 0.0), 8.0);
    }
    vec3 specular = vec3(0.3) * spec; // assuming bright white light color
    fragColor = vColor * vec4(ambient + diffuse + specular, 1.0);
}