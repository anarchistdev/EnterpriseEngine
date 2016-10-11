#version 140

in vec2 texCoords;

out vec4 colorOut;

uniform sampler2D colorTexture;

uniform float contrast;

void main(){
	colorOut = texture(colorTexture, texCoords);
	colorOut.rgb = (colorOut.rgb - 0.5) * (1.0 + contrast) + 0.5;
}