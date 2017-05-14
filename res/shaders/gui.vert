#version 140

// Values vertex shader takes in
in vec2 inPosition;

// Values vertex shader outputs
out vec2 outTextureCoords;

// Values set from java code
uniform mat4 uniTransformationMatrix;

void main(void) {
	// Calculate the OpenGL position for the vertex
	gl_Position = uniTransformationMatrix * vec4(inPosition, 0.0, 1.0);
	outTextureCoords = vec2(inPosition.x, inPosition.y);
}
