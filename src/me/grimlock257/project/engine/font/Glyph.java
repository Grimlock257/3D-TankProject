package me.grimlock257.project.engine.font;

/** Represents a glyph (i.e a single character) */
public class Glyph {
	// Variables about this glyph
	private int asciiCode;
	private double xMinTextureCoord;
	private double yMinTextureCoord;
	private double xMaxTextureCoord;
	private double yMaxTextureCoord;
	private double xOffset;
	private double yOffset;
	private double width;
	private double height;
	private double advance;

	/** Create a new glyph for the font face provided
	 * @param assciCode The ASCII code that relates to this glyph
	 * @param xTextureCoord The x texture coordinate (top left)
	 * @param yTextureCoord The y texture coordinate (top left)
	 * @param textureWidth The width of the glyph
	 * @param textureHeight The height of the glyph
	 * @param xOffset The x distance from cursor to left edge of glyph
	 * @param yOffset The y distance from cursor to top edge of glyph
	 * @param width The width of the glyph
	 * @param height The height of the glyph
	 * @param advance How far the cursor should move after this glyph */
	protected Glyph(int assciCode, double xTextureCoord, double yTextureCoord, double textureWidth, double textureHeight, double xOffset, double yOffset, double width, double height, double advance) {
		this.asciiCode = assciCode;
		this.xMinTextureCoord = xTextureCoord;
		this.yMinTextureCoord = yTextureCoord;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.xMaxTextureCoord = textureWidth + xTextureCoord;
		this.yMaxTextureCoord = textureHeight + yTextureCoord;
		this.advance = advance;
	}

	protected int getASCIICode() {
		return asciiCode;
	}

	protected double getXMinTextureCoord() {
		return xMinTextureCoord;
	}

	protected double getYMinTextureCoord() {
		return yMinTextureCoord;
	}

	protected double getXMaxTextureCoord() {
		return xMaxTextureCoord;
	}

	protected double getYMaxTextureCoord() {
		return yMaxTextureCoord;
	}

	protected double getXOffset() {
		return xOffset;
	}

	protected double getYOffset() {
		return yOffset;
	}

	protected double getWidth() {
		return width;
	}

	protected double getHeight() {
		return height;
	}

	protected double getAdvance() {
		return advance;
	}
}