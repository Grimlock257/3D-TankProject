package me.grimlock257.project.engine.render.renderers;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import me.grimlock257.project.engine.math.Matrix4f;
import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.engine.shader.shaders.EntityShader;
import me.grimlock257.project.engine.texture.ModelTexture;
import me.grimlock257.project.models.ModelMesh;
import me.grimlock257.project.terrains.Terrain;
import me.grimlock257.project.utils.MatrixUtils;

/** This class deals with rendering terrains */
public class TerrainRenderer {
	private EntityShader shader;

	protected TerrainRenderer(EntityShader shader) {
		this.shader = shader;
	}

	/** Render the terrains in the world */
	protected void render(ArrayList<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			bindVAO(terrain);
			createTransformationMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModelMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindVAO();
		}
	}

	/** Bind the VAO for the current terrain, and enable the relevant attribute slots so OpenGL can use them */
	private void bindVAO(Terrain terrain) {
		// Get the modelMesh of the current terrain
		ModelMesh modelMesh = terrain.getModelMesh();

		// Enable the relevant attribute locations
		GL30.glBindVertexArray(modelMesh.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		// Tells OpenGL to activate texture bank 0 and bind the texture to bank 0
		ModelTexture texture = terrain.getTexture();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}

	/** Disable the relevant attribute slots and then unbind the VAO */
	private void unbindVAO() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	/** Create a transformation matrix so that the terrain can be rendered in the correct location in the world */
	private void createTransformationMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = MatrixUtils.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), new Vector3f(), 1);
		shader.setUniformTransformationMat4f(transformationMatrix);
	}
}