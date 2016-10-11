#version 400 core

in vec3 position;
in vec2 texCoords;
in vec3 normal;


#include <vertexBase.glsl>

void main(void) {
    worldPosition = calcWorldPos(position);
    shadowCoords = calcShadowCoords();

    positionRelativeToCam = calcPosRelativeToCam();

    gl_ClipDistance[0] = calcClipDistance();

    gl_Position =  calcGLPosition();
    pass_texCoords = texCoords;

    surfaceNormal = calcSurfaceNormal(normal);

    calcLights();

    toCameraVector = calcToCameraVector();

    calcVisibility();

    shadowCoords.w = calcShadowCoordW();
}