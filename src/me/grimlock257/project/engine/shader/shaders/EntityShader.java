package me.grimlock257.project.engine.shader.shaders;

import me.grimlock257.project.engine.math.Matrix4f;
import me.grimlock257.project.engine.shader.Shader;
import me.grimlock257.project.entities.world.Camera;
import me.grimlock257.project.entities.world.Light;
import me.grimlock257.project.utils.MatrixUtils;

/** Create the shader, defining the locations of uniform values, as well as storing information in the VBO */
public class EntityShader extends Shader {
	// Shader file paths
	private static final String VERT_FILE = "res/shaders/entity.vert";
	private static final String FRAG_FILE = "res/shaders/entity.frag";

	// Integers to store the addresses for the following uniform locations
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;

	public EntityShader() {
		super(VERT_FILE, FRAG_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "inPosition");
		super.bindAttribute(1, "inTextureCoords");
		super.bindAttribute(2, "inNormals");
	}

	@Override
	protected void getUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("uniTransformationMatrix");
		location_projectionMatrix = super.getUniformLocation("uniProjectionMatrix");
		location_viewMatrix = super.getUniformLocation("uniViewMatrix");
		location_lightPosition = super.getUniformLocation("uniLightPosition");
		location_lightColour = super.getUniformLocation("uniLightColour");
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

	/** Load a light into the shader */
	public void setUniformLight(Light light) {
		super.setUniformVec3f(location_lightPosition, light.getPosition());
		super.setUniformVec3f(location_lightColour, light.getColour());
	}
}