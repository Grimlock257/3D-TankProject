package me.grimlock257.project.engine.font;

import me.grimlock257.project.engine.math.Vector2f;
import me.grimlock257.project.engine.math.Vector3f;

/** Represents a text element */
public class GUIText {
	// VAO information
	private int vaoID;
	private int vertexCount;

	// General information about the text element
	private FontFace fontFace;
	private String text;
	private int amountOfLines;
	private float fontSize;
	private float maxLineLength;
	private Vector3f color = new Vector3f(0f, 0f, 0f);
	private Vector2f position;
	private boolean isCentered = false;

	/** Create a new text element */
	public GUIText(String text, float fontSize, FontFace fontFace, Vector2f position, float maxLineLength, boolean isCentered) {
		this.text = text;
		this.fontSize = fontSize;
		this.fontFace = fontFace;
		this.position = position;
		this.maxLineLength = maxLineLength;
		this.isCentered = isCentered;
		FontEngine.addText(this);
	}

	/** Update the text to be displayed on the screen */
	public void updateText(String text) {
		this.text = text;
		remove();
		FontEngine.addText(this);
	}

	/** Remove this text element from the screen. */
	public void remove() {
		FontEngine.removeText(this);
	}

	/** Return the font face for this text element */
	public FontFace getFont() {
		return fontFace;
	}

	/** Set the color of the text element (RGB) */
	public void setColor(float r, float g, float b) {
		color.set(r, g, b);
	}

	/** Return the color of this text element */
	public Vector3f getColor() {
		return color;
	}

	/** Return the amount of lines for this text element */
	public int getNumberOfLines() {
		return amountOfLines;
	}

	/** Return the position of the text element (top left) */
	public Vector2f getPosition() {
		return position;
	}

	/** Return the VAO ID of the mesh for this text element */
	public int getMesh() {
		return vaoID;
	}

	/** Set the VAO for this text element */
	public void setVAO(int vao, int verticesCount) {
		this.vaoID = vao;
		this.vertexCount = verticesCount;
	}

	/** Return the total number of vertices for this text element */
	public int getVertexCount() {
		return this.vertexCount;
	}

	/** Return the font size of this text element */
	protected float getFontSize() {
		return fontSize;
	}

	/** Set the amount of lines this text elements uses */
	protected void setAmountOfLines(int amount) {
		this.amountOfLines = amount;
	}

	/** Return whether this text should be centred */
	protected boolean isCentered() {
		return isCentered;
	}

	/** Return the maximum line length for this text */
	protected float getMaxLineLength() {
		return maxLineLength;
	}

	/** Return the text */
	protected String getText() {
		return text;
	}
}