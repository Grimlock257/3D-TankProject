package me.grimlock257.project.engine.font;

/** Represents a text mesh in terms of raw data */
public class TextMesh {
	// Variables that store the position/texture coordinates that make up our text
	private float[] positions;
	private float[] textureCoords;

	protected TextMesh(float[] positions, float[] textureCoords) {
		this.positions = positions;
		this.textureCoords = textureCoords;
	}

	/** Return the vertex positions of our text */
	public float[] getPositions() {
		return positions;
	}

	/** Return the texture coordinates of our text */
	public float[] getTextureCoords() {
		return textureCoords;
	}

	/** Return the amount of vertices in our text */
	public int getVertexCount() {
		return positions.length / 2;
	}
}