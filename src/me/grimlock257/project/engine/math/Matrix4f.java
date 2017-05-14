package me.grimlock257.project.engine.math;

import java.nio.FloatBuffer;

import me.grimlock257.project.utils.BufferUtils;

/** Contains identity matrix for a 4 float matrix, projection types, translate, rotate and scale also multiplication of
 * matrices */
public class Matrix4f {
	public float[] elements = new float[4 * 4];

	public Matrix4f() {
		setIdentity(this);
	}

	/** Create a new identity matrix, where everything is 0, other than the diagonal (top left to bottom right) which is
	 * one. The identity matrix is effectively the number 1 when referring to matrices */
	public static Matrix4f setIdentity(Matrix4f matrix) {
		for (int i = 0; i < (4 * 4); i++) {
			matrix.elements[i] = 0.0f;
		}

		matrix.elements[0 + 0 * 4] = 1.0f;
		matrix.elements[1 + 1 * 4] = 1.0f;
		matrix.elements[2 + 2 * 4] = 1.0f;
		matrix.elements[3 + 3 * 4] = 1.0f;

		return matrix;
	}

	/** Create a matrix which holds the relevant translational information */
	public static Matrix4f translate(Vector3f vector) {
		Matrix4f matrix = new Matrix4f();

		matrix.elements[0 + 3 * 4] = vector.x;
		matrix.elements[1 + 3 * 4] = vector.y;
		matrix.elements[2 + 3 * 4] = vector.z;

		return matrix;
	}

	/** Create a matrix which holds the relevant translational information (this one is used for GUI) */
	public static Matrix4f translate(Vector2f vector) {
		Matrix4f matrix = new Matrix4f();

		matrix.elements[0 + 3 * 4] = vector.x;
		matrix.elements[1 + 3 * 4] = vector.y;

		return matrix;
	}

	/** Create a matrix which holds the relevant rotational information. Parameter: Angle of which to rotate (degrees),
	 * x, y and z represent which axis to rotate about (0 or 1) */
	public static Matrix4f rotate(float angle, float x, float y, float z) {
		Matrix4f matrix = new Matrix4f();

		float radAngle = (float) Math.toRadians(angle);
		float sin = (float) Math.sin(radAngle);
		float cos = (float) Math.cos(radAngle);
		float oneMinusCos = 1.0f - cos;
		float xS = (float) Math.pow(x, 2); // x squared
		float yS = (float) Math.pow(y, 2); // y squared
		float zS = (float) Math.pow(z, 2); // z squared

		matrix.elements[0 + 0 * 4] = xS * oneMinusCos + cos;
		matrix.elements[1 + 0 * 4] = x * y * oneMinusCos + z * sin;
		matrix.elements[2 + 0 * 4] = x * z * oneMinusCos - y * sin;

		matrix.elements[0 + 1 * 4] = x * y * oneMinusCos - z * sin;
		matrix.elements[1 + 1 * 4] = yS * oneMinusCos + cos;
		matrix.elements[2 + 1 * 4] = y * z * oneMinusCos + x * sin;

		matrix.elements[0 + 2 * 4] = x * z * oneMinusCos + y * sin;
		matrix.elements[1 + 2 * 4] = y * z * oneMinusCos - x * sin;
		matrix.elements[2 + 2 * 4] = zS * oneMinusCos + cos;

		return matrix;
	}

	/** Create a matrix which holds the relevant scaling information. */
	public static Matrix4f scale(float scale) {
		Matrix4f matrix = new Matrix4f();

		matrix.elements[0 + 0 * 4] *= scale;
		matrix.elements[1 + 1 * 4] *= scale;
		matrix.elements[2 + 2 * 4] *= scale;

		return matrix;
	}

	/** Create a matrix which holds the relevant scaling information. */
	public static Matrix4f scale(float x, float y, float z) {
		Matrix4f matrix = new Matrix4f();

		matrix.elements[0 + 0 * 4] *= x;
		matrix.elements[1 + 1 * 4] *= y;
		matrix.elements[2 + 2 * 4] *= z;

		return matrix;
	}

	/** Multiply two matrices together, this is done by multiplying each row in matrix A by each column in matrix B to
	 * produce AB. */
	public Matrix4f multiply(Matrix4f matrixB) {
		Matrix4f resultant = new Matrix4f();

		// For matrix multiplication each row from matrix A is multiplied by each column of matrix B, these individual
		// multiplications are added up to give each element. This for loop does that, after adding doing it
		// multiplication it is added to the sum thus far. After the additions have been completed (meaning the row and
		// column have been successfully multiplied), the answer is then inserted into the resultant matrix.
		for (int column = 0; column < 4; column++) {
			for (int row = 0; row < 4; row++) {
				float sum = 0.0f;

				// element from row/column to be multiplying
				for (int element = 0; element < 4; element++) {
					sum += this.elements[row + element * 4] * matrixB.elements[element + column * 4];
				}
				resultant.elements[row + column * 4] = sum;
			}
		}
		return resultant;
	}

	/** Call the BufferUtils method to create a float buffer containing the elements. */
	public FloatBuffer toFloatBuffer() {
		return BufferUtils.createFloatBuffer(elements);
	}

	@Override
	public String toString() {
		StringBuilder matrix = new StringBuilder();

		for (int y = 0; y < 4; y++) {
			matrix.append("|");
			for (int x = 0; x < 4; x++) {
				matrix.append((x > 0 ? "  " : "") + this.elements[y + x * 4]);
			}
			matrix.append("|\n");
		}

		return matrix.toString();
	}
}