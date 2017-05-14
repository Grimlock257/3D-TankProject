package me.grimlock257.project.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** Methods to do with handling files */
public class FileUtils {
	private FileUtils() {
	}

	/** Read in a file and return as a string */
	public static String loadFileAsString(String file) {
		StringBuilder fileSource = new StringBuilder();

		// Try to open the file, if failure, the catch will run, printing the error and exiting the program
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			// Runs until it runs out of lines to read (must have reached the end of the file)
			while ((line = reader.readLine()) != null) {
				fileSource.append(line);
				fileSource.append("\n");
			}

			reader.close();
		} catch (IOException e) {
			// Display error messages to the console
			System.err.println("FILE READER: Could not read file: " + file);
			e.printStackTrace();
			System.exit(1);
		}

		return fileSource.toString();
	}
}