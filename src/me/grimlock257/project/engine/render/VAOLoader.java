package me.grimlock257.project.engine.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.engine.physics.rendering.ColliderMesh;
import me.grimlock257.project.engine.texture.Texture;
import me.grimlock257.project.models.ModelMesh;
import me.grimlock257.project.utils.BufferUtils;

/** This class contains methods for loading data in to Vertex Array Objects (VAOs). E.g: Models and Textures */
public class VAOLoader {
	// Lists to keep track of IDs of VAOs, VBOs and Textures
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> texs = new ArrayList<Integer>();

	/** Store a model into a VAO so that it can be rendered in 3D space */
	public ModelMesh storeInVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices, Vector3f[] minMax) {
		int vaoID = generateVAO();

		bindVAO(vaoID);
		bindIndicesBuffer(indices);
		storeVBOInAttributeList(0, 3, positions);
		storeVBOInAttributeList(1, 2, textureCoords);
		storeVBOInAttributeList(2, 3, normals);
		unbindVAO();

		return new ModelMesh(vaoID, indices.length, minMax);
	}

	/** Used for Fonts, store a font QUAD into a VAO so that it can be rendered to the screen */
	public int storeInVAO(float[] positions, float[] textureCoords) {
		int vaoID = generateVAO();

		bindVAO(vaoID);
		storeVBOInAttributeList(0, 2, positions);
		storeVBOInAttributeList(1, 2, textureCoords);
		unbindVAO();

		return vaoID;
	}

	/** This one is used for storing the QUAD used for GUIs */
	public ModelMesh storeInVAO(float[] positions) {
		int vaoID = generateVAO();

		bindVAO(vaoID);
		storeVBOInAttributeList(0, 2, positions);
		unbindVAO();

		return new ModelMesh(vaoID, positions.length / 2);
	}

	/** This method loads a collider to a VAO */
	public ColliderMesh storeColliderInVAO(float[] positions) {
		int vaoID = generateVAO();

		bindVAO(vaoID);
		bindIndicesBuffer(new int[] { 0, 5, 4, 3, 3, 4, 7, 2, 7, 6, 1, 2, 0, 1, 6, 5 });
		storeVBOInAttributeList(0, 3, positions);
		unbindVAO();

		return new ColliderMesh(vaoID);
	}

	/** Store a texture into memory */
	public int storeTexture(String fileName) {
		Texture texture = new Texture("res/" + fileName + ".png");
		texs.add(texture.textureID);

		return texture.textureID;
	}

	/** Create a new VAO ID so that we have somewhere in memory to store our data for later use */
	private int generateVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);

		return vaoID;
	}

	/** Bind the VAO so OpenGL can it to store the data in it */
	private void bindVAO(int vaoID) {
		GL30.glBindVertexArray(vaoID);
	}

	/** Unbind the current VAO so that we are no longer writing to it */
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	/** Bind the indices buffer, so OpenGL knows how to connect vertices */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = BufferUtils.createIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	/** Place the data, as a VBO, into the attribute list
	 * @param size The amount of floats per vertex, i.e a Vector3f would have 3 floats */
	private void storeVBOInAttributeList(int index, int size, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	/** When exiting the game, delete all the buffers, vertex arrays and texture references */
	public void delete() {
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}

		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}

		for (int texture : texs) {
			GL11.glDeleteTextures(texture);
		}
	}
}