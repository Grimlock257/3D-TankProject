package me.grimlock257.project.entities;

import me.grimlock257.project.engine.math.Vector3f;

/** Objects that can spawn entities into the world must have these two methods */
public interface SpawnPoint {
	public Vector3f[] getSpawnPoint();

	public void setSpawnPoint();
}