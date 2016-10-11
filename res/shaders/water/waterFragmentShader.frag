#version 400 core

out vec4 out_Color;
in vec2 texCoords;

in vec4 clipSpace;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;
uniform vec3 lightColor;

const float waveStrength = 0.02;

uniform float moveFactor;
uniform vec3 color;

uniform float near;
uniform float far;

in vec3 toCameraVector;
in vec3 fromLightVector;

const float shineDamper = 20.0;
const float reflectivity = 0.5;

void main(void) {
    vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
    vec2 refractionTexCoords = vec2(ndc.x, ndc.y);
    vec2 reflectionTexCoords = vec2(ndc.x, -ndc.y);

    float depth = texture(depthMap, refractionTexCoords).r;
    float floorDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
    depth = gl_FragCoord.z;
    float waterDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
    float waterDepth = floorDistance - waterDistance;

//    vec2 distortion1 = (texture(dudvMap, vec2(texCoords.x + moveFactor, texCoords.y)).rg * 2.0 - 1.0) * waveStrength;
//    vec2 distortion2 = (texture(dudvMap, vec2(-texCoords.x + moveFactor, texCoords.y + + moveFactor)).rg * 2.0 - 1.0) * waveStrength;
//    vec2 totalDistortion = distortion1 + distortion2;

    vec2 distortion1 = texture(dudvMap, vec2(texCoords.x + moveFactor, texCoords.y)).rg * 0.2;
    distortion1 = texCoords + vec2(distortion1.x, distortion1.y + moveFactor);
    vec2 totalDistortion = (texture(dudvMap, distortion1).rg * 2.0 - 1) * waveStrength * clamp(waterDepth / 20.0, 0.0, 1.0);

    reflectionTexCoords += totalDistortion;
    refractionTexCoords += totalDistortion;

    refractionTexCoords = clamp(refractionTexCoords, 0.001, 0.999);

    reflectionTexCoords.x = clamp(reflectionTexCoords.x, 0.001, 0.999);
    reflectionTexCoords.y = clamp(reflectionTexCoords.y, -0.999, -0.001);

    vec4 reflectionColor = texture(reflectionTexture, reflectionTexCoords);
    vec4 refractionColor = texture(refractionTexture, refractionTexCoords);

    vec4 normalMapColor = texture(normalMap, distortion1);
    vec3 normal = vec3(normalMapColor.r * 2.0 - 1, normalMapColor.b * 2.0, normalMapColor.g * 2.0 - 1.0);
    normal = normalize(normal);

    vec3 viewVector = normalize(toCameraVector);
    float refractiveFactor = dot(viewVector, normal);
    refractiveFactor = pow(refractiveFactor, 0.5);

    vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
    float specular = max(dot(reflectedLight, viewVector), 0.0);
    specular = pow(specular, shineDamper);
    vec3 specularHighlights = lightColor * specular * reflectivity * clamp(waterDepth / 5.0, 0.0, 1.0);

	refractiveFactor = clamp(refractiveFactor, 0.0, 1.0);

    refractionColor = mix(refractionColor, vec4(color,1.0) /*+ vec4(0.5,0.5,0.5,1.0)*/, clamp(waterDepth/60.0, 0.0, 1.0));

	out_Color = mix(reflectionColor, refractionColor, refractiveFactor);
	out_Color = mix(out_Color, vec4(color, 1.0), 0.2) + vec4(specularHighlights, 1.0);
	out_Color.a = clamp(waterDepth / 5.0, 0.0, 1.0);

}