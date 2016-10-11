const int numLights = 4;

out vec2 pass_texCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[numLights];
out float visibility;
out vec4 shadowCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform mat4 toShadowMapSpace;
uniform float shadowDistance;
uniform float shadowTransitionDistance;

out vec3 toCameraVector;

uniform vec3 lightPosition[numLights];

uniform float useFakeLighting;

uniform vec4 clipPlane;

const float density = 0.000035;
const float gradient =  3.0;

float distance;

vec4 worldPosition;
vec4 positionRelativeToCam;

out vec3 ecPosition;

vec4 calcShadowCoords() {
    return toShadowMapSpace * worldPosition;
}

vec4 calcWorldPos(vec3 pos) {
    return transformationMatrix * vec4(pos, 1.0);
}

float calcClipDistance() {
    return dot(worldPosition, clipPlane);
}

void calcLights() {
    ecPosition = vec3(viewMatrix * worldPosition);
    for (int i = 0; i < numLights; i++) {
        toLightVector[i] = lightPosition[i] - worldPosition.xyz;
    }
}

void calcLights(mat3 tanSpace) {
    ecPosition = vec3(viewMatrix * worldPosition);
    for (int i = 0; i < numLights; i++) {
        toLightVector[i] = tanSpace * lightPosition[i] - worldPosition.xyz;
    }
}

vec4 calcPosRelativeToCam() {
    return viewMatrix * worldPosition;
}

vec4 calcGLPosition() {
    return projectionMatrix * positionRelativeToCam;
}

vec2 calcPassTexCoords(vec2 texCoords, float numberOfRows, vec2 offset) {
    return (texCoords / numberOfRows) + offset;
}

vec3 calcSurfaceNormal(vec3 normal) {
    vec3 actualNormal = normal;
    // TODO - find  a better way than branching
    if (useFakeLighting > 0.5) {
        actualNormal = vec3(0.0, 1.0, 0.0);
    }

    return (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
}

float calcVisibility() {
    distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance*density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
    return visibility;
}

vec3 calcToCameraVector() {
    return (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
}

float calcShadowCoordW() {
    distance = distance - (shadowDistance - shadowTransitionDistance);
    distance = distance / shadowTransitionDistance;
    return clamp(1.0 - distance, 0.0, 1.0);
}

mat3 calcToTangentSpace(mat4 modelViewMatrix, vec3 tangent) {
    vec3 norm = normalize(surfaceNormal);
    vec3 tang = normalize((modelViewMatrix * vec4(tangent, 0.0)).xyz);
    vec3 bitang = normalize(cross(normal, tang));
    mat3 toTangentSpace = mat3(
        tang.x, bitang.x, norm.x,
        tang.y, bitang.y, norm.y,
        tang.z, bitang.z, norm.z
    );

    return toTangentSpace;
}