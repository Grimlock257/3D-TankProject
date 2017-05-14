package me.grimlock257.project.entities;

import java.util.ArrayList;

import me.grimlock257.project.Main;
import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.engine.physics.AABB;
import me.grimlock257.project.models.Model;

/** This class represents a projectile */
public class Projectile extends Entity {
	private Vector3f velocity = new Vector3f();
	private boolean isActive = true;
	private boolean isExplosive = true;
	private float explosionStrength;
	private float explosionRadius;
	private float rotX = 0f;
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	/** Create a new projectile entity */
	public Projectile(Model model, Vector3f[] source, float firePower, float explosionStrength, float explosionRadius, boolean isExplosive, float scale) {
		super(model, new Vector3f(source[0]), new Vector3f(source[1]), scale);

		this.isExplosive = isExplosive;
		this.explosionStrength = explosionStrength;
		this.explosionRadius = explosionRadius;

		float radAngleY = (float) Math.toRadians(source[1].y);
		float radAngleX = (float) Math.abs(Math.toRadians(source[1].x));

		float resultantXZ = firePower * (float) Math.cos(radAngleX);

		velocity.y = (float) Math.sin(radAngleX) * firePower;
		velocity.x = (float) Math.sin(radAngleY) * resultantXZ;
		velocity.z = (float) Math.cos(radAngleY) * resultantXZ;
	}

	/** Apply gravity, check for collisions and adjust the rotation of position of the projectile */
	public void update(Entity... entities) {
		for (Entity entity : entities) {
			this.entities.add(entity);
		}

		this.velocity.y -= Main.getGravity();
		checkCollisions(this.getAABB());

		float absVelY = Math.abs(velocity.y);
		float resultantZ = (float) Math.sqrt(Math.pow(velocity.x, 2) + Math.pow(velocity.z, 2));

		if (resultantZ > 0 && velocity.y > 0) {
			rotX = (float) (90 - Math.toDegrees(Math.atan(absVelY / resultantZ)));
		} else if (resultantZ > 0 && velocity.y < 0) {
			rotX = (float) (90 + Math.toDegrees(Math.atan(absVelY / resultantZ)));
		} else if (resultantZ < 0 && velocity.y > 0) {
			rotX = (float) (270 + Math.toDegrees(Math.atan(absVelY / resultantZ)));
		} else if (resultantZ < 0 && velocity.y < 0) {
			rotX = (float) (270 - Math.toDegrees(Math.atan(absVelY / resultantZ)));
		}

		this.increasePosition(Vector3f.multiply(velocity, 0.18f));
		this.setRotX(rotX);

		this.getCollider().setPosition(getPosition());
		this.getAABB().updateAABB(this);

		this.entities.clear();
	}

	/** Check for collisions between the projectile and all passed in entities that need to be tested for collision */
	private void checkCollisions(AABB aabb) {
		aabb.resetCollided();

		// Loop through every entity that needs to be tested for collision
		for (Entity entity : entities) {
			// This is the distance between the projectile and the entity
			float distance = (float) Math.sqrt(Math.pow(this.getPosition().x - entity.getPosition().x, 2) + Math.pow(this.getPosition().z - entity.getPosition().z, 2));
			float damage = 0;

			// If direct collision, then if within explosionRadius
			if (aabb.checkCollision(this, entity.getAABB()) && this.isExplosive && entity instanceof EntityDamageable) {
				damage = explosionStrength;
			} else if (this.getPosition().y < 0 && distance < this.explosionRadius && this.isExplosive && entity instanceof EntityDamageable) {
				damage = explosionStrength * (1 / (distance / 2));
			}

			if (damage != 0) {
				((EntityDamageable) entity).dealDamage(damage);
				System.out.println("Delt " + damage + " damage to " + ((entity instanceof Player) ? "Player" : "Enemy") + ". Health Remaining: " + ((EntityDamageable) entity).getHealth());
			}
		}
		// If that projectile has collided from any direction, set the velocity to nothing, and if the projectile is
		// explosive, set isActive to false as the missile is now defunct
		if (aabb.isCollidedLeft() || aabb.isCollidedRight() || aabb.isCollidedFront() || aabb.isCollidedBehind() || aabb.isCollidedTop() || aabb.isCollidedBottom()) {
			getAABB().updateAABB(this);
			this.velocity = new Vector3f();

			if (this.isExplosive)
				this.isActive = false;
		}

		// If the projectile is below y = 0, then it has collided with the ground as there is no ground height variation
		if (this.getPosition().y < 0) {
			this.setPositionY(0);
			this.velocity = new Vector3f();

			if (this.isExplosive)
				this.isActive = false;
		}
	}

	/** Return whether this projectile is active */
	public boolean isActive() {
		return isActive;
	}

	@Override
	public String toString() {
		return super.toString() + " [Vel: '" + this.velocity.toString() + "']";
	}
}