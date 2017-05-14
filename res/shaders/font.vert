#version 330

// Values vertex shader takes in
in vec2 inPosition;
in vec2 inTextureCoords;

// Values vertex shader outputs
out vec2 outTextureCoords;

// Values set from java code
uniform vec2 uniTranslation;

void main(void){
	// Calculate the OpenGL position for the vertex
	gl_Position = vec4(inPosition + uniTranslation * vec2(2.0, -2.0), 0.0, 1.0);
	outTextureCoords = inTextureCoords;
}
