package me.grimlock257.project.utils.managers;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import me.grimlock257.project.engine.input.Keyboard;
import me.grimlock257.project.engine.input.MouseScroll;

/** Handles creation, updating and deletion of the display. */
public class DisplayManager {
	// Constants for the dimensions of the application window
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	private static final String TITLE = "3D Graphics Project - Adam Watson of Sir Isaac Newton Sixth Form";

	private static long window;
	private static GLFWVidMode vidmode;

	/** Create a GLFW window. Initialises the context, set callbacks and window settings */
	public static void createDisplay() {
		// Initialise GLFW (Deals with window creation and IO) - Returns 1 if success, 0 if not
		if (!glfwInit())
			throw new IllegalStateException("GLFW failed to initialise!");

		// The window should not be resizable
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		// Enable multi-sampling with 8 samples
		glfwWindowHint(GLFW_SAMPLES, 8);

		// Parameters: width, height, title, full screen, sharing resources between contexts
		window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, 0, 0);
		if (window == GLFW_FALSE)
			throw new IllegalStateException("GLFW failed to create window");

		// Get the resolution of the primary monitor
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		// Place window in the centre of the screen
		glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

		// Set callbacks so GLFW listens for input
		glfwSetKeyCallback(window, new Keyboard());
		glfwSetScrollCallback(window, new MouseScroll());

		// Make this window have a context so graphics can be drawn
		glfwMakeContextCurrent(window);

		// Enable v-sync
		glfwSwapInterval(1);

		// Tell GLFW to display the window
		glfwShowWindow(window);

		// This essentially creates a context for OpenGL to draw to
		GL.createCapabilities();

		// Tell GL where it can draw. Parameters: x, y, width, height. (x, y) defined as bottom left corner
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}

	/** Update the display by swapping buffers and polling for events */
	public static void updateDisplay() {
		// Two buffers are created, so one buffer is drawn to while one is displayed, then they are swapped so the
		// one that was just drawn is displayed
		glfwSwapBuffers(window);

		// Process events received and return them instantly
		glfwPollEvents();
	}

	/** Terminate the display, freeing the resources as GLFW won't need them anymore as the application is exiting */
	public static void terminateDisplay() {
		glfwTerminate();
	}

	/** Get the address of the window */
	public static long getWindow() {
		return window;
	}

	/** Get the vidmode, holds information about the window, resolution etc. */
	public static GLFWVidMode getVidmode() {
		return vidmode;
	}
}