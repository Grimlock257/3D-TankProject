package me.grimlock257.project.entities;

import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.models.Model;

/** Represents the Turret Barrel on a turret body */
public class TurretBarrel extends Entity implements SpawnPoint {
	// This is the point in 3D space from which a projectile will be spawn. Position then rotation
	private Vector3f[] spawnPoint = { new Vector3f(), new Vector3f() };
	private float rotX = 0;
	private Entity parent;

	// This class will be instantiated in the parent class, i.e the turret body
	public TurretBarrel(Model model, TurretBody body) {
		super(model, new Vector3f(body.getPosition()), new Vector3f(body.getRotation()));

		this.parent = body;
	}

	/** Update the collider, rotation and position of the turret barrel as well as update the spawn point for
	 * projectiles to reflect the new position of the turret barrel */
	protected void update(float barrelPitchSpeed) {
		this.getCollider().setPosition(getPosition());

		move(barrelPitchSpeed);

		Vector3f rot = parent.getRotation();

		// Numbers here are taken from blender 3D modelling software
		float posX = parent.getPosition().x + (float) (2.79111 * Math.sin(Math.toRadians(parent.getRotY())));
		float posY = (float) (parent.getPosition().y + 4.21186);
		float posZ = parent.getPosition().z + (float) (2.79111 * Math.cos(Math.toRadians(parent.getRotY())));

		this.setPosition(posX, posY, posZ);
		this.setRotation(rot.x + rotX, rot.y, rot.z);

		this.setRotY(this.getRotY() % 360);

		// Set the coordinates of the spawn point accordingly
		setSpawnPoint();
	}

	/** Move the barrel by the inputed speed */
	private void move(float barrelPitchSpeed) {
		rotX += barrelPitchSpeed;

		if (barrelPitchSpeed < 0 && rotX < -60) {
			rotX = -60;
		} else if (barrelPitchSpeed > 0 && rotX > 0) {
			rotX = 0;
		}
	}

	/** Set the pitch of the barrel - used for AI tanks */
	protected void setPitch(float pitch) {
		rotX = pitch;
	}

	/** Return the spawn point that belongs to this turret barrel */
	public Vector3f[] getSpawnPoint() {
		return spawnPoint;
	}

	@Override
	/** Set the spawn point for the turret barrel */
	public void setSpawnPoint() {
		// Position
		this.spawnPoint[0].x = this.getPosition().x + (float) ((6.65 * Math.sin(Math.toRadians(this.getRotY()))) * Math.cos(Math.toRadians(this.getRotX())));
		this.spawnPoint[0].y = this.getPosition().y + (float) ((6.65 * Math.cos(Math.toRadians(this.getRotX()))) * Math.tan(Math.toRadians(-this.getRotX())));
		this.spawnPoint[0].z = this.getPosition().z + (float) ((6.65 * Math.cos(Math.toRadians(this.getRotX()))) * Math.cos(Math.toRadians(this.getRotY())));

		// Rotation
		this.spawnPoint[1].x = this.getRotX();
		this.spawnPoint[1].y = this.getRotY();
		this.spawnPoint[1].z = this.getRotZ();
	}
}