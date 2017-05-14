package me.grimlock257.project.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

//TODO Look into ByteBuffer
public class BufferUtils {
	private BufferUtils() {
	}

	/** Need to create a int buffer as indices need to be stored as ints */
	public static IntBuffer createIntBuffer(int[] data) {
		IntBuffer buffer = ByteBuffer.allocateDirect(data.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
		buffer.put(data).flip();

		return buffer;
	}

	/** Need to create a float buffer as information stored in VBO must be in this format */
	public static FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buffer = ByteBuffer.allocateDirect(data.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
		buffer.put(data).flip();

		return buffer;
	}
}