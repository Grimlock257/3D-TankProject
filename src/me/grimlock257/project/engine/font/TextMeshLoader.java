package me.grimlock257.project.engine.font;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/** Creates the QUAD for the */
public class TextMeshLoader {
	protected static final double LINE_HEIGHT = 0.03f;
	protected static final int ASCII_SPACE = 32;

	private FNTLoader metaData;

	protected TextMeshLoader(File metaFile) {
		metaData = new FNTLoader(metaFile);
	}

	protected TextMesh createTextMesh(GUIText text) {
		ArrayList<Line> lines = createStructure(text);
		TextMesh data = createQuadVertices(text, lines);
		return data;
	}

	private ArrayList<Line> createStructure(GUIText text) {
		char[] chars = text.getText().toCharArray();
		ArrayList<Line> lines = new ArrayList<Line>();
		Line currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineLength());
		Word currentWord = new Word(text.getFontSize());
		for (char c : chars) {
			int ascii = (int) c;
			if (ascii == ASCII_SPACE) {
				boolean added = currentLine.tryToAddWord(currentWord);
				if (!added) {
					lines.add(currentLine);
					currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineLength());
					currentLine.tryToAddWord(currentWord);
				}
				currentWord = new Word(text.getFontSize());
				continue;
			}
			Glyph character = metaData.getGlyph(ascii);
			currentWord.addGlyph(character);
		}
		completeStructure(lines, currentLine, currentWord, text);
		return lines;
	}

	private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, GUIText text) {
		boolean added = currentLine.tryToAddWord(currentWord);
		if (!added) {
			lines.add(currentLine);
			currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineLength());
			currentLine.tryToAddWord(currentWord);
		}
		lines.add(currentLine);
	}

	private TextMesh createQuadVertices(GUIText text, List<Line> lines) {
		text.setAmountOfLines(lines.size());
		double curserX = 0f;
		double curserY = 0f;
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> textureCoords = new ArrayList<Float>();
		for (Line line : lines) {
			if (text.isCentered()) {
				curserX = (line.getMaxLength() - line.getLineLength()) / 2;
			}
			for (Word word : line.getWords()) {
				for (Glyph letter : word.getGlyphs()) {
					addVerticesForCharacter(curserX, curserY, letter, text.getFontSize(), vertices);
					addTexCoords(textureCoords, (float) letter.getXMinTextureCoord(), (float) letter.getYMinTextureCoord(), (float) letter.getXMaxTextureCoord(), (float) letter.getYMaxTextureCoord());
					curserX += letter.getAdvance() * text.getFontSize();
				}
				curserX += metaData.getSpaceWidth() * text.getFontSize();
			}
			curserX = 0;
			curserY += LINE_HEIGHT * text.getFontSize();
		}
		return new TextMesh(listToArray(vertices), listToArray(textureCoords));
	}

	private void addVerticesForCharacter(double curserX, double curserY, Glyph character, double fontSize, List<Float> vertices) {
		double x = curserX + (character.getXOffset() * fontSize);
		double y = curserY + (character.getYOffset() * fontSize);
		double maxX = x + (character.getWidth() * fontSize);
		double maxY = y + (character.getHeight() * fontSize);
		double properX = (2 * x) - 1;
		double properY = (-2 * y) + 1;
		double properMaxX = (2 * maxX) - 1;
		double properMaxY = (-2 * maxY) + 1;
		addVertices(vertices, (float) properX, (float) properY, (float) properMaxX, (float) properMaxY);
	}

	private static void addVertices(List<Float> vertices, float x, float y, float maxX, float maxY) {
		vertices.add((float) x);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) y);
	}

	private static void addTexCoords(List<Float> texCoords, float x, float y, float maxX, float maxY) {
		texCoords.add((float) x);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) y);
	}

	private static float[] listToArray(ArrayList<Float> listOfFloats) {
		float[] array = new float[listOfFloats.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = listOfFloats.get(i);
		}
		return array;
	}
}