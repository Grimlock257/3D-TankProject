package me.grimlock257.project.models;

import me.grimlock257.project.engine.texture.ModelTexture;

/** Represents a ModelMesh with a texture applied to it */
public class ModelTextured {
	private ModelMesh modelMesh;
	private ModelTexture texture;

	public ModelTextured(ModelMesh model, ModelTexture texture) {
		this.modelMesh = model;
		this.texture = texture;
	}

	/** Get the model mesh for this model */
	public ModelMesh getModelMesh() {
		return modelMesh;
	}

	/** Return the texture of the model */
	public ModelTexture getTexture() {
		return texture;
	}
}