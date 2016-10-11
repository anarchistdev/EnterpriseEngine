#version 140

in vec2 position;

in mat4 modelViewMatrix;
in vec4 texOffsets;
in float blend;

uniform float numberOfRows;

out vec2 texCoords1;
out vec2 texCoords2;
out float blendFactor;

uniform mat4 projectionMatrix;

void main(){
    vec2 texCoords = position + vec2(0.5, 0.5);
    texCoords.y = 1.0 - texCoords.y;
    texCoords /= numberOfRows;
    texCoords1 = texCoords + texOffsets.xy;
    texCoords2 = texCoords + texOffsets.zw;
    blendFactor = blend;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}