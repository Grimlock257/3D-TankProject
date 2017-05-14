package me.grimlock257.project.engine.physics.rendering;

/** Represents a collider in memory - this class is used for the rendering of a collider */
public class ColliderMesh {
	private int vaoID;

	public ColliderMesh(int vaoID) {
		this.vaoID = vaoID;
	}

	/** Get the VAO ID of the mesh */
	public int getVaoID() {
		return vaoID;
	}
}