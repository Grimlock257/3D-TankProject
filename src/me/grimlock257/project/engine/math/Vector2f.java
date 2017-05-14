package me.grimlock257.project.engine.math;

/** Contains everything needed for vectors, in this case just creating a vector with 2 floats */
public class Vector2f {
	public float x, y;

	public Vector2f() {
		set(0, 0);
	}

	public Vector2f(Vector2f vector) {
		set(vector.x, vector.y);
	}

	public Vector2f(float x, float y) {
		set(x, y);
	}

	/** Sets the current Vector to the one passed in */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "X:" + this.x + ", Y:" + this.y;
	}
}