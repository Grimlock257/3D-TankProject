package me.grimlock257.project.entities;

import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.models.Model;

/** Represents the Turret Body on a tank */
public class TurretBody extends Entity {
	private float rotY = 0;
	private Entity parent;

	// The turret barrel object that belongs to this turret body
	private TurretBarrel turretBarrel = new TurretBarrel(Model.turretBarrel, this);

	// This class will be instantiated in the parent class, i.e the tank
	public TurretBody(Model model, Entity parent) {
		super(model, new Vector3f(parent.getPosition()), new Vector3f(parent.getRotation()));

		this.parent = parent;
	}

	/** Update the collider, rotation and position of the turret body as well as the call the update in turret barrel */
	protected void update(float barrelPitchSpeed, float turretBodyRotationSpeed) {
		this.getCollider().setPosition(getPosition());

		rotate(turretBodyRotationSpeed);

		Vector3f rot = parent.getRotation();
		this.setPosition(parent.getPosition());
		this.setRotation(rot.x, rot.y + rotY, rot.z);
		
		this.setRotY(this.getRotY() % 360);

		turretBarrel.update(barrelPitchSpeed);
	}

	/** Rotate the turret body */
	private void rotate(float turretBodyRotationSpeed) {
		rotY += turretBodyRotationSpeed;

		if (turretBodyRotationSpeed < 0 && rotY < -60) {
			rotY = -60;
		} else if (turretBodyRotationSpeed > 0 && rotY > 60) {
			rotY = 60;
		}
	}

	/** Return the turret barrel that belongs to this turret body */
	protected TurretBarrel getTurretBarrel() {
		return turretBarrel;
	}
}