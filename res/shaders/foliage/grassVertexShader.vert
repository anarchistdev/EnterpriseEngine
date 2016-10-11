#version 400 core

in vec3 position;
in vec2 texCoords;
in vec3 normal;
uniform vec3 offset;

#include <vertexBase.glsl>

uniform float windIntensity;
uniform float time;
float random = 1;
float useTime;

const float PI2 = 6.283185307179586476925286766559;

int factor = 1;

float sway() {
    return windIntensity * (sin(useTime / 300) + 0.5);
}

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main(void) {
    worldPosition = calcWorldPos(vec3(position.x + offset.x, position.y + offset.y, position.z + offset.z));
    shadowCoords = calcShadowCoords();

    positionRelativeToCam = calcPosRelativeToCam();

    gl_ClipDistance[0] = calcClipDistance();

    gl_Position = projectionMatrix * positionRelativeToCam;
    useTime += time * 1000;
    //gl_Position.x += sway();

    //gl_Position = vec4(gl_Position.x + offset.x, gl_Position.y + offset.y, gl_Position.z + offset.z + offset.x/5.0 + offset.y/2.0, gl_Position.w);

    pass_texCoords = texCoords;
    //pass_texCoords.x += sway() / 500;
//    float t = float(mod(time, 500))/500.0;
    float windFactor = (( (pow(((sin((-useTime * 4.0 + position.x / 20.0 + sin(position.y * 25.4) / 1.0 )) + 1.0) / 3.0), 4.0)) +

                                          (sin( useTime * 100.0 + position.y * 25.4 ) * 0.02) ) * pass_texCoords.t);

    //gl_Position.x += sway() * rand(texCoords);

    surfaceNormal = calcSurfaceNormal(normal);

    calcLights();

    toCameraVector = calcToCameraVector();

    visibility = calcVisibility();

    shadowCoords.w = calcShadowCoordW();
}