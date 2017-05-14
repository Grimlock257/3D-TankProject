package me.grimlock257.project.engine.font;

import java.util.ArrayList;

/** Represents a word (consists of words) */
public class Line {
	// Variables about the current line
	private ArrayList<Word> words = new ArrayList<Word>();
	private double maxLength;
	private double spaceWidth;
	private double lineLength = 0;

	protected Line(double spaceWidth, double fontSize, double maxLength) {
		this.spaceWidth = spaceWidth * fontSize;
		this.maxLength = maxLength;
	}

	/** Try to add the word to line, if adding the word would exceed the maximum line length, then return false */
	protected boolean tryToAddWord(Word word) {
		double addedLength = word.getWidth();

		// If this list already contains words, then we need to account for a space
		if (words.isEmpty() == false) {
			addedLength += spaceWidth;
		}

		// If the length of the current line + the length of the word we're trying to add are less than the maximum
		// length of the line, add the world and increase the length of this line, otherwise return false as we can't
		// add the word to this line
		if (lineLength + addedLength <= maxLength) {
			words.add(word);
			lineLength += addedLength;
			return true;
		} else {
			return false;
		}
	}

	/** Return the maximum length of the line */
	protected double getMaxLength() {
		return maxLength;
	}

	/** Return the current length of the line */
	protected double getLineLength() {
		return lineLength;
	}

	/** Return the words that make up this line */
	protected ArrayList<Word> getWords() {
		return words;
	}
}