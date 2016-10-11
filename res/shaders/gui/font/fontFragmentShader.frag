#version 330

in vec2 pass_texCoords;

out vec4 colorOut;

uniform vec3 color;
uniform sampler2D fontAtlas;

const float width = 0.5;
const float edge  = 0.1;

uniform float borderWidth;
const float borderEdge = 0.4;

uniform vec2 offset;
uniform vec3 outlineColor;

void main() {
    float distance = 1.0 - texture(fontAtlas, pass_texCoords).a;
    float alpha = 1.0 - smoothstep(width, width+edge, distance);

    float distance2 = 1.0 - texture(fontAtlas, pass_texCoords + offset).a;
    float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth+borderEdge, distance2);

    float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
    vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha);

    colorOut = vec4(overallColor, overallAlpha);
}