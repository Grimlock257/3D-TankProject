package me.grimlock257.project.terrains;

import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.engine.render.VAOLoader;
import me.grimlock257.project.engine.texture.ModelTexture;
import me.grimlock257.project.models.ModelMesh;

/** This class represents a terrain tile, which are generated here */
public class Terrain {
	private static final float TERRAIN_LENGTH = 200; // How many units long the terrain is
	private static final int VERTEX_AMOUNT = 3; // How many vertices along a terrain edge
	private static final int SQUARE_FACE_AMOUNT = VERTEX_AMOUNT - 1;

	private float xPos;
	private float zPos;
	private ModelMesh modelMesh;
	private ModelTexture texture;
	
	private float frictionCoefficient;

	public Terrain(int terrainGridX, int terrainGridZ, VAOLoader vaoLoader, ModelTexture texture, float frictionCoefficient) {
		this.xPos = terrainGridX * TERRAIN_LENGTH;
		this.zPos = terrainGridZ * TERRAIN_LENGTH;
		this.texture = texture;
		this.modelMesh = generateTerrain(vaoLoader);
		
		this.frictionCoefficient = frictionCoefficient;
	}

	/** Generate the terrain tile, this essentially generates the same data that is stored in an OBJ file */
	private ModelMesh generateTerrain(VAOLoader loader) {
		int totalVertexAmount = VERTEX_AMOUNT * VERTEX_AMOUNT; // Total amount of vertices per terrain tile

		// Initialise the arrays that hold the information about the generated OBJ file
		float[] v = new float[totalVertexAmount * 3]; // *3 as vectors
		float[] vn = new float[totalVertexAmount * 3]; // *3 as vectors
		float[] vt = new float[totalVertexAmount * 2]; // *2 as vectors

		// Terrain tiles are square, made of triangles, triangle require 6 indices. hence 6 * amount of vertices squared
		int[] indices = new int[6 * SQUARE_FACE_AMOUNT * SQUARE_FACE_AMOUNT];

		// Generate vertex information about the terrain
		// Current vertex is the vertex we are currently generating
		for (int z = 0, currentVertex = 0; z < VERTEX_AMOUNT; z++) { // Loop through the z row
			for (int x = 0; x < VERTEX_AMOUNT; x++, currentVertex++) { // Loop through the x columns
				// Generate the x,y,z of the vertex
				v[currentVertex * 3] = (float) (TERRAIN_LENGTH / SQUARE_FACE_AMOUNT) * x;
				v[currentVertex * 3 + 1] = 0;
				v[currentVertex * 3 + 2] = (float) (TERRAIN_LENGTH / SQUARE_FACE_AMOUNT) * z;

				// Generate the normals of the vertex (x, y, z)
				vn[currentVertex * 3] = 0;
				vn[currentVertex * 3 + 1] = 1;
				vn[currentVertex * 3 + 2] = 0;

				// Generate the texture coordinates (x and y)
				vt[currentVertex * 2] = (float) x / SQUARE_FACE_AMOUNT;
				vt[currentVertex * 2 + 1] = (float) z / SQUARE_FACE_AMOUNT;
			}
		}

		// Generate indices data about the terrain
		for (int z = 0, currentIndice = 0; z < SQUARE_FACE_AMOUNT; z++) {
			for (int x = 0; x < SQUARE_FACE_AMOUNT; x++) {
				int indice0 = (z * VERTEX_AMOUNT) + x; // Top Left
				int indice1 = ((z + 1) * VERTEX_AMOUNT) + x; // Bottom Left
				int indice2 = indice1 + 1; // Bottom Right
				int indice3 = indice0 + 1; // Top Right

				indices[currentIndice++] = indice0;
				indices[currentIndice++] = indice1;
				indices[currentIndice++] = indice2;
				indices[currentIndice++] = indice2;
				indices[currentIndice++] = indice3;
				indices[currentIndice++] = indice0;
			}
		}

		Vector3f[] minMax = { new Vector3f(this.xPos, 0, this.zPos), new Vector3f(this.xPos + TERRAIN_LENGTH, 0, this.zPos + TERRAIN_LENGTH) };
		return loader.storeInVAO(v, vt, vn, indices, minMax);
	}

	/** Return the terrain x value */
	public float getX() {
		return xPos;
	}

	/** Return the terrain z value */
	public float getZ() {
		return zPos;
	}

	/** Return the terrain length */
	public float getLength() {
		return TERRAIN_LENGTH;
	}
	
	/** Return the friction coefficient of the terrain */
	public float getFrictionCoefficient() {
		return frictionCoefficient;
	}

	/** Return the model mesh */
	public ModelMesh getModelMesh() {
		return modelMesh;
	}

	/** Return the texture of the terrain */
	public ModelTexture getTexture() {
		return texture;
	}
}