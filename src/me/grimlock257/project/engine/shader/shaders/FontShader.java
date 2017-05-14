package me.grimlock257.project.engine.shader.shaders;

import me.grimlock257.project.engine.math.Vector2f;
import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.engine.shader.Shader;

/** Create the shader, defining the locations of uniform values, as well as storing information in the VBO */
public class FontShader extends Shader {
	// Shader file paths
	private static final String VERT_FILE = "res/shaders/font.vert";
	private static final String FRAG_FILE = "res/shaders/font.frag";

	// Integers to store the addresses for the following uniform locations
	private int location_colour;
	private int location_translation;

	public FontShader() {
		super(VERT_FILE, FRAG_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "inPosition");
		super.bindAttribute(1, "inTextureCoords");
	}

	@Override
	protected void getUniformLocations() {
		location_colour = super.getUniformLocation("uniColour");
		location_translation = super.getUniformLocation("uniTranslation");
	}

	/** Load font colour into the shader - Vector3f for red, green, blue */
	public void setUniformTextColour(Vector3f colour) {
		super.setUniformVec3f(location_colour, colour);
	}

	/** Load translational information into the shader */
	public void setUniformTranslationVec2f(Vector2f translation) {
		super.setUniformVec2f(location_translation, translation);
	}
}