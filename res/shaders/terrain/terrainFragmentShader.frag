#version 400 core

#include <fragmentBase.glsl>

layout(location = 0) out vec4 colorOut;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

void main(void) {
	//lightFactor = sampleShadowPCF(shadowMap, newCoords.xy, newCoords.z - 2.0, vec2(1/4096, 1/4096));

	vec4 blendMapColor = texture(blendMap, pass_texCoords);
	
	float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	vec2 tiledCoords = pass_texCoords * 40;
	vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoords) * backTextureAmount;
	vec4 rTextureColor = texture(rTexture,tiledCoords) * blendMapColor.r;
	vec4 gTextureColor = texture(gTexture,tiledCoords) * blendMapColor.g;
	vec4 bTextureColor = texture(bTexture,tiledCoords) * blendMapColor.b;
	
	vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;
	totalColor = toLinear(totalColor);

	initVars();
	lightFactor = calcLightFactor();

	calcCookTorranceLighting();
	
	
    colorOut = vec4(totalDiffuse,1.0) *  totalColor + vec4(totalSpecular,1.0);
    colorOut = mix(vec4(skyColor,1.0),colorOut, visibility);

    colorOut = toGamma(colorOut);

}