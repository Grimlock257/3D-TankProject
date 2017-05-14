package me.grimlock257.project.engine.texture;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.grimlock257.project.utils.BufferUtils;

/** This loads a texture from a file and stores it in memory. Returns an ID for the texture. */
public class Texture {
	private int width, height;
	public int textureID;

	public Texture(String path) {
		textureID = load(path);
	}

	/** Load a texture file */
	private int load(String file) {
		int[] pixels = null;

		// Try to open the file, if failure, the catch will run, printing the error and exiting the program
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(file));
			width = image.getWidth();
			height = image.getHeight();

			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			// Display error messages to the console
			System.err.println("TEXTURE FILE READER: Could not read file: " + file);
			e.printStackTrace();
			System.exit(1);
		}

		int[] data = new int[width * height];

		// Reorder the bits
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}

		int result = glGenTextures();

		// Tell OpenGL to store the texture so we can use it later
		glBindTexture(GL_TEXTURE_2D, result);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		glBindTexture(GL_TEXTURE_2D, 0);

		return result;
	}

	/** Bind the texture so that OpenGL can use it */
	public void bind(int id) {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	/** Unbind the texture as we no longer need it */
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}