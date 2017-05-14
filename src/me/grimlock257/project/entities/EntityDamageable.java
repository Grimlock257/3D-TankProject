package me.grimlock257.project.entities;

import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.models.Model;

/** This class represents an entity that can be damaged */
public class EntityDamageable extends Entity {
	private float maxHealth = 100f;
	private float health = maxHealth;

	private boolean isDead = false;

	public EntityDamageable(Model model) {
		super(model);
	}

	public EntityDamageable(Model model, Vector3f position) {
		super(model, position);
	}

	public EntityDamageable(Model model, Vector3f position, Vector3f rotation) {
		super(model, position, rotation);
	}

	public EntityDamageable(Model model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}

	/** Reset the entities health */
	public void resetHealth() {
		this.health = this.maxHealth;
	}

	/** Return the entities health */
	public float getHealth() {
		return health;
	}

	/** Subtract damage from the entities health
	 * @param damage The damage to deal to the entity */
	public void dealDamage(float damage) {
		health -= damage;
	}

	/** Return whether the entity is dead */
	public boolean isDead	() {
		return isDead;
	}

	/** Set the isDead variable */
	public void setIsDead(boolean isDead) {
		this.isDead = isDead;
	}

	@Override
	public String toString() {
		return "Entity [Pos: '" + getPosition().toString() + "' | Rot: '" + getRotation().toString() + "' | Scale: '" + getScale() + "' | Health: '" + getHealth() + "']";
	}
}