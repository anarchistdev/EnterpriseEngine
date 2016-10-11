#version 400 core

in vec3 position;
in vec2 texCoords;
in vec3 normal;
in vec3 tangent;

uniform float numberOfRows;
uniform vec2 offset;

#include <vertexBase.glsl>

void main(void) {
    worldPosition = calcWorldPos(position);
    shadowCoords = calcShadowCoords();

    positionRelativeToCam = calcPosRelativeToCam();
    mat4 modelViewMatrix = viewMatrix * transformationMatrix;

    gl_ClipDistance[0] = calcClipDistance();

    gl_Position = calcGLPosition();

    pass_texCoords = calcPassTexCoords(texCoords, numberOfRows, offset);

    surfaceNormal = calcSurfaceNormal(normal);

    mat3 toTangentSpace = calcToTangentSpace(modelViewMatrix, tangent);

    calcLights(toTangentSpace);

    toCameraVector = toTangentSpace * calcToCameraVector();

    visibility = calcVisibility();

    shadowCoords.w = calcShadowCoordW();

}