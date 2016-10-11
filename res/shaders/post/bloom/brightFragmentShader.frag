#version 150

in vec2 texCoords;

out vec4 colorOut;

uniform sampler2D colorTexture;

const float gamma = 2.2;
float exposure = 2.0;

void main(){
    vec3 color = texture(colorTexture, texCoords).rgb;
    float brightness = (color.r * 0.2126) + (color.g * 0.7152) + (color.b * 0.0722);

    vec3 result = vec3(1.0) - exp(-color * exposure);
    result = pow(result, vec3(1.0 / gamma));

    colorOut = vec4(result, 1.0);
}