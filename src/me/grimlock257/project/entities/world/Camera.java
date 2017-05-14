package me.grimlock257.project.entities.world;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import me.grimlock257.project.engine.input.Keyboard;
import me.grimlock257.project.engine.input.MouseScroll;
import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.entities.Entity;
import me.grimlock257.project.utils.managers.DisplayManager;

/** Handles the camera */
public class Camera {
	// Variables relating camera position respective to target
	private float dist = 50;
	private float cameraAngleRelativeToTarget = 0;

	// Variable relating to camera transformations
	private Vector3f position = new Vector3f();
	private float pitch = 20; // How high or low the camera is aimed
	private float yaw = 0; // How much left or right the camera is aiming

	// The target the camera is following
	private Entity target;

	public Camera(Entity target) {
		setTarget(target);
	}

	/** Call the relevant methods for an update */
	public void update() {
		// Get camera control input
		input();
	}

	/** Get user input for the camera */
	private void input() {
		if (Keyboard.keys[GLFW_KEY_BACKSPACE]) {
			dist = 50;
			cameraAngleRelativeToTarget = 0;
			pitch = 20;
		}

		if (Keyboard.keys[GLFW_KEY_B]) {
			pitch = 90;
		}
		if (Keyboard.keys[GLFW_KEY_N]) {
			pitch = 0;
			cameraAngleRelativeToTarget = -90;
		}

		// Calculate the remaining settings for the camera based on the user input
		calculatePitch();
		calculateZoom();
		calculateRelativeCameraAngleToTarget();

		// Move the camera
		move();
	}

	/** Calculate camera position based on the target */
	private void move() {
		// Calculate the horizontal/vertical distance that the camera should be from the target
		float hDist = (float) (dist * Math.cos(Math.toRadians(pitch + 5)));
		float vDist = (float) (dist * Math.sin(Math.toRadians(pitch + 5)));

		// Calculate the camera position
		calculateCameraPosition(hDist, vDist);

		this.yaw = (180 - (target.getRotY() + cameraAngleRelativeToTarget)) % 360;
	}

	/** Return the position of the camera */
	public Vector3f getPosition() {
		return position;
	}

	/** Return the pitch of the camera */
	public float getPitch() {
		return pitch;
	}

	/** Return the yaw of the camera */
	public float getYaw() {
		return yaw;
	}

	/** Return the entity the camera is currently following */
	public Entity getTarget() {
		return target;
	}

	/** Set the entity to be followed to target argument */
	public void setTarget(Entity target) {
		this.target = target;
	}

	/** Calculate the zoom of the camera, i.e the distance of the direct line between the camera and target */
	private void calculateZoom() {
		if (glfwGetMouseButton(DisplayManager.getWindow(), GLFW_MOUSE_BUTTON_RIGHT) == GL_FALSE && glfwGetKey(DisplayManager.getWindow(), GLFW_KEY_LEFT_SHIFT) == GL_FALSE) {
			double zoomLevel = MouseScroll.deltaY * 15;
			dist -= zoomLevel;
			MouseScroll.resetDelta();
		}
	}

	/** Calculate the pitch of the camera */
	private void calculatePitch() {
		if (glfwGetMouseButton(DisplayManager.getWindow(), GLFW_MOUSE_BUTTON_RIGHT) == GL_TRUE) {
			double pitchChange = MouseScroll.deltaY * 0.5;
			pitch -= pitchChange;
		}
	}

	/** Calculate the relative angle of the camera around the target */
	private void calculateRelativeCameraAngleToTarget() {
		if (glfwGetKey(DisplayManager.getWindow(), GLFW_KEY_LEFT_SHIFT) == GL_TRUE) {
			double angleChange = MouseScroll.deltaY * 0.9;
			cameraAngleRelativeToTarget -= angleChange;
		}
	}

	/** Calculate the position of the camera in 3D space */
	private void calculateCameraPosition(float horizDistance, float verticDistance) {
		float cameraAngle = (float) Math.toRadians(target.getRotY() + cameraAngleRelativeToTarget);
		float xOffset = (float) (horizDistance * Math.sin(cameraAngle));
		float zOffset = (float) (horizDistance * Math.cos(cameraAngle));

		position.x = target.getPosition().x - xOffset;
		position.z = target.getPosition().z - zOffset;
		position.y = target.getPosition().y + verticDistance;
	}
}