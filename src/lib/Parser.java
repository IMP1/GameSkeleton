package lib;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public class Parser {
	
	public static final String NEWLINE = System.getProperty("line.separator");
	
	public static boolean warnings = true;
	
	protected char[] input;
	protected int position;
	protected String[] lines;
	protected int line;

	protected Parser(String input) {
		this.input = input.toCharArray();
		this.position = 0;
		this.line = 1;
		this.lines = input.split("\\n|\\r|" + NEWLINE);
		consumeWhitespace();
	}

	protected void consumeWhitespace() {
		while (!eof() && Character.isWhitespace(nextChar())) {
			consumeChar();
		}
	}
	
	protected boolean eof() {
		return position >= input.length;
	}

	protected boolean startsWith(String text) {
		char[] nextChars = Arrays.copyOfRange(input, position, position + text.length());
		boolean equal = new String(nextChars).equals(text);
		return equal;
	}
	
	protected boolean startsWith(char c) {
		return nextChar() == c;
	}
	
	protected boolean eol() {
		return isNewLine();
	}
	
	protected boolean isNewLine() {
		return startsWith(NEWLINE) || nextChar() == '\n' || nextChar() == '\r';
	}
	
	protected int consumeInteger() {
		StringBuilder result = new StringBuilder();
		if (startsWith('-')) result.append(consumeChar());
		while (!eof() && Character.isDigit(nextChar())) {
			result.append(consumeChar());
		}
		return Integer.parseInt(result.toString());
	}
	
	protected char consumeChar() {
		if (isNewLine()) {
			line ++;
		}
		char currentChar = input[position];
		position ++;
		return currentChar;
	}
	
	protected char nextChar() {
		if (position >= input.length) {
			String line = new String(Arrays.copyOfRange(input, position-10, position-1));
			System.out.println("\"" + line + "\"");
		}
		return input[position];
	}
	
	protected void consumeString(String word) {
		consumeString(word, "Was expecting, but did not get, the word '" + word + "'.");
	}
	protected void consumeString(String word, String errorMessage) {
		char[] chars = word.toCharArray();
		for (char c : chars) {
			makeSure(consumeChar() == c, errorMessage);
		}
	}
	
	protected void consumeStringIfThere(String word) {
		if (startsWith(word))
			consumeString(word);
	}
	
	/**
	 * Custom assert method. Throws a RuntimeException should the condition not be met.
	 * @param condition the condition that must hold.
	 * @param message the error message to print should the assertion fail.
	 */
	protected void makeSure(boolean condition, String message) {
		if (!condition) {
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append(message).append("\n").append("Line #").append(line).append("\n");
			if (line >= 1) {
				errorMessage.append(line-1).append(": ").append(lines[line-1]).append("\n");
			}
			errorMessage.append(line).append(": ").append(lines[line]).append("\n");
			if (line < lines.length - 1) {
				errorMessage.append(line+1).append(": ").append(lines[line+1]).append("\n");
			}
			throw new RuntimeException(errorMessage.toString());
		}
	}
	
	public void printContext(String identifier, int width) {
		System.out.println(); System.out.println();
		int from = Math.max(0, position - width);
		int to = Math.min(position + width, input.length-1);
		System.out.print(Arrays.copyOfRange(input, from, position));
		System.out.print(identifier + input[position] + identifier);
		System.out.print(Arrays.copyOfRange(input, position + 1, to));
		System.out.println(); System.out.println(); System.out.println();
	}
	
	public static String loadURLContents(URL url) {
		try {
			InputStream is = url.openStream();
			int ptr = 0;
			StringBuffer buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
}
