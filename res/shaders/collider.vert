#version 400 core

// Values vertex shader takes in
in vec3 inPosition;

// Values set from java code
uniform mat4 uniTransformationMatrix;
uniform mat4 uniProjectionMatrix;
uniform mat4 uniViewMatrix;

void main(void) {
	// Calculate the OpenGL position for the object
	gl_Position = uniProjectionMatrix * uniViewMatrix * uniTransformationMatrix * vec4(inPosition, 1.0);
}
