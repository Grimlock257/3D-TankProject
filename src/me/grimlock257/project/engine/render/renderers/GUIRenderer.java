package me.grimlock257.project.engine.render.renderers;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import me.grimlock257.project.Main;
import me.grimlock257.project.engine.math.Matrix4f;
import me.grimlock257.project.engine.shader.shaders.GUIShader;
import me.grimlock257.project.engine.texture.GUITexture;
import me.grimlock257.project.models.ModelMesh;
import me.grimlock257.project.utils.MatrixUtils;
import me.grimlock257.project.utils.managers.ModelManager;

/** This class deals with rendering GUIs */
public class GUIRenderer {
	private final ModelMesh QUAD;

	private GUIShader shader;

	protected GUIRenderer(GUIShader shader) {
		QUAD = ModelManager.retrieveQuad();
		this.shader = shader;
	}

	/** Render the GUIs onto the screen */
	protected void render(List<GUITexture> guis) {
		bindVAO();
		Main.getRenderer().enableBlend();
		Main.getRenderer().disableDepthTest();
		for (GUITexture gui : guis) {
			// Tells OpenGL to activate texture bank 0 and bind the texture to bank 0
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			createTransformationMatrix(gui);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());
		}
		Main.getRenderer().enableDepthTest();
		Main.getRenderer().disableBlend();
		unbindVAO();
	}

	/** Bind the VAO for the current collider, and enable the relevant attribute slots so OpenGL can use them */
	private void bindVAO() {
		GL30.glBindVertexArray(QUAD.getVaoID());
		GL20.glEnableVertexAttribArray(0);
	}

	/** Disable the relevant attribute slots and then unbind the VAO */
	private void unbindVAO() {
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	/** Create a transformation matrix so that the GUI can be rendered in the correct location on the screen */
	private void createTransformationMatrix(GUITexture gui) {
		Matrix4f matrix = MatrixUtils.createTransformationMatrix(gui.getPosition(), gui.getScale());
		shader.setUniformTransformationMat4f(matrix);
	}
}