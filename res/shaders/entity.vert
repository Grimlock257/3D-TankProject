#version 400 core

// Values vertex shader takes in
in vec3 inPosition;
in vec2 inTextureCoords;
in vec3 inNormal;

// Values vertex shader outputs
out vec2 outTextureCoords;
out vec3 outSurfaceNormal;
out vec3 outToLightVector;

// Values set from java code
uniform mat4 uniTransformationMatrix;
uniform mat4 uniProjectionMatrix;
uniform mat4 uniViewMatrix;
uniform vec3 uniLightPosition;

void main(void) {
	// Calculate the world position for the object
	vec4 positionInWorld = uniTransformationMatrix * vec4(inPosition, 1.0);

	// Calculate the OpenGL position for the vertex
	gl_Position = uniProjectionMatrix * uniViewMatrix * positionInWorld;
	outTextureCoords = inTextureCoords;

	// The model may have been transformed, so our normals also need to be transformed
	outSurfaceNormal = (uniTransformationMatrix * vec4(inNormal, 0.0)).xyz;
	// Calculate the vector that goes from the vertex to the light source
	outToLightVector = uniLightPosition - positionInWorld.xyz;
}
