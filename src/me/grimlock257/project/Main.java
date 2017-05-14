package me.grimlock257.project;

import static org.lwjgl.glfw.GLFW.*;

import java.io.File;
import java.util.ArrayList;

import me.grimlock257.project.engine.font.FontEngine;
import me.grimlock257.project.engine.font.FontFace;
import me.grimlock257.project.engine.font.GUIText;
import me.grimlock257.project.engine.input.Keyboard;
import me.grimlock257.project.engine.math.Vector2f;
import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.engine.render.ProjectionType;
import me.grimlock257.project.engine.render.VAOLoader;
import me.grimlock257.project.engine.render.renderers.RenderEngine;
import me.grimlock257.project.engine.texture.GUITexture;
import me.grimlock257.project.engine.texture.ModelTexture;
import me.grimlock257.project.entities.EnemyTank;
import me.grimlock257.project.entities.Player;
import me.grimlock257.project.entities.Projectile;
import me.grimlock257.project.entities.world.Camera;
import me.grimlock257.project.entities.world.Light;
import me.grimlock257.project.models.Model;
import me.grimlock257.project.terrains.Terrain;
import me.grimlock257.project.utils.managers.DisplayManager;
import me.grimlock257.project.utils.managers.ModelManager;
import me.grimlock257.project.utils.managers.ProjectileManager;

public class Main {
	private static final float GRAVITY = 0.5f;
	private static RenderEngine renderer;
	private static final Vector3f[] defaultPlayerSpawn = { new Vector3f(), new Vector3f() };

	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		// Create the application window
		DisplayManager.createDisplay();

		// Create a new Loader object so that we can load models
		VAOLoader vaoLoader = new VAOLoader();

		// Instantiate the ModelManager so that all models are loaded
		new ModelManager(vaoLoader);

		// Instantiate the render engine
		renderer = new RenderEngine();

		// Initialise the Text Master
		FontEngine.init(vaoLoader);

		FontFace font = new FontFace(vaoLoader.storeTexture("arial"), new File("res/fonts/arial.fnt"));
		GUIText opponent = new GUIText("", 1, font, new Vector2f(0.5f, 0.88f), 0.5f, true);
		GUIText difficulty = new GUIText("Difficulty: " + Settings.difficulty, 1, font, new Vector2f(0.5f, 0.91f), 0.5f, true);
		GUIText player1 = new GUIText("", 1, font, new Vector2f(0, 0.88f), 0.5f, true);
		GUIText tankBodyRotation = new GUIText("", 1, font, new Vector2f(0, 0.91f), 0.5f, true);
		GUIText turretBodyRotation = new GUIText("", 1, font, new Vector2f(0, 0.94f), 0.5f, true);
		GUIText turretBarrelPitch = new GUIText("", 1, font, new Vector2f(0, 0.97f), 0.5f, true);

		// Set up terrain grids
		Terrain terrain1 = new Terrain(0, 0, vaoLoader, new ModelTexture(vaoLoader.storeTexture("sand")), 0.1f);
		Terrain terrain2 = new Terrain(1, 0, vaoLoader, new ModelTexture(vaoLoader.storeTexture("grass")), 5f);

		// Set up default entities
		Player player = new Player(Model.tank, new Vector3f(defaultPlayerSpawn[0]), new Vector3f(defaultPlayerSpawn[1]));
		EnemyTank enemy = new EnemyTank(Model.tankEnemy, new Vector3f(50, 0, 50f), new Vector3f(), 1);

		// Set up the GUIs
		GUITexture gui = new GUITexture(vaoLoader.storeTexture("missile"), new Vector2f(-1f, -0.88f), new Vector2f(2f, 0.12f));

		// Set up the light and camera
		Light light = new Light(new Vector3f(100, 150, 30), new Vector3f(1, 1, 1));
		Camera camera = new Camera(player.getTurretBody());

		// List of terrains to pass to the player
		ArrayList<Terrain> terrains = new ArrayList<Terrain>();

		// Actual game loop, where rendering, logic etc. happens here
		while (!glfwWindowShouldClose(DisplayManager.getWindow())) {
			renderer.addGUI(gui);

			renderer.addTerrain(terrain1);
			renderer.addTerrain(terrain2);
			terrains.add(terrain1);
			terrains.add(terrain2);

			renderer.addEntity(player);
			renderer.addEntity(player.getTurretBody());
			renderer.addEntity(player.getTurretBarrel());

			renderer.addEntity(enemy);
			renderer.addEntity(enemy.getTurretBody());
			renderer.addEntity(enemy.getTurretBarrel());

			// Update the entities, get input etc.
			player.update(terrains, enemy);
			enemy.update(player.getPosition());
			camera.update();

			// Updates relating to text
			if (Settings.difficulty == "EASY") {
				opponent.updateText("Opponent Health: " + enemy.getHealth());
			} else {
				opponent.remove();
			}

			difficulty.updateText("Difficulty: " + Settings.difficulty);
			player1.updateText("Player Health: " + player.getHealth());
			tankBodyRotation.updateText("Tank Body Rotation (Y): " + String.format("%.1f", Math.abs(player.getRotY())));
			turretBodyRotation.updateText("Turret Body Rotation (Y): " + String.format("%.1f", Math.abs(player.getTurretBody().getRotY())));
			turretBarrelPitch.updateText("Barrel Pitch: " + String.format("%.1f", Math.abs(player.getTurretBarrel().getRotX())));

			// Clean the projectiles array to remove dead missiles
			ProjectileManager.cleanProjectiles();

			// Loop through projectiles, update and render them
			for (Projectile projectile : ProjectileManager.getProjectiles()) {
				if (projectile.isActive()) {
					projectile.update(player, enemy);
					renderer.addEntity(projectile);
				}
			}

			// Render everything, using the light and camera
			renderer.render(light, camera);

			// Tell the display to update
			DisplayManager.updateDisplay();

			// Check if ESCAPE pressed for exit
			if (Keyboard.keys[GLFW_KEY_ESCAPE]) {
				glfwSetWindowShouldClose(DisplayManager.getWindow(), true);
			}

			if (Keyboard.keys[GLFW_KEY_O]) {
				renderer.updateProjectionType(ProjectionType.ORTHOGRAPHIC);
			}
			if (Keyboard.keys[GLFW_KEY_P]) {
				renderer.updateProjectionType(ProjectionType.PERSPECTIVE);
			}
			if (Keyboard.keys[GLFW_KEY_C]) {
				renderer.setRenderColliders();
			}
			if (Keyboard.keys[GLFW_KEY_K]) {
				Settings.difficulty = "EASY";
			}
			if (Keyboard.keys[GLFW_KEY_L]) {
				Settings.difficulty = "NORMAL";
			}
		}

		// Memory Management when exiting
		renderer.delete();
		vaoLoader.delete();
		DisplayManager.terminateDisplay();
	}

	// TODO: Move to a 'constants'(?) class with in the physics package
	public static float getGravity() {
		return GRAVITY;
	}

	public static RenderEngine getRenderer() {
		return renderer;
	}

	public static Vector3f[] getDefaultPlayerSpawn() {
		return defaultPlayerSpawn;
	}
}