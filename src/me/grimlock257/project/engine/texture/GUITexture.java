package me.grimlock257.project.engine.texture;

import me.grimlock257.project.engine.math.Vector2f;

/** This class represents a GUI texture element */
public class GUITexture {
	private int texture;
	private Vector2f position;
	private Vector2f scale;

	public GUITexture(int texture, Vector2f position, Vector2f scale) {
		super();
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}

	/** Return the texture of the GUI */
	public int getTexture() {
		return texture;
	}

	/** Return the position of the GUI */
	public Vector2f getPosition() {
		return position;
	}

	/** Return the scale of the GUI */
	public Vector2f getScale() {
		return scale;
	}
}