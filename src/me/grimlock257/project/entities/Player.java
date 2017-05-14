package me.grimlock257.project.entities;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import me.grimlock257.project.Main;
import me.grimlock257.project.engine.input.Keyboard;
import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.engine.physics.AABB;
import me.grimlock257.project.models.Model;
import me.grimlock257.project.terrains.Terrain;
import me.grimlock257.project.utils.managers.ProjectileManager;

/** This class represents the player */
public class Player extends EntityDamageable {
	// Constants for movement speeds
	private static final float FORWARD_MOVEMENT_SPEED = 1f;
	private static final float BACKWARD_MOVEMENT_SPEED = FORWARD_MOVEMENT_SPEED * 0.30f;
	private static final float PITCH_ADJUST_SPEED = 1f;
	private static final float ROTATION_SPEED = 1.2f;

	// The turret body object that belongs to this tank
	private TurretBody turretBody = new TurretBody(Model.turretBody, this);

	// Variables relating to movement speeds
	private float currentMovementSpeed = 0;
	private float currentRotationSpeed = 0;
	private float barrelPitchSpeed = 0;
	private float turretBodyRotationSpeed = 0;
	private float dx, dy, dz;
	private float movementModifier = 1;

	// Variables for firing
	private float firePower = 20f;
	private int cooldown = 0;
	private int ammunitionType = 0;

	// List of entities to test collisions with
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Terrain> terrains = new ArrayList<Terrain>();

	public Player(Model model) {
		super(model, new Vector3f(), new Vector3f(), 1);
	}

	public Player(Model model, Vector3f position) {
		super(model, position, new Vector3f(), 1);
	}

	public Player(Model model, Vector3f position, Vector3f rotation) {
		super(model, position, rotation, 1);
	}

	public Player(Model model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}

	/** Get user input and update the collider and AABB */
	public void update(ArrayList<Terrain> terrains, Entity... entities) {
		this.terrains = terrains;

		for (Entity entity : entities) {
			this.entities.add(entity);
		}

		input();

		getCollider().setPosition(getPosition());
		getAABB().updateAABB(this);

		this.setRotY(this.getRotY() % 360);

		this.entities.clear();
	}

	/** Get user input to move the tank around the game world */
	private void input() {
		// Actual Tank Controls
		if (Keyboard.keys[GLFW_KEY_W]) {
			currentMovementSpeed = FORWARD_MOVEMENT_SPEED;
		} else if (Keyboard.keys[GLFW_KEY_S]) {
			currentMovementSpeed = -BACKWARD_MOVEMENT_SPEED;
		} else {
			currentMovementSpeed = 0;
			movementModifier = 1;
		}

		if (Keyboard.keys[GLFW_KEY_A]) {
			currentRotationSpeed = ROTATION_SPEED;
		} else if (Keyboard.keys[GLFW_KEY_D]) {
			currentRotationSpeed = -ROTATION_SPEED;
		} else {
			currentRotationSpeed = 0;
		}

		// Turret Controls
		if (Keyboard.keys[GLFW_KEY_DOWN]) {
			barrelPitchSpeed = PITCH_ADJUST_SPEED;
		} else if (Keyboard.keys[GLFW_KEY_UP]) {
			barrelPitchSpeed = -PITCH_ADJUST_SPEED;
		} else {
			barrelPitchSpeed = 0;
		}

		if (Keyboard.keys[GLFW_KEY_LEFT]) {
			turretBodyRotationSpeed = ROTATION_SPEED;
		} else if (Keyboard.keys[GLFW_KEY_RIGHT]) {
			turretBodyRotationSpeed = -ROTATION_SPEED;
		} else {
			turretBodyRotationSpeed = 0;
		}

		// Firing controls
		if (Keyboard.keys[GLFW_KEY_SPACE] && cooldown == 0) {
			fire();
			cooldown = 60;
		}

		// Swap the active weapon
		if (Keyboard.keys[GLFW_KEY_E]) {
			ammunitionType = (ammunitionType == 0) ? 1 : 0;
			System.out.println("The active weapon is " + ((ammunitionType == 0) ? "Big Missile" : "Small Missile") + ".");
		}

		// Firing cool down
		if (cooldown > 0)
			cooldown--;

		// Move the tank based on the input data
		move();
	}

	/** Move the player by the inputed speed and update the turret body entity */
	private void move() {
		dx = (float) ((currentMovementSpeed * movementModifier) * Math.sin(Math.toRadians(getRotY())));
		dz = (float) ((currentMovementSpeed * movementModifier) * Math.cos(Math.toRadians(getRotY())));

		checkCollisions(this.getAABB());

		float playerX = getPosition().x;
		float playerZ = getPosition().z;

		for (Terrain terrain : terrains) {
			if (playerX > terrain.getX() && playerX < terrain.getX() + terrain.getLength()) {
				if (playerZ > terrain.getZ() && playerZ < terrain.getZ() + terrain.getLength()) {
					movementModifier = terrain.getFrictionCoefficient();
				}
			}
		}

		super.increaseRotation(0, this.currentRotationSpeed, 0);
		super.increasePosition(dx, dy, dz);

		// Move the turret head based on the input data
		this.turretBody.update(barrelPitchSpeed, turretBodyRotationSpeed);
	}

	/** Fire a projectile */
	private void fire() {
		if (ammunitionType == 0) {
			ProjectileManager.addProjectile(Model.missile, getProjectileSpawner(), firePower, 10f, 20f, true, 3);
			recoil(firePower / 10);
		} else {
			ProjectileManager.addProjectile(Model.missile, getProjectileSpawner(), firePower * 2, 2f, 16f, true);
			recoil(firePower / 20);
		}
	}

	/** Move the tank back by recoilSpeed */
	private void recoil(float recoilSpeed) {
		float dx = (float) -(recoilSpeed * Math.sin(Math.toRadians(getTurretBarrel().getRotY())));
		float dz = (float) -(recoilSpeed * Math.cos(Math.toRadians(getTurretBarrel().getRotY())));

		this.increasePosition(dx, 0, dz);
	}

	/** Check for collisions between the projectile and all passed in entities that need to be tested for collision */
	private void checkCollisions(AABB aabb) {
		aabb.resetCollided();

		// Loop through every entity that needs to be tested for collision
		for (Entity entity : entities) {
			aabb.checkCollision(this, entity.getAABB());
		}

		if (aabb.isCollidedLeft() || aabb.isCollidedRight() || aabb.isCollidedFront() || aabb.isCollidedBehind() || aabb.isCollidedTop() || aabb.isCollidedBottom())
			getAABB().updateAABB(this);

		// The following 6 if prevent movement in the direction of collision by testing if the attempted movement speed
		// is towards to the side the player collided from
		if (aabb.isCollidedLeft() && dx > 0)
			dx = 0;

		if (aabb.isCollidedRight() && dx < 0)
			dx = 0;

		if (aabb.isCollidedFront() && dz > 0)
			dz = 0;

		if (aabb.isCollidedBehind() && dz < 0)
			dz = 0;

		if (aabb.isCollidedTop() && dy > 0)
			dy = 0;

		if (aabb.isCollidedBottom() && dy < 0)
			dy = 0;

		// If the player falls of the game world and falls below y = 10, set the players position and rotation to the
		// spawn point specified in the main method
		if (this.getPosition().y < -10) {
			this.setPosition(new Vector3f(Main.getDefaultPlayerSpawn()[0]));
			this.setRotation(new Vector3f(Main.getDefaultPlayerSpawn()[1]));
		}
	}

	/** Return the turret body that belongs to the player - used for rendering */
	public Entity getTurretBody() {
		return turretBody;
	}

	/** Return the turret barrel that belongs to the player - used for rendering */
	public Entity getTurretBarrel() {
		return turretBody.getTurretBarrel();
	}

	/** Return the spawn point that belongs to this tank */
	private Vector3f[] getProjectileSpawner() {
		return turretBody.getTurretBarrel().getSpawnPoint();
	}
}