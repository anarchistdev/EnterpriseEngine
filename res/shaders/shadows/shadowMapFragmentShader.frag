#version 330

in vec2 texCoords;

out vec4 colorOut;

uniform sampler2D modelTexture;

void main(void){
    float alpha = texture(modelTexture, texCoords).a;
    if (alpha < 0.5) {
        discard;
    }

    float depth = gl_FragCoord.z;

    float dx = dFdx(depth);
    float dy = dFdy(depth);

	colorOut = vec4(1.0);
	//colorOut = vec4(depth, depth * depth, 0.0, 0.0);
	//colorOut = vec4(depth, pow(depth, 2.0) + 0.25*(dx*dx + dy*dy), 0.0, 1.0);

}