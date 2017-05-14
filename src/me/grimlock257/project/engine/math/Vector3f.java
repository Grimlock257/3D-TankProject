package me.grimlock257.project.engine.math;

/** Contains everything needed for vectors, in this case just creating a vector with 3 floats */
public class Vector3f {
	public float x, y, z;

	public Vector3f() {
		set(0, 0, 0);
	}

	public Vector3f(Vector3f vector) {
		set(vector.x, vector.y, vector.z);
	}

	public Vector3f(float x, float y, float z) {
		set(x, y, z);
	}

	/** Sets the current Vector to the one passed in */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/** Add two vectors together */
	public static Vector3f add(Vector3f vec1, Vector3f vec2) {
		Vector3f result = new Vector3f();

		result.x = vec1.x + vec2.x;
		result.y = vec1.y + vec2.y;
		result.z = vec1.z + vec2.z;

		return result;
	}

	/** Multiply a vector by another vector */
	public static Vector3f multiply(Vector3f vec1, Vector3f vec2) {
		Vector3f result = new Vector3f();

		result.x = vec1.x * vec2.x;
		result.y = vec1.y * vec2.y;
		result.z = vec1.z * vec2.z;

		return result;
	}

	/** Multiply a vector by a scale factor */
	public static Vector3f multiply(Vector3f vec1, float scaleFactor) {
		Vector3f result = new Vector3f();

		result.x = vec1.x * scaleFactor;
		result.y = vec1.y * scaleFactor;
		result.z = vec1.z * scaleFactor;

		return result;
	}

	/** Negate a vector, inverting the sign of each element */
	public static Vector3f negate(Vector3f vec) {
		Vector3f result = new Vector3f();

		result.x = -vec.x;
		result.y = -vec.y;
		result.z = -vec.z;

		return result;
	}

	@Override
	public String toString() {
		return "X:" + this.x + ", Y:" + this.y + ", Z:" + this.z;
	}
}