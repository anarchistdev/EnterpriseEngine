#version 150

in vec2 position;

uniform float targetHeight;

const int kerning = 11;
out vec2 blurTexCoords[kerning];

void main(void){
	gl_Position = vec4(position, 0.0, 1.0);
	vec2 centerTexCoords = position * 0.5 + 0.5;

	float pixelSize = 1.0 / targetHeight;

	for (int i = -5; i < 5; i++) {
    	blurTexCoords[i+5] = centerTexCoords + vec2(0.0, pixelSize * i);
    }
}