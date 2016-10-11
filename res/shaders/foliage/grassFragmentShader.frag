#version 400 core

#include <fragmentBase.glsl>

out vec4 ColorOut;

uniform sampler2D text;

void main(void) {

    initVars();

    setAmbiance(1.35);

    lightFactor = calcLightFactor();

    calcStandardLighting();

    vec4 textureColor = calcTextureColor(text);

    ColorOut = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
    ColorOut = mix(vec4(skyColor, 1.0), ColorOut, visibility);
}