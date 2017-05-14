package me.grimlock257.project.engine.render.renderers;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import me.grimlock257.project.engine.font.FontFace;
import me.grimlock257.project.engine.font.GUIText;
import me.grimlock257.project.engine.shader.shaders.FontShader;

/** This class deals with rendering fonts */
public class FontRenderer {
	private FontShader shader;

	public FontRenderer(FontShader shader) {
		this.shader = shader;
	}

	/** Render any text to the screen */
	public void render(HashMap<FontFace, ArrayList<GUIText>> texts) {
		for (FontFace font : texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for (GUIText text : texts.get(font)) {
				bindVAO(text);
				shader.setUniformTextColour(text.getColor());
				shader.setUniformTranslationVec2f(text.getPosition());
				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
				unbindVAO();
			}
		}
	}

	/** Bind the VAO for the current collider, and enable the relevant attribute slots so OpenGL can use them */
	private void bindVAO(GUIText text) {
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}

	/** Disable the relevant attribute slots and then unbind the VAO */
	private void unbindVAO() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
}