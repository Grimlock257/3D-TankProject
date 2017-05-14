package me.grimlock257.project.models;

import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.engine.physics.GenerateColliderBox;
import me.grimlock257.project.engine.physics.rendering.ColliderMesh;
import me.grimlock257.project.engine.render.VAOLoader;

/** Represents a model in terms of vertices stored in memory */
public class ModelMesh {
	private int vaoID;
	private int vertexCount;

	private Vector3f[] minMax;
	private ColliderMesh colliderMesh;

	private VAOLoader loader = new VAOLoader();

	public ModelMesh(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.minMax = null;
		this.colliderMesh = null;
	}

	public ModelMesh(int vaoID, int vertexCount, Vector3f[] minMax) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.minMax = minMax;
		this.colliderMesh = loader.storeColliderInVAO(GenerateColliderBox.generateVertices(minMax));
	}

	/** Get the VAO ID of the mesh */
	public int getVaoID() {
		return vaoID;
	}

	/** Get the amount of vertices in the model */
	public int getVertexCount() {
		return vertexCount;
	}

	// Return the minimum and maximum coordinate of the model as a Vector3f array
	public Vector3f[] getMinMax() {
		return minMax;
	}

	/** Get the collider mesh for this model */
	public ColliderMesh getColliderMesh() {
		return colliderMesh;
	}
}