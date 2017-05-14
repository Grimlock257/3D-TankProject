package me.grimlock257.project.entities.world;

import me.grimlock257.project.engine.math.Vector3f;

/** Handles the light */
public class Light {
	private Vector3f position;
	private Vector3f colour;

	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
}