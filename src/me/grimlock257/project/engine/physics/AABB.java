package me.grimlock257.project.engine.physics;

import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.entities.Entity;

/** This class stores the AABB (Axis Aligned Bounding Box) for each entity. Contains methods for detecting collision */
public class AABB {
	private Vector3f min, max, lastMin, lastMax;
	private boolean hasCollidedLeft, hasCollidedRight, hasCollidedFront, hasCollidedBehind, hasCollidedTop, hasCollidedBottom = false;

	public AABB(Entity entity) {
		this.min = new Vector3f(Vector3f.add(entity.getPosition(), entity.getPositionMinMax()[0]));
		this.max = new Vector3f(Vector3f.add(entity.getPosition(), entity.getPositionMinMax()[1]));

		this.lastMin = min;
		this.lastMax = max;
	}

	/** Check if a collision has occurred */
	public boolean checkCollision(Entity primary, AABB secondary) {
		// These if statements check to see if there was a collision
		if (this.max.x < secondary.min.x)
			return false;

		if (this.min.x > secondary.max.x)
			return false;

		if (this.max.y < secondary.min.y)
			return false;

		if (this.min.y > secondary.max.y)
			return false;

		if (this.max.z < secondary.min.z)
			return false;

		if (this.min.z > secondary.max.z)
			return false;

		// If we get this far, there has been a collision. This if block determines where the collision originated from
		if (collidedFromLeft(secondary)) {
			primary.setPositionX((secondary.getMin().x - (primary.getWidth() / 2)));
			setCollidedLeft(true);
			return true;
		}

		if (collidedFromRight(secondary)) {
			primary.setPositionX((secondary.getMax().x + (primary.getWidth() / 2)));
			setCollidedRight(true);
			return true;
		}

		if (collidedFromFront(secondary)) {
			primary.setPositionZ((secondary.getMin().z - (primary.getLength() / 2)));
			setCollidedFront(true);
			return true;
		}

		if (collidedFromBehind(secondary)) {
			primary.setPositionZ((secondary.getMax().z + (primary.getLength() / 2)));
			setCollidedBehind(true);
			return true;
		}

		if (collidedFromTop(secondary)) {
			float heightFromGround = Math.abs(primary.getAABB().getMin().y - primary.getPosition().y);
			primary.setPositionY((secondary.getMin().y) - (primary.getHeight() - heightFromGround));
			setCollidedTop(true);
			return true;
		}

		if (collidedFromBottom(secondary)) {
			primary.setPositionY((secondary.getMax().y));
			setCollidedBottom(true);
			return true;
		}

		// If the two entities have merged
		System.err.println("COLLISION: Two entities are currently inside one another.");
		return true;
	}

	private boolean collidedFromLeft(AABB other) {
		return lastMax.x <= other.min.x && this.max.x >= other.min.x;
	}

	private boolean collidedFromRight(AABB other) {
		return lastMin.x >= other.max.x && this.min.x <= other.max.x;
	}

	private boolean collidedFromFront(AABB other) {
		return lastMax.z <= other.min.z && this.max.z >= other.min.z;
	}

	private boolean collidedFromBehind(AABB other) {
		return lastMin.z >= other.max.z && this.min.z <= other.max.z;
	}

	private boolean collidedFromTop(AABB other) {
		return lastMax.y <= other.min.y && this.max.y >= other.min.y;
	}

	private boolean collidedFromBottom(AABB other) {
		return lastMin.y >= other.max.y && this.min.y <= other.max.y;
	}

	/** Update the coordinates of the AABB to match where the entity has moved to */
	public void updateAABB(Entity entity) {
		lastMin = min;
		lastMax = max;

		min = new Vector3f(Vector3f.add(entity.getPosition(), entity.getPositionMinMax()[0]));
		max = new Vector3f(Vector3f.add(entity.getPosition(), entity.getPositionMinMax()[1]));
	}

	public Vector3f getMin() {
		return min;
	}

	public void setMin(Vector3f min) {
		this.min = min;
	}

	public Vector3f getMax() {
		return max;
	}

	public void setMax(Vector3f max) {
		this.max = max;
	}

	/** Reset all the hasCollided booleans */
	public void resetCollided() {
		this.hasCollidedLeft = false;
		this.hasCollidedRight = false;
		this.hasCollidedFront = false;
		this.hasCollidedBehind = false;
		this.hasCollidedTop = false;
		this.hasCollidedBottom = false;
	}

	public boolean isCollidedLeft() {
		return hasCollidedLeft;
	}

	public void setCollidedLeft(boolean collidedLeft) {
		this.hasCollidedLeft = collidedLeft;
	}

	public boolean isCollidedRight() {
		return hasCollidedRight;
	}

	public void setCollidedRight(boolean collidedRight) {
		this.hasCollidedRight = collidedRight;
	}

	public boolean isCollidedFront() {
		return hasCollidedFront;
	}

	public void setCollidedFront(boolean collidedFront) {
		this.hasCollidedFront = collidedFront;
	}

	public boolean isCollidedBehind() {
		return hasCollidedBehind;
	}

	public void setCollidedBehind(boolean collidedBehind) {
		this.hasCollidedBehind = collidedBehind;
	}

	public boolean isCollidedTop() {
		return hasCollidedTop;
	}

	public void setCollidedTop(boolean collidedTop) {
		this.hasCollidedTop = collidedTop;
	}

	public boolean isCollidedBottom() {
		return hasCollidedBottom;
	}

	public void setCollidedBottom(boolean collidedBottom) {
		this.hasCollidedBottom = collidedBottom;
	}
}