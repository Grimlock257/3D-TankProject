package me.grimlock257.project.entities;

import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.engine.physics.AABB;
import me.grimlock257.project.engine.physics.rendering.Collider;
import me.grimlock257.project.models.Model;
import me.grimlock257.project.models.ModelTextured;

/** This class represents a Textured Model as an entity instance in the game world */
public class Entity {
	private ModelTextured model;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	private float width;
	private float length;
	private float height;
	private AABB aabb;
	private Collider collider;

	/** Create an entity at origin with no rotation, scale 1
	 * @param model The model for the entity */
	public Entity(Model model) {
		this.model = model.getModel();
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.scale = 1;
		this.aabb = new AABB(this);
		this.collider = new Collider(this.getModel().getModelMesh().getColliderMesh(), new Vector3f());

		calculateDimensions(this.getPositionMinMax());
	}

	/** Create an entity at specified position with no rotation, scale 1
	 * @param model The model for the entity
	 * @param position The location for which the entity to be at */
	public Entity(Model model, Vector3f position) {
		this.model = model.getModel();
		this.position = position;
		this.rotation = new Vector3f();
		this.scale = 1;
		this.aabb = new AABB(this);
		this.collider = new Collider(this.getModel().getModelMesh().getColliderMesh(), new Vector3f());

		calculateDimensions(this.getPositionMinMax());
	}

	/** Create an entity at specified position,rotation and scale 1
	 * @param model The model for the entity
	 * @param position The location for which the entity to be at
	 * @param rotation The rotation for which the entity to have */
	public Entity(Model model, Vector3f position, Vector3f rotation) {
		this.model = model.getModel();
		this.position = position;
		this.rotation = rotation;
		this.scale = 1;
		this.aabb = new AABB(this);
		this.collider = new Collider(this.getModel().getModelMesh().getColliderMesh(), new Vector3f());

		calculateDimensions(this.getPositionMinMax());
	}

	/** Create an entity at specified position,rotation and scale
	 * @param model The model for the entity
	 * @param position The location for which the entity to be at
	 * @param rotation The rotation for which the entity to have
	 * @param scale The scale for which the entity to have */
	public Entity(Model model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model.getModel();
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.aabb = new AABB(this);
		this.collider = new Collider(this.getModel().getModelMesh().getColliderMesh(), new Vector3f());

		calculateDimensions(this.getPositionMinMax());
	}

	/** Calculate the dimensions of the entity based on min/max data */
	private void calculateDimensions(Vector3f[] minMax) {
		this.width = Math.abs(minMax[0].x) + Math.abs(minMax[1].x);
		this.height = Math.abs(minMax[0].y) + Math.abs(minMax[1].y);
		this.length = Math.abs(minMax[0].z) + Math.abs(minMax[1].z);
	}

	public float getWidth() {
		return width;
	}

	public float getLength() {
		return length;
	}

	public float getHeight() {
		return height;
	}

	/** Increase the x, y and z position coordinates by dx, dy and dz respectively */
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	/** Increase the position coordinates by dPos */
	public void increasePosition(Vector3f dPos) {
		this.position = Vector3f.add(this.position, dPos);
	}

	/** Increase the x, y and z rotations by dx, dy and dz respectively */
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotation.x += dx;
		this.rotation.y += dy;
		this.rotation.z += dz;
	}

	public ModelTextured getModel() {
		return model;
	}

	public void setModel(ModelTextured model) {
		this.model = model;
	}

	public AABB getAABB() {
		return aabb;
	}

	public Vector3f[] getPositionMinMax() {
		return model.getModelMesh().getMinMax();
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public void setPositionX(float x) {
		this.position.x = x;
	}

	public void setPositionY(float y) {
		this.position.y = y;
	}

	public void setPositionZ(float z) {
		this.position.z = z;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public void setRotation(float rotX, float rotY, float rotZ) {
		this.rotation.x = rotX;
		this.rotation.y = rotY;
		this.rotation.z = rotZ;
	}

	public float getRotX() {
		return rotation.x;
	}

	public void setRotX(float rotX) {
		rotation.x = rotX;
	}

	public float getRotY() {
		return rotation.y;
	}

	public void setRotY(float rotY) {
		rotation.y = rotY;
	}

	public float getRotZ() {
		return rotation.z;
	}

	public void setRotZ(float rotZ) {
		rotation.z = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Collider getCollider() {
		return collider;
	}

	public void setCollider(Collider collider) {
		this.collider = collider;
	}

	@Override
	public String toString() {
		return "Entity [Pos: '" + position.toString() + "' | Rot: '" + rotation.toString() + "' | Scale: '" + scale + "']";
	}
}