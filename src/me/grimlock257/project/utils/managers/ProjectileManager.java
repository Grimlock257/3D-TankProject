package me.grimlock257.project.utils.managers;

import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.entities.Projectile;
import me.grimlock257.project.models.Model;

/** This class handles all projectiles in the game. Contained here are methods for managing projectiles, such as adding
 * a new projectile or cleaning the projectile array */
public class ProjectileManager {
	private static Projectile[] projectiles = new Projectile[0];
	private static int projectileCount = 0;

	/** Creates a projectile in the world using the following parameters:
	 * @param model The model reference for the projectile
	 * @param source The spawn point of the projectile
	 * @param firePower The fire power of which the projectile has (initial speed)
	 * @param explosionStrength The explosion damage the projectile deals at the centre of the explosion
	 * @param explosionRadius The radius of which the projectile affects
	 * @param explosive Whether the projectile is to destroy on impact */
	public static void addProjectile(Model model, Vector3f[] source, float firePower, float explosionStrength, float explosionRadius, boolean explosive) {
		addProjectile(model, source, firePower, explosionStrength, explosionRadius, explosive, 1);
	}

	/** Creates a projectile in the world using the following parameters:
	 * @param model The model reference for the projectile
	 * @param source The spawn point of the projectile
	 * @param firePower The fire power of which the projectile has (initial speed)
	 * @param explosionStrength The explosion damage the projectile deals at the centre of the explosion
	 * @param explosionRadius The radius of which the projectile affects
	 * @param explosive Whether the projectile is to destroy on impact
	 * @param scale The scale of the projectile */
	public static void addProjectile(Model model, Vector3f[] source, float firePower, float explosionStrength, float explosionRadius, boolean explosive, float scale) {
		projectileCount++;

		if (projectileCount > projectiles.length) {
			Projectile[] tempProjectiles = projectiles.clone();
			projectiles = new Projectile[projectileCount];

			for (int i = 0; i < tempProjectiles.length; i++) {
				projectiles[i] = tempProjectiles[i];
			}
		}

		projectiles[projectiles.length - 1] = new Projectile(model, source, firePower, explosionStrength, explosionRadius, explosive, scale);
	}

	/** Resizes the projectile array by removing inactive entries to the array */
	public static void cleanProjectiles() {
		for (int i = 0; i < projectiles.length; i++) {
			if (!projectiles[i].isActive())
				projectileCount--;
		}

		Projectile[] tempProjectiles = projectiles.clone();
		projectiles = new Projectile[projectileCount];

		for (int i = 0, j = 0; i < tempProjectiles.length; i++) {
			if (!tempProjectiles[i].isActive())
				continue;

			projectiles[j++] = tempProjectiles[i];
		}
	}

	/** Fetches the array of projectiles */
	public static Projectile[] getProjectiles() {
		return projectiles;
	}
}