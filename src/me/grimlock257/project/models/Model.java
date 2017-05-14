package me.grimlock257.project.models;

import me.grimlock257.project.utils.managers.ModelManager;

/** This class is used to access a model */
public enum Model {
	// List of models
	tank("tank"),
	turretBody("turretBody"),
	turretBarrel("turretBarrel"),
	tankEnemy("tankEnemy"),
	missile("missile"),
	cube("cube");

	private String name = "Model has not been renamed";

	Model(String name) {
		this.name = name;
	}

	/** Get the model via the ModelManager */
	public ModelTextured getModel() {
		return ModelManager.retrieveModel(this.name);
	}

	/** Get the QUAD used for GUIs via the ModelManager */
	public ModelMesh getQUAD() {
		return ModelManager.retrieveQuad();
	}
}