package me.grimlock257.project.utils.managers;

import java.util.HashMap;

import me.grimlock257.project.engine.render.OBJLoader;
import me.grimlock257.project.engine.render.VAOLoader;
import me.grimlock257.project.engine.texture.ModelTexture;
import me.grimlock257.project.models.ModelMesh;
import me.grimlock257.project.models.ModelTextured;

/** This is where all the models are instantiated and loaded into memory */
public class ModelManager {
	// Initialise all the models and lists of models
	private static ModelTextured tankBody, tankTurretBody, tankTurretBarrel, tankEnemy, missile, cube;
	private static HashMap<String, ModelTextured> models = new HashMap<String, ModelTextured>();

	// The following two create a QUAD in memory which can be used for the GUI elements
	private final float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static ModelMesh QUAD;

	public ModelManager(VAOLoader loader) {
		// Create a new OBJLoader object to be used when loading models
		OBJLoader objLoader = new OBJLoader();

		// Load the QUAD for GUI elements into memory
		QUAD = loader.storeInVAO(positions);

		// Load the actual model data into a VAO
		ModelMesh tankBodyModel = objLoader.readObjModelFile("tankBody", loader);
		ModelMesh turretBodyModel = objLoader.readObjModelFile("turretBody", loader);
		ModelMesh turretBarrelModel = objLoader.readObjModelFile("turretBarrel", loader);
		ModelMesh missileModel = objLoader.readObjModelFile("missile", loader);
		ModelMesh cubeModel = objLoader.readObjModelFile("cube", loader);

		// Apply a texture and save that as a object
		tankBody = new ModelTextured(tankBodyModel, new ModelTexture(loader.storeTexture("tank")));
		tankTurretBody = new ModelTextured(turretBodyModel, new ModelTexture(loader.storeTexture("tank")));
		tankTurretBarrel = new ModelTextured(turretBarrelModel, new ModelTexture(loader.storeTexture("tank")));
		tankEnemy = new ModelTextured(tankBodyModel, new ModelTexture(loader.storeTexture("enemyTank")));
		missile = new ModelTextured(missileModel, new ModelTexture(loader.storeTexture("missile")));
		cube = new ModelTextured(cubeModel, new ModelTexture(loader.storeTexture("white")));

		// Put these objects into a HashMap, for use with the Model ENUM
		models.put("tank", tankBody);
		models.put("turretBody", tankTurretBody);
		models.put("turretBarrel", tankTurretBarrel);
		models.put("tankEnemy", tankEnemy);
		models.put("missile", missile);
		models.put("cube", cube);
	}

	/** Retrieve a model */
	public static ModelTextured retrieveModel(String name) {
		return models.get(name);
	}

	/** Retrieve the quad model used for GUI elements */
	public static ModelMesh retrieveQuad() {
		return QUAD;
	}
}