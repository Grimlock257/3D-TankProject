package me.grimlock257.project.engine.texture;

/** This class represents a texture to be used for a model */
public class ModelTexture {
	private int textureID;

	public ModelTexture(int id) {
		this.textureID = id;
	}

	/** Return the ID of the texture */
	public int getID() {
		return this.textureID;
	}
}