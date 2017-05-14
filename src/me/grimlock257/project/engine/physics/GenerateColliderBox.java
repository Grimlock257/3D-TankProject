package me.grimlock257.project.engine.physics;

import me.grimlock257.project.engine.math.Vector3f;

public class GenerateColliderBox {
	private static float minX, minY, minZ, maxX, maxY, maxZ;

	public GenerateColliderBox() {
	}

	/** Set the minimum and maximum values for x, y and z to the ones passed in */
	private static void setMinMax(Vector3f[] minMax) {
		minX = minMax[0].x;
		minY = minMax[0].y;
		minZ = minMax[0].z;

		maxX = minMax[1].x;
		maxY = minMax[1].y;
		maxZ = minMax[1].z;
	}

	/** Create the vertices array for the collider box */
	public static float[] generateVertices(Vector3f[] minMax) {
		setMinMax(minMax);

		float[] verticesArray = new float[24];

		verticesArray[0] = maxX;
		verticesArray[1] = maxY;
		verticesArray[2] = maxZ;

		verticesArray[3] = maxX;
		verticesArray[4] = maxY;
		verticesArray[5] = minZ;

		verticesArray[6] = minX;
		verticesArray[7] = maxY;
		verticesArray[8] = minZ;

		verticesArray[9] = minX;
		verticesArray[10] = maxY;
		verticesArray[11] = maxZ;

		verticesArray[12] = minX;
		verticesArray[13] = minY;
		verticesArray[14] = maxZ;

		verticesArray[15] = maxX;
		verticesArray[16] = minY;
		verticesArray[17] = maxZ;

		verticesArray[18] = maxX;
		verticesArray[19] = minY;
		verticesArray[20] = minZ;

		verticesArray[21] = minX;
		verticesArray[22] = minY;
		verticesArray[23] = minZ;

		return verticesArray;
	}
}