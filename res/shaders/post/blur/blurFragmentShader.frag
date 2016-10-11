#version 150

out vec4 colorOut;

uniform sampler2D originalTexture;

in vec2 blurTexCoords[11];

void main(){
	colorOut = vec4(0.0);
	colorOut += texture(originalTexture, blurTexCoords[0]) * 0.0093;
    colorOut += texture(originalTexture, blurTexCoords[1]) * 0.028002;
    colorOut += texture(originalTexture, blurTexCoords[2]) * 0.065984;
    colorOut += texture(originalTexture, blurTexCoords[3]) * 0.121703;
    colorOut += texture(originalTexture, blurTexCoords[4]) * 0.175713;
    colorOut += texture(originalTexture, blurTexCoords[5]) * 0.198596;
    colorOut += texture(originalTexture, blurTexCoords[6]) * 0.175713;
    colorOut += texture(originalTexture, blurTexCoords[7]) * 0.121703;
    colorOut += texture(originalTexture, blurTexCoords[8]) * 0.065984;
    colorOut += texture(originalTexture, blurTexCoords[9]) * 0.028002;
    colorOut += texture(originalTexture, blurTexCoords[10]) * 0.0093;
}