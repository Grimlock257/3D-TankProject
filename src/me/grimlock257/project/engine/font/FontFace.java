package me.grimlock257.project.engine.font;

import java.io.File;

/** Represents a font face */
public class FontFace {
	// Variables for creating a font face QUAD
	private int textureAtlas;
	private TextMeshLoader textLoader;

	public FontFace(int textureAtlas, File fontFile) {
		this.textureAtlas = textureAtlas;
		this.textLoader = new TextMeshLoader(fontFile);
	}

	/** Returns the font texture atlas. */
	public int getTextureAtlas() {
		return textureAtlas;
	}

	/** Takes in an unloaded text and calculate all of the vertices for the quads on which this text will be rendered.
	 * The vertex positions and texture coords and calculated based on the information from the font file. */
	public TextMesh loadText(GUIText text) {
		return textLoader.createTextMesh(text);
	}
}