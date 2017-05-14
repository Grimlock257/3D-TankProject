package me.grimlock257.project.engine.physics.rendering;

import me.grimlock257.project.engine.math.Vector3f;

/** Represents a collider - this class is used for the rendering of a collider */
public class Collider {
	private ColliderMesh mesh;
	private Vector3f position;

	public Collider(ColliderMesh mesh, Vector3f position) {
		this.mesh = mesh;
		this.position = position;
	}

	public Collider(ColliderMesh mesh, Vector3f position, Vector3f rotation) {
		this.mesh = mesh;
		this.position = position;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/** Return the collider mesh */
	public ColliderMesh getMesh() {
		return mesh;
	}

	public void setMesh(ColliderMesh mesh) {
		this.mesh = mesh;
	}
}