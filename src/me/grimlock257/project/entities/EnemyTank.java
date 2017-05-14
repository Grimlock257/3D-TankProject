package me.grimlock257.project.entities;

import me.grimlock257.project.Main;
import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.models.Model;
import me.grimlock257.project.utils.managers.ProjectileManager;

/** This class represents the enemy tank */
public class EnemyTank extends EntityDamageable {
	private final float TANK_MOVEMENT_SPEED = 0.5f;
	private int cooldown = 0;
	private float diffX, diffZ, rotY;

	// Change to pitch adjust
	private float barrelPitch;
	private float firePower = 20f;
	private float maxDistance = 49;
	private boolean inRange = false;

	private TurretBody turretBody = new TurretBody(Model.turretBody, this);

	public EnemyTank(Model model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}

	/** Call the methods required to make the AI tank work every frame */
	public void update(Vector3f targetPosition) {
		// Difference, gives difference of target in relation to the this tank
		diffX = targetPosition.x - this.getPosition().x;
		diffZ = targetPosition.z - this.getPosition().z;

		aimEasy();

		// Move the turret head based on the input data
		this.turretBody.update(1, 0);
		this.setRotY(rotY);
		this.turretBody.getTurretBarrel().setPitch(barrelPitch);

		if (cooldown-- < 1 && inRange) {
			fire();
			cooldown = 60;
		}

		// Update the bounding box
		this.getCollider().setPosition(getPosition());
		this.getAABB().updateAABB(this);
	}

	/** Aim at the target */
	private void aimEasy() {
		// Adjust the rotation of the tank
		if (diffX < 0 && diffZ < 0) {
			rotY = (float) (180 + Math.toDegrees(Math.atan(Math.abs(diffX / diffZ))));
		} else if (diffX > 0 && diffZ < 0) {
			rotY = (float) (180 - Math.toDegrees(Math.atan(Math.abs(diffX / diffZ))));
		} else if (diffX > 0 && diffZ > 0) {
			rotY = (float) (90 - Math.toDegrees(Math.atan(Math.abs(diffZ / diffX))));
		} else if (diffX < 0 && diffZ > 0) {
			rotY = (float) (270 + Math.toDegrees(Math.atan(Math.abs(diffZ / diffX))));
		}

		// Calculate the distance between the tank and target
		float distance = (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffZ, 2));

		moveTowardsTarget(distance);

		// Calculate the barrelPitch using R = ((initial velocity squared) x sin(2 theta)) divided by acceleration,
		// rearranged for theta
		barrelPitch = (float) (0.5 * Math.toDegrees(Math.asin((-Main.getGravity() * distance) / Math.pow(firePower, 2)))) - 1;
	}

	/** Move towards the target if over a certain distance away */
	private void moveTowardsTarget(float distance) {
		if (distance > maxDistance) {
			float dx = (float) (TANK_MOVEMENT_SPEED * Math.sin(Math.toRadians(getRotY())));
			float dz = (float) (TANK_MOVEMENT_SPEED * Math.cos(Math.toRadians(getRotY())));

			this.increasePosition(dx, 0, dz);

			inRange = false;
		} else {
			inRange = true;
		}
	}

	/** Fire a projectile */
	private void fire() {
		ProjectileManager.addProjectile(Model.missile, getProjectileSpawner(), firePower, 10f, 20f, true);
		recoil(firePower / 10);
	}

	/** Move the tank back by recoilSpeed */
	private void recoil(float recoilSpeed) {
		float dx = (float) -(recoilSpeed * Math.sin(Math.toRadians(getTurretBarrel().getRotY())));
		float dz = (float) -(recoilSpeed * Math.cos(Math.toRadians(getTurretBarrel().getRotY())));

		this.increasePosition(dx, 0, dz);
	}

	/** Return the turret body that belongs to the tank - used for rendering */
	public Entity getTurretBody() {
		return turretBody;
	}

	/** Return the turret barrel that belongs to the tank - used for rendering */
	public Entity getTurretBarrel() {
		return turretBody.getTurretBarrel();
	}

	/** Return the spawn point that belongs to this tank */
	private Vector3f[] getProjectileSpawner() {
		return turretBody.getTurretBarrel().getSpawnPoint();
	}
}