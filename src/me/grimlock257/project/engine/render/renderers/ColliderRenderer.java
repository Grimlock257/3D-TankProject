package me.grimlock257.project.engine.render.renderers;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import me.grimlock257.project.engine.math.Matrix4f;
import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.engine.physics.rendering.Collider;
import me.grimlock257.project.engine.shader.shaders.ColliderShader;
import me.grimlock257.project.utils.MatrixUtils;

/** This class deals with rendering colliders */
public class ColliderRenderer {
	private ColliderShader shader;

	protected ColliderRenderer(ColliderShader shader) {
		this.shader = shader;
	}

	/** Render the colliders in the world */
	protected void render(List<Collider> colliders) {
		for (Collider collider : colliders) {
			bindVAO(collider);
			createTransformationMatrix(collider);
			GL11.glDrawElements(GL11.GL_QUADS, 16, GL11.GL_UNSIGNED_INT, 0);
			unbindVAO();
		}
	}

	/** Bind the VAO for the current collider, and enable the relevant attribute slots so OpenGL can use them */
	private void bindVAO(Collider collider) {
		GL30.glBindVertexArray(collider.getMesh().getVaoID());
		GL20.glEnableVertexAttribArray(0);
	}

	/** Disable the relevant attribute slots and then unbind the VAO */
	private void unbindVAO() {
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	/** Create a transformation matrix so that the collider can be rendered in the correct location in the world */
	private void createTransformationMatrix(Collider collider) {
		Matrix4f transformationMatrix = MatrixUtils.createTransformationMatrix(collider.getPosition(), new Vector3f(), 1);
		shader.setUniformTransformationMat4f(transformationMatrix);
	}
}