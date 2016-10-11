#version 140

in vec2 texCoords;
//
out vec4 colorOut;
//
uniform sampler2D colorTexture;

//
//void main(){
//	colorOut = texture(colorTexture, texCoords).rbga;
//}

float mul = 2.0;
float time = 1.5;
vec2 offset(vec2 uv, float time) {
	return (vec2(.5)-texture(colorTexture, uv+vec2(time/10.0)).xy)*mul;
}

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}
vec2 offseta(vec2 uv) {
	return (vec2(.5, .5)-texture(colorTexture, uv+vec2(time/10.0)).xy)*mul;
}
void main()
{
	vec2 uv = texCoords.xy;
	time = rand(texCoords + offseta(texCoords));
	colorOut = texture(colorTexture,uv+offset(uv, time)/100.0/*+offseta(uv)/100.0*/)*vec4(0, 0.5,0.5, 1);
}