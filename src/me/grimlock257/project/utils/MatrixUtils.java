package me.grimlock257.project.utils;

import me.grimlock257.project.engine.math.Matrix4f;
import me.grimlock257.project.engine.math.Vector2f;
import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.entities.world.Camera;

/** Handles matrix related methods */
public class MatrixUtils {
	private MatrixUtils() {
	}

	/** Create an orthographic projection matrix (has a solid rectangle view of the world, with no perception of depth).
	 * 'z' coordinate is used to determine render order. */
	public static Matrix4f createOrthographicMatrix(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f projection = new Matrix4f();

		projection.elements[0 + 0 * 4] = 2.0f / (right - left);
		projection.elements[1 + 1 * 4] = 2.0f / (top - bottom);
		projection.elements[2 + 2 * 4] = -2.0f / (far - near);
		projection.elements[0 + 3 * 4] = -(right + left) / (right - left);
		projection.elements[1 + 3 * 4] = -(top + bottom) / (top - bottom);
		projection.elements[2 + 3 * 4] = -(far + near) / (far - near);

		return projection;
	}

	/** Create a perspective matrix. Using this type of projection means depth exists, you can zoom in and out of the
	 * world. The view is given by a frustum, which is essentially a rectangular based pyramid with a length of the top
	 * cut of, this defines the viewable areas of the world. */
	public static Matrix4f createPerspectiveMatrix(float width, float height, float fov, float near, float far) {
		Matrix4f projection = new Matrix4f();

		float aspect = width / height;
		float cotFOV = 1f / (float) Math.tan(Math.toRadians(fov / 2f));

		projection.elements[0 + 0 * 4] = cotFOV / aspect;
		projection.elements[1 + 1 * 4] = cotFOV;
		projection.elements[2 + 2 * 4] = (near + far) / (near - far);
		projection.elements[3 + 2 * 4] = -1;
		projection.elements[2 + 3 * 4] = (2 * far * near) / (near - far);
		projection.elements[3 + 3 * 4] = 0;

		return projection;
	}

	/** Create a transformation matrix for an entity */
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		Matrix4f matrix = new Matrix4f();

		matrix = matrix.multiply(Matrix4f.translate(translation));
		matrix = matrix.multiply(Matrix4f.rotate(rotation.y, 0, 1, 0));
		matrix = matrix.multiply(Matrix4f.rotate(rotation.x, 1, 0, 0));
		matrix = matrix.multiply(Matrix4f.rotate(rotation.z, 0, 0, 1));
		matrix = matrix.multiply(Matrix4f.scale(scale));

		return matrix;
	}

	/** Create a transformation matrix for a GUI */
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();

		matrix = matrix.multiply(Matrix4f.translate(translation));
		matrix = matrix.multiply(Matrix4f.scale(scale.x, scale.y, 0f));

		return matrix;
	}

	/** Create a view matrix for the camera so the the user can have the illusion of moving around */
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f matrix = new Matrix4f();

		Vector3f negativeCameraPos = Vector3f.negate(camera.getPosition());

		matrix = matrix.multiply(Matrix4f.rotate((float) camera.getPitch(), 1, 0, 0));
		matrix = matrix.multiply(Matrix4f.rotate((float) camera.getYaw(), 0, 1, 0));
		matrix = matrix.multiply(Matrix4f.translate(negativeCameraPos));

		return matrix;
	}
}