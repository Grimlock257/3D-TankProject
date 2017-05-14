package me.grimlock257.project.engine.render.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import me.grimlock257.project.engine.math.Matrix4f;
import me.grimlock257.project.engine.shader.shaders.EntityShader;
import me.grimlock257.project.engine.texture.ModelTexture;
import me.grimlock257.project.entities.Entity;
import me.grimlock257.project.models.ModelMesh;
import me.grimlock257.project.models.ModelTextured;
import me.grimlock257.project.utils.MatrixUtils;

/** This class deals with rendering entities */
public class EntityRenderer {
	private EntityShader shader;

	protected EntityRenderer(EntityShader shader) {
		this.shader = shader;
	}

	/** Render the entities in the world */
	protected void render(HashMap<ModelTextured, ArrayList<Entity>> entities) {
		// Loop through all of the types of model (e.g all entities that use the tankBarrel model)
		for (ModelTextured model : entities.keySet()) {
			bindVAO(model);
			List<Entity> batch = entities.get(model);

			// Render each entity which uses the currently bound model
			for (Entity entity : batch) {
				createTransformationMatrix(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModelMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}

			unbindVAO();
		}
	}

	/** Bind the VAO for the current model, and enable the relevant attribute slots so OpenGL can use them */
	private void bindVAO(ModelTextured entity) {
		// Get the modelMesh of the current entity
		ModelMesh modelMesh = entity.getModelMesh();

		// Enable the relevant attribute locations
		GL30.glBindVertexArray(modelMesh.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		// Tells OpenGL to activate texture bank 0 and bind the texture to bank 0
		ModelTexture texture = entity.getTexture();
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

	/** Create a transformation matrix so that the entity can be rendered in the correct location in the world */
	private void createTransformationMatrix(Entity entity) {
		Matrix4f transformationMatrix = MatrixUtils.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		shader.setUniformTransformationMat4f(transformationMatrix);
	}
}