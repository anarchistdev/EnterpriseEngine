#version 400 core

#include <fragmentBase.glsl>

layout(location = 0) out vec4 ColorOut;

uniform sampler2D modelTexture;
uniform sampler2D normalMap;

void main(void) {

    initVars();

    lightFactor = calcLightFactor();

    vec4 normalColor = 2.0 * texture(normalMap, pass_texCoords) - 1.0;
    unitNormal = normalize(normalColor.rgb);
    unitVectorToCamera = normalize(toCameraVector);

    vec4 textureColor = calcTextureColor(modelTexture);
    calcCookTorranceLighting();

    ColorOut = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
    ColorOut = mix(vec4(skyColor, 1.0), ColorOut, visibility);
    ColorOut = toGamma(ColorOut);
}