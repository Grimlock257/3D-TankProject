package me.grimlock257.project.engine.font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.grimlock257.project.engine.render.VAOLoader;

public class FontEngine {
	private static VAOLoader vaoLoader;
	private static HashMap<FontFace, ArrayList<GUIText>> texts = new HashMap<FontFace, ArrayList<GUIText>>();

	public static void init(VAOLoader vaoLoaderIn) {
		vaoLoader = vaoLoaderIn;
	}

	public static void addText(GUIText text) {
		FontFace font = text.getFont();
		TextMesh data = font.loadText(text);
		int vao = vaoLoader.storeInVAO(data.getPositions(), data.getTextureCoords());
		text.setVAO(vao, data.getVertexCount());
		ArrayList<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}

	/** Remove a text element from the screen */
	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}

	/** Return all the texts that are on the screen */
	public static HashMap<FontFace, ArrayList<GUIText>> getTexts() {
		return texts;
	}
}