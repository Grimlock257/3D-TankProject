package me.grimlock257.project.engine.shader.shaders;

import me.grimlock257.project.engine.math.Matrix4f;
import me.grimlock257.project.engine.shader.Shader;
import me.grimlock257.project.entities.world.Camera;
import me.grimlock257.project.utils.MatrixUtils;

/** Create the shader, defining the locations of uniform values, as well as storing information in the VBO */
public class ColliderShader extends Shader {
	// Shader file paths
	private static final String VERT_FILE = "res/shaders/collider.vert";
	private static final String FRAG_FILE = "res/shaders/collider.frag";

	// Integers to store the addresses for the following uniform locations
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

	public ColliderShader() {
		super(VERT_FILE, FRAG_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "inPosition");
	}

	@Override
	protected void getUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("uniTransformationMatrix");
		location_projectionMatrix = super.getUniformLocation("uniProjectionMatrix");
		location_viewMatrix = super.getUniformLocation("uniViewMatrix");
	}

	/** Load transformation matrix into the shader */
	public void setUniformTransformationMat4f(Matrix4f matrix) {
		super.setUniformMat4f(location_transformationMatrix, matrix);
	}

	/** Load projection matrix into the shader */
	public void setUniformProjectionMat4f(Matrix4f projection) {
		super.setUniformMat4f(location_projectionMatrix, projection);
	}

	/** Load view matrix into the shader */
	public void setUniformViewMat4f(Camera camera) {
		Matrix4f matrix = MatrixUtils.createViewMatrix(camera);
		super.setUniformMat4f(location_viewMatrix, matrix);
	}
}