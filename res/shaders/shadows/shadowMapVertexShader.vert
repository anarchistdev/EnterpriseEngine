#version 150

in vec3 in_position;
in vec2 in_texCoords;

out vec2 texCoords;

uniform mat4 mvpMatrix;

void main(void){
	gl_Position = mvpMatrix * vec4(in_position, 1.0);
	texCoords = in_texCoords;
}
