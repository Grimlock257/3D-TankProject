#version 400 core

// Values fragment shader takes in
in vec2 outTextureCoords;
in vec3 outSurfaceNormal;
in vec3 outToLightVector;

// Values fragment shader outputs to the screen
out vec4 outColour;

// Values set from java code
uniform sampler2D uniTextureSampler;
uniform vec3 uniLightColour;

void main(void) {
	// Normalize our vectors so that size doesn't affect our calculations
	vec3 unitNormal = normalize(outSurfaceNormal);
	vec3 unitLightVector = normalize(outToLightVector);

	// Calculate diffuse lighting for out pixel
	float normalDotLight = dot(unitNormal, unitLightVector);
	float diffuse = max(normalDotLight, 0.2); // 0.2 so that we have ambient lighting to ensure no parts of the model are completely black

	// Multiply brightness by light color by texture pixel color to produce the final color
	outColour = diffuse * vec4(uniLightColour, 1.0) * texture(uniTextureSampler, outTextureCoords);
}
