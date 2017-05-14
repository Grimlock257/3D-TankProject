#version 330

// Values fragment shader takes in
in vec2 outTextureCoords;

// Values fragment shader outputs to the screen
out vec4 outColour;

// Values set from java code
uniform vec3 uniColour;
uniform sampler2D uniFontAtlas;

void main(void){
	// The colour to output to the screen
	outColour = vec4(uniColour, texture(uniFontAtlas, outTextureCoords).a);
}
