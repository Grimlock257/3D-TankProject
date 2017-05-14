package me.grimlock257.project.engine.input;

import org.lwjgl.glfw.GLFWScrollCallback;

public class MouseScroll extends GLFWScrollCallback {
	public static double deltaY = 0;

	@Override
	public void invoke(long window, double xoffset, double yoffset) {
		MouseScroll.deltaY = yoffset;
	}

	public static void resetDelta() {
		deltaY = 0;
	}
}