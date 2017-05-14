package me.grimlock257.project.engine.physics;

public enum CollisionSide {
	LEFT,
	RIGHT,
	FRONT,
	BEHIND,
	TOP,
	BOTTOM,
	NONE, 		// Used when no collision is occurring
	COLLIDING	// Used when the two testable entities are inside one another
}