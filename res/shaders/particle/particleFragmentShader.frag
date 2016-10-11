#version 140

out vec4 outColor;

in vec2 texCoords1;
in vec2 texCoords2;
in float blendFactor;

uniform sampler2D particleTexture;

void main(){
    vec4 color1 = texture(particleTexture, texCoords1);
    vec4 color2 = texture(particleTexture, texCoords2);

	outColor = mix(color1,  color2, blendFactor);
}