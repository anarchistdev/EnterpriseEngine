#version 400 core

in vec2 position;

out vec4 clipSpace;

out vec2 texCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform vec3 cameraPos;

uniform vec3 lightPosition;

uniform float tiling;

out vec3 toCameraVector;
out vec3 fromLightVector;

void main(void) {
    vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
    clipSpace = projectionMatrix * viewMatrix * worldPosition;
	gl_Position = clipSpace;
	texCoords = vec2(position.x/2.0 + 0.5, position.y/2.0 + 0.5) * tiling;
	toCameraVector = cameraPos - worldPosition.xyz;
	fromLightVector = worldPosition.xyz - lightPosition;
}