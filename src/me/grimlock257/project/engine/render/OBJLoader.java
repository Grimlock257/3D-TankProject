package me.grimlock257.project.engine.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import me.grimlock257.project.engine.math.Vector2f;
import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.models.ModelMesh;

/** This class contains methods for loading data in from an OBJ file to store in a Vertex Array Object (VAO) using the
 * VAOLoader class and returning this as a ModelMesh */
public class OBJLoader {
	// Flags that exist within the OBJ file. These are used to determine what a line tells us
	private final String OBJ_GEOMETRY_VERTEX = "v";
	private final String OBJ_TEXTURE_VERTEX = "vt";
	private final String OBJ_NORMAL_VERTEX = "vn";
	private final String OBJ_FACE = "f";

	// Create lists so that we can dump our data here while extracting data from the OBJ file
	// First three named after the tokens, listed at beginning of file
	private ArrayList<Vector3f> v;
	private ArrayList<Vector2f> vt;
	private ArrayList<Vector3f> vn;
	private ArrayList<Integer> indices;

	// These will be the final arrays returned as the reader is designed to use float arrays
	private float[] finalVertices;
	private float[] finalNormals;
	private float[] finalTextures;
	private int[] finalIndices;

	// Array has 2 elements, each a Vector3f. First is minimum x, y, z values, second is maximum x, y, z values
	private Vector3f[] minMax;

	/** Loads a OBJ file and returns a ModelMesh. Extract the vertices, texture coordinates, vertex normals and face
	 * information (from which the indices array is created) from the file and store the information in relevant arrays
	 * so that this information can be passed to VAOLoader to be stored in memory */
	public ModelMesh readObjModelFile(String fileName, VAOLoader vaoLoader) {
		initialiseArrays();

		FileReader fileReader = null;

		// Try open OBJ file, if failure, catch will run, printing error and exiting program
		try {
			fileReader = new FileReader(new File("res/models/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			// Display error messages to the console
			System.err.println("OBJ Loader: Couldn't load the file");
			e.printStackTrace();
			System.exit(1);
		}

		// Create a buffered reader so file contents can be read and stored somewhere
		BufferedReader reader = new BufferedReader(fileReader);
		String line;

		// Try read OBJ, if failure, catch will run, printing error and exiting program
		try {
			extractData: while ((line = reader.readLine()) != null) {
				String[] currentLine = line.split(" "); // Split line by spaces as for example: "v 1.000000 -1.000000
														// -1.000000", each part separated by spaces, this creates a
														// string array where element [0] is the token (v in this case),
														// [1] is x, [2] is y, [3] is z

				// Read each line, determine what it starts with so we know what array to put our data in, once we've
				// hit "f" we must've read in all our data as "f" is the last section of information in the OBJ format
				// file, so break out of this loop as we will process lines beginning with "f" in a later loop
				switch (currentLine[0]) {
					case OBJ_GEOMETRY_VERTEX:
						Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
						v.add(vertex);

						updateMinMax(vertex);
						break;
					case OBJ_TEXTURE_VERTEX:
						Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
						vt.add(texture);
						break;
					case OBJ_NORMAL_VERTEX:
						Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
						vn.add(normal);
						break;
					case OBJ_FACE:
						finalVertices = new float[v.size() * 3]; // Multiply by 3 as vertices are 3D vectors
						finalTextures = new float[v.size() * 2]; // Multiply by 2 as texture coordinates are 2D
						finalNormals = new float[v.size() * 3]; // Multiply by 3 as normals are 3D vectors
						break extractData;
					default:
						continue;
				}
			}

			// Read the lines beginning with "f"
			while (line != null) {
				// If we're not reading a line beginning with "f", skip and start the loop again
				if (!line.startsWith(OBJ_FACE)) {
					line = reader.readLine();
					continue;
				}

				// Read in the three vertices that make up a face
				String[] currentLine = line.split(" ");
				String[] vert1 = currentLine[1].split("/"); // Vertex 1
				String[] vert2 = currentLine[2].split("/"); // Vertex 2
				String[] vert3 = currentLine[3].split("/"); // Vertex 3

				parseVertex(vert1);
				parseVertex(vert2);
				parseVertex(vert3);

				line = reader.readLine();
			}

			reader.close();
		} catch (Exception e) {
			// Display error messages to the console
			System.err.println("OBJ Loader: Couldn't read the file");
			e.printStackTrace();
			System.exit(1);
		}

		// Add vertices to array in x, y, z format
		int vertexIndex = 0;
		for (Vector3f vertex : v) {
			finalVertices[vertexIndex++] = vertex.x;
			finalVertices[vertexIndex++] = vertex.y;
			finalVertices[vertexIndex++] = vertex.z;
		}

		// Initialise the size of our arrays now that we have read in the file and know the length of this
		finalIndices = new int[indices.size()];

		// Copy indices list to indices int array
		for (int indiceIndex = 0; indiceIndex < indices.size(); indiceIndex++) {
			finalIndices[indiceIndex] = indices.get(indiceIndex);
		}

		return vaoLoader.storeInVAO(finalVertices, finalTextures, finalNormals, finalIndices, minMax);
	}

	/** Initialise the arrays so that they are ready to be used for OBJ reading */
	private void initialiseArrays() {
		// Initialise the arrays that hold the information we extract from the OBJ File
		v = new ArrayList<Vector3f>();
		vt = new ArrayList<Vector2f>();
		vn = new ArrayList<Vector3f>();
		indices = new ArrayList<Integer>();

		// Initialise the arrays that hold the final extracted information
		finalVertices = null;
		finalNormals = null;
		finalTextures = null;
		finalIndices = null;

		// Initialise the minMax values
		minMax = new Vector3f[2];
	}

	/** Set the bounding box values, called if vertex being parsed is the first vertex */
	private void setMinMax(Vector3f vertex) {
		minMax[0] = new Vector3f(vertex); // Minimum
		minMax[1] = new Vector3f(vertex); // Maximum
	}

	/** Update the bounding box values (the minimum and maximum values) */
	private void updateMinMax(Vector3f vertex) {
		if (minMax[0] == null || minMax[1] == null) {
			setMinMax(vertex);
			return;
		}

		// Minimum
		minMax[0].x = Math.min(minMax[0].x, vertex.x);
		minMax[0].y = Math.min(minMax[0].y, vertex.y);
		minMax[0].z = Math.min(minMax[0].z, vertex.z);

		// Maximum
		minMax[1].x = Math.max(minMax[1].x, vertex.x);
		minMax[1].y = Math.max(minMax[1].y, vertex.y);
		minMax[1].z = Math.max(minMax[1].z, vertex.z);
	}

	/** Parse each vertex, puts all information in all the arrays in the correct order, not random order OBJ provides */
	private void parseVertex(String[] vertexData) {
		// vertexData in order of vertex/texture coordinate/normal vector
		int vertex = Integer.parseInt(vertexData[0]) - 1; // Minus 1 as array
		indices.add(vertex);

		Vector2f textureCoordinate = vt.get(Integer.parseInt(vertexData[1]) - 1); // Minus 1 as array
		finalTextures[vertex * 2] = textureCoordinate.x;
		finalTextures[vertex * 2 + 1] = 1 - textureCoordinate.y;

		Vector3f vertexNormal = vn.get(Integer.parseInt(vertexData[2]) - 1); // Minus 1 as array
		finalNormals[vertex * 3] = vertexNormal.x;
		finalNormals[vertex * 3 + 1] = vertexNormal.y;
		finalNormals[vertex * 3 + 2] = vertexNormal.z;
	}
}