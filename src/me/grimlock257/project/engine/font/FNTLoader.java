package me.grimlock257.project.engine.font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import me.grimlock257.project.utils.managers.DisplayManager;

/** This class contains methods for loading data in from a font file */
public class FNTLoader {
	private double aspect;
	private double verticalPerPixelSize;
	private double horizontalPerPixelSize;
	private double spaceWidth;
	private int[] padding;
	private int paddingWidth;
	private int paddingHeight;

	private HashMap<Integer, Glyph> glyphData = new HashMap<Integer, Glyph>();

	private BufferedReader reader;
	private HashMap<String, String> values = new HashMap<String, String>();

	/** Opens a font file in preparation for reading */
	protected FNTLoader(File file) {
		this.aspect = (double) DisplayManager.getVidmode().width() / (double) DisplayManager.getVidmode().height();

		// Try open FNT file, if failure, catch will run, printing error
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			// Display error messages to the console
			System.err.println("FNT Loader: Couldn't load the file");
			e.printStackTrace();
		}

		loadPadding();
		loadLineSizes();
		int imageWidth = getValueOfVariable("scaleW");
		storeGlyphData(imageWidth);

		// Try close the reader, if failure, catch will run, printing error
		try {
			reader.close();
		} catch (IOException e) {
			// Display error messages to the console
			System.err.println("FNT Loader: Couldn't close the reader");
			e.printStackTrace();
		}
	}

	/** Return the data associated with the provided ASCII code */
	protected Glyph getGlyph(int ascii) {
		return glyphData.get(ascii);
	}

	/** Return the width of a space */
	protected double getSpaceWidth() {
		return spaceWidth;
	}

	/** Read the next line, storing the information in values array */
	private boolean parseNextLine() {
		values.clear();
		String line = null;

		// Try read FNT file, if failure, catch will run, printing error
		try {
			line = reader.readLine();
		} catch (IOException e) {
			// Display error messages to the console
			System.err.println("FNT Loader: Couldn't read the file");
			e.printStackTrace();
		}

		if (line == null)
			return false;

		for (String part : line.split(" ")) {
			String[] valuePairs = part.split("=");
			if (valuePairs.length == 2) {
				values.put(valuePairs[0], valuePairs[1]);
			}
		}

		return true;
	}

	/** Gets the {@code int} value of the variable with a certain name on the current line.
	 * @param variable - the name of the variable.
	 * @return The value of the variable. */
	private int getValueOfVariable(String variable) {
		return Integer.parseInt(values.get(variable));
	}

	/** Gets the array of ints associated with a variable on the current line.
	 * @param variable - the name of the variable.
	 * @return The int array of values associated with the variable. */
	private int[] getValuesOfVariable(String variable) {
		String[] numbers = values.get(variable).split(",");
		int[] actualValues = new int[numbers.length];
		for (int i = 0; i < actualValues.length; i++) {
			actualValues[i] = Integer.parseInt(numbers[i]);
		}
		return actualValues;
	}

	/** Load the padding data from the FNT file */
	private void loadPadding() {
		parseNextLine();
		this.padding = getValuesOfVariable("padding");
		this.paddingWidth = padding[1] + padding[3];
		this.paddingHeight = padding[0] + padding[2];
	}

	/** Load line size data from the FNT file */
	private void loadLineSizes() {
		parseNextLine();
		int lineHeightPixels = getValueOfVariable("lineHeight") - paddingHeight;
		verticalPerPixelSize = TextMeshLoader.LINE_HEIGHT / (double) lineHeightPixels;
		horizontalPerPixelSize = verticalPerPixelSize / aspect;
	}

	/** Load the glyphs and store them as glyphs */
	private void storeGlyphData(int imageWidth) {
		parseNextLine(); // Skip the line beginning with page
		parseNextLine(); // Skip the line beginning with chars
		while (parseNextLine()) {
			Glyph glyph = storeGlyph(imageWidth);
			if (glyph != null) {
				glyphData.put(glyph.getASCIICode(), glyph);
			}
		}
	}

	/** Loads all the data about one character in the texture atlas and converts it all from 'pixels' to 'screen-space'
	 * before storing. The effects of padding are also removed from the data.
	 * @param imageSize - the size of the texture atlas in pixels.
	 * @return The data about the character. */
	private Glyph storeGlyph(int imageSize) {
		int assciCode = getValueOfVariable("id");
		if (assciCode == TextMeshLoader.ASCII_SPACE) {
			this.spaceWidth = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize;
			return null;
		}
		double xTextureCoordinate = ((double) getValueOfVariable("x") + (padding[1] - 3)) / imageSize;
		double yTextureCoordinate = ((double) getValueOfVariable("y") + (padding[0] - 3)) / imageSize;
		int width = getValueOfVariable("width") - (paddingWidth - (2 * 3));
		int height = getValueOfVariable("height") - ((paddingHeight) - (2 * 3));
		double quadWidth = width * horizontalPerPixelSize;
		double quadHeight = height * verticalPerPixelSize;
		double textureWidth = (double) width / imageSize;
		double textureHeight = (double) height / imageSize;
		double xOffset = (getValueOfVariable("xoffset") + padding[1] - 3) * horizontalPerPixelSize;
		double yOffset = (getValueOfVariable("yoffset") + (padding[0] - 3)) * verticalPerPixelSize;
		double advance = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize;
		return new Glyph(assciCode, xTextureCoordinate, yTextureCoordinate, textureWidth, textureHeight, xOffset, yOffset, quadWidth, quadHeight, advance);
	}
}