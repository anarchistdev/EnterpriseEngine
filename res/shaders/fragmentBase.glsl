const int numLights = 4;

#include <light/lightingFunctions.glsl>
#include <light/gamma.glsl>

layout(location = 1) out vec4 outBloom;

in vec2 pass_texCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[numLights];

in vec3 toCameraVector;

in vec4 shadowCoords;

in float visibility;

uniform vec3 lightColor[numLights];

uniform vec3 attenuation[numLights];

// If you are making a custom lighting calculation, these variables
// have to be filled out.
vec3 totalDiffuse = vec3(0.0);
vec3 totalSpecular = vec3(0.0);

vec3 unitNormal;
vec3 unitVectorToCamera;

uniform sampler2D shadowMap;
float lightFactor = 1.0;
//uniform int pcfCount;
//float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);
uniform float shadowMapSize;
float texelSize = 1.0 / shadowMapSize;
const int pcfCount = 4;
const float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);

float ambiance = 0.5;

uniform vec3 skyColor;

in vec3 ecPosition;

// Cook-Torrance values
uniform float roughnessValue;
uniform float F0;
float k = 1.0;
uniform float specularPower;

uniform samplerCube skyboxReflection;
uniform float reflectivity;

uniform sampler2D specularMap;
uniform float hasSpecularMap;


//float roughnessValue = 0.1; // 0 : smooth, 1: rough
//float F0 = 0.8; // fresnel reflectance at normal incidence
//float k = 0.2; // fraction of diffuse reflection (specular reflection = 1 - k)


void initVars() {
    unitNormal = normalize(surfaceNormal);
    unitVectorToCamera = normalize(toCameraVector);
}

float linstep(float low, float high, float v){
    return clamp((v-low)/(high-low), 0.0, 1.0);
}

void setAmbiance(float amb) {
    ambiance = amb;
}

float calcLightFactor() {
	float total = 0.0;
	for (int x=-pcfCount; x<=pcfCount; x++){
		for (int y=-pcfCount; y<=pcfCount; y++){
			float objectNearestLight = texture(shadowMap,shadowCoords.xy + vec2(x,y) * texelSize).r;
			if(shadowCoords.z > objectNearestLight + 0.002){
				total +=1.0;
			}
		}
	}

	total /= totalTexels;
    return 1.0 - (total * shadowCoords.w);
}

float calcVSM(vec3 newCoords) {
    float compare = newCoords.z;

    vec2 moments = texture(shadowMap, newCoords.xy).xy;
    float p = step(compare, moments.x);
    float variance = max(moments.y - moments.x * moments.x, 0.000002);
    float d = compare - moments.x;
    float pMax = variance / (variance * d*d);

    return min(max(p ,pMax), 1.0);
}

//float sampleShadow(sampler2D shadowMap, vec2 coords, float compare) {
//    return step(compare, texture(shadowMap, coords.xy).r);
//}
//
//float sampleShadowLinear(sampler2D shadow, vec2 texCoords, float compare, vec2 shadowPixSize) {
//	vec2 pixelPos = texCoords / shadowPixSize + vec2(0.5);
//	vec2 fracPart = fract(pixelPos);
//	vec2 startPix = (pixelPos - fracPart) * shadowPixSize;
//
//	float botLeft = sampleShadow(shadow, startPix, compare);
//	float botRigh = sampleShadow(shadow, startPix + vec2(shadowPixSize.x, 0), compare);
//	float topLeft = sampleShadow(shadow, startPix + vec2(0, shadowPixSize.y), compare);
//	float topRigh = sampleShadow(shadow, startPix + shadowPixSize, compare);
//
//	// mix is a lerp
//	float mixA = mix(botLeft, topLeft, fracPart.y);
//	float mixB = mix(botRigh, topRigh, fracPart.y);
//
//	return mix(mixA, mixB, fracPart.x);
//
//}
//
//float calcLightFactor() {
//    float mapSize = 4096.0;
//    float texelSize = 1.0 / mapSize;
//    float total = 0.0;
//
//    for (float x = -pcfCount; x <= pcfCount; x+=1.0f) {
//        for (float y = -pcfCount; y <= pcfCount; y+=1.0f) {
//            total  += sampleShadowLinear(shadowMap, shadowCoords.xy + vec2(x, y), shadowCoords.z - 2.0, vec2(texelSize, texelSize));
////            if (shadowCoords.z > nearestToLight) {
////                total += 1;
////            }
//        }
//    }
//
//    //total /= totalTexels;
//    return  total / totalTexels;
//}

//float sampleShadowPCF(sampler2D shadowMap, vec2 coords, float compare, vec2 texelSize) {
//    const float NUM_SAMPLES = 7.0f;
//    const float SAMPLES_START = (NUM_SAMPLES-1.0f)/2.0f;
//    const float NUM_SAMPLES_SQRT = NUM_SAMPLES * NUM_SAMPLES;
//    float result = 0.0f;
//
//    for (float y = -SAMPLES_START; y <= SAMPLES_START; y+= 1.0f) {
//        for (float x = -SAMPLES_START; x <= SAMPLES_START; x+= 1.0f) {
//            vec2 coordsOffset = vec2(x, y) * texelSize;
//            //result += sampleShadowLinear(shadowMap, coords + coordsOffset, compare, texelSize);
//            result += texture(shadowMap, coords + coordsOffset * texelSize).r;
//        }
//    }
//
//    return result / NUM_SAMPLES_SQRT;
//}




//void calcStandardLighting() {
//    for (int i = 0; i < numLights; i++) {
//        float distance = length(toLightVector[i]);
//        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
//        vec3 unitLightVector = normalize(toLightVector[i]);
//        float nDot1 = dot(unitNormal, unitLightVector);
//        float brightness = max(nDot1, 0.0);
//        vec3 lightDirection = -unitLightVector;
//        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
//        float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
//        specularFactor = max(specularFactor, 0);
//        float dampedFactor = pow(specularFactor, shineDamper);
//        totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i])/attFactor;
//        totalDiffuse = totalDiffuse + (brightness * lightColor[i])/attFactor;
//    }
//
//    totalDiffuse = max(totalDiffuse * lightFactor, ambiance);
//}

void calcCookTorranceLighting() {
    vec3 eyeDir = unitVectorToCamera;
    vec3 normalPos = vec3(surfaceNormal.xy, surfaceNormal.z+5);
    vec3 eyeDirection = normalize(toCameraVector);

    for (int i = 0; i < numLights; i++) {
        vec3 unitLightVector = normalize(toLightVector[i]);
        vec3 lightDirection = unitLightVector;
        float NdotL = max(dot(unitNormal, lightDirection), 0.0);

        float specular = specularPower * cookTorranceSpecular(lightDirection, eyeDirection, unitNormal, roughnessValue, F0);
        float diffuse = lambertDiffuse(lightDirection, unitNormal);


        totalSpecular = totalSpecular + (specular * lightColor[i]);
        totalDiffuse = totalDiffuse + (diffuse);
    }

    totalDiffuse = max(totalDiffuse * lightFactor, ambiance);
    totalSpecular = totalSpecular * lightFactor;
}

vec4 calcTextureColor(sampler2D tex) {
    vec4 Color = texture(tex, pass_texCoords);
    if (hasSpecularMap > 0.5) {
        totalSpecular *= texture(specularMap, pass_texCoords).r;
    }

    if (Color.a < 0.5) {
        discard;
    }
    return toLinear(Color);

}