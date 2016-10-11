#version 150

in vec2 texCoords;

out vec4 colorOut;

uniform sampler2D colorTexture;
uniform sampler2D highlightTexture;

uniform float sceneDamper;

void main(void){
    vec4 sceneColor = texture(colorTexture, texCoords);
    vec4 highlightColor = texture(highlightTexture, texCoords);
    colorOut = sceneColor + highlightColor * sceneDamper;
}