package me.grimlock257.project.engine.font;

import java.util.ArrayList;

/** Represents a word (consists of glyphs) */
public class Word {
	// Variables about the current word
	private ArrayList<Glyph> glyphs = new ArrayList<Glyph>();
	private double width = 0;
	private double fontSize;

	protected Word(double fontSize) {
		this.fontSize = fontSize;
	}

	/** Add a glyph to the current word and increase the width of this word based on the glyph added */
	protected void addGlyph(Glyph glyph) {
		glyphs.add(glyph);
		width += glyph.getAdvance() * fontSize;
	}

	/** Return the glyphs that make up this word */
	protected ArrayList<Glyph> getGlyphs() {
		return glyphs;
	}

	/** Return the width of the word */
	protected double getWidth() {
		return width;
	}
}