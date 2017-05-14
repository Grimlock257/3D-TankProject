package me.grimlock257.project.engine.shader.shaders;

import me.grimlock257.project.engine.math.Matrix4f;
import me.grimlock257.project.engine.shader.Shader;

/** Create the shader, defining the locations of uniform values, as well as storing information in the VBO */
public class GUIShader extends Shader {
	// Shader file paths
	private static final String VERT_FILE = "res/shaders/gui.vert";
	private static final String FRAG_FILE = "res/shaders/gui.frag";

	// Integers to store the addresses for the following uniform locations
	private int location_transformationMatrix;

	public GUIShader() {
		super(VERT_FILE, FRAG_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "inPosition");
	}

	@Override
	protected void getUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("uniTransformationMatrix");
	}

	/** Load transformation matrix into the shader */
	public void setUniformTransformationMat4f(Matrix4f matrix) {
		super.setUniformMat4f(location_transformationMatrix, matrix);
	}
}