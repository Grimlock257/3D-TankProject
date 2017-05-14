#version 140

// Values fragment shader takes in
in vec2 outTextureCoords;

// Values fragment shader outputs to the screen
out vec4 outColour;

// Values set from java code
uniform sampler2D uniGuiTexture;

void main(void) {
	// The colour to output to the screen
	outColour = texture(uniGuiTexture, outTextureCoords);
}
