package lib.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.ArrayList;

import jog.Graphics;

public class TextBox {
	
	public static final char NEWLINE = '\n';
	
	private int x, y, width, height;
	private int horizontalPadding;
	private int verticalPadding;
	private int lineSpacing;
	
	private double typeDelay;
	private double typingtimer;
	private double delayTimer;
	private boolean isTyping;
	private boolean isDelaying;
	
	private String text;
	private int currentIndex;
	private HashMap<Integer, Color> colourChanges;
	private HashMap<Integer, Double> delays;
	private HashMap<Integer, Double> speedChanges;
	private ArrayList<Integer> clearTexts;

	public TextBox(int xPos, int yPos, int w, int h, int spacing, double charWait) {
		x = xPos;
		y = yPos;
		width = w;
		height = h;
		horizontalPadding = 8;
		verticalPadding = 16;
		lineSpacing = spacing;
		typingtimer = 0;
		delayTimer = 0;
		typeDelay = charWait;
		isTyping = false;
		isDelaying = false;
		text = "";
		currentIndex = 0;
		colourChanges = new HashMap<Integer, Color>();
		delays = new HashMap<Integer, Double>();
		speedChanges = new HashMap<Integer, Double>();
		clearTexts = new ArrayList<Integer>();
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public int width() {
		return width;
	}
	
	public int height() {
		return height;
	}
	
	public boolean isTyping() {
		return isTyping;
	}
	
	public void clear() {
		clearTexts.add(text.length());
	}

	private void reset() {
		HashMap<Integer, Color> newColours = new HashMap<Integer, Color>();
		for (int time : colourChanges.keySet()) {
			if (time - currentIndex >= 0) newColours.put(time - currentIndex, colourChanges.get(time));
		}
		colourChanges = newColours;
		
		HashMap<Integer, Double> newDelays = new HashMap<Integer, Double>();
		for (int time : delays.keySet()) {
			if (time - currentIndex >= 0) newDelays.put(time - currentIndex, delays.get(time));
		}
		delays = newDelays;
		
		HashMap<Integer, Double> newSpeeds = new HashMap<Integer, Double>();
		for (int time : speedChanges.keySet()) {
			if (time - currentIndex >= 0) newSpeeds.put(time - currentIndex, speedChanges.get(time));
		}
		delays = newSpeeds;
		
		ArrayList<Integer> newClears = new ArrayList<Integer>();
		for (int time : clearTexts) {
			if (time - currentIndex >= 0) newClears.add(time - currentIndex);
		}
		clearTexts = newClears;
		
		text = text.substring(currentIndex);
		currentIndex = 0;
	}

	public void addText(String newText) {
		text += newText;
		isTyping = true;
	}
	
	public void addLine(String text) {
		addText(text);
		newline();
	}
	
	public void newline() {
		text += NEWLINE;
	}
	
	public void setColour(int r, int g, int b, int a) {
		colourChanges.put(text.length(), new Color(r, g, b, a));
	}
	public void setColour(int r, int g, int b) { setColour(r, g, b, 255); }
	
	public void delay(double delay) {
		delays.put(text.length(), delay);
	}
	
	public void setSpeed(double wait) {
		speedChanges.put(text.length(), wait);
	}
	
	public void update(double dt) {
		// Update delay
		if (isDelaying && delayTimer <= 0) {
			isDelaying = false;
		} else if (isDelaying) {
			delayTimer = Math.max(0, delayTimer - dt);
			return;
		}
		//
		if (currentIndex == text.length()) {
			isTyping = false;
		}
		if (!isTyping) return;
		// Update 
		typingtimer += dt;
		while (typingtimer >= typeDelay) {
			typingtimer -= typeDelay;
			currentIndex += 1;
			
			boolean shouldReturn = false;
			if (speedChanges.containsKey(currentIndex)) {
				typeDelay = speedChanges.get(currentIndex);
			}
			if (delays.containsKey(currentIndex)) {
				isDelaying = true;
				delayTimer = delays.get(currentIndex);
				shouldReturn = true;
			}
			if (clearTexts.contains(currentIndex)) {
				reset();
				shouldReturn = true;
			}
			if (shouldReturn) return;
			
			if (currentIndex >= text.length()) {
				currentIndex = text.length();
				isTyping = false;
			} 
		}
	}
	
	public void draw() {
		int charX = horizontalPadding;
		int charY = verticalPadding;
		for (int i = 0; i < currentIndex; i ++) {
			if (colourChanges.containsKey(i)) {
				Graphics.setColour(colourChanges.get(i));
			}
			char letter = text.charAt(i);
			if (letter == NEWLINE) {
				charY += lineSpacing;
				charX = horizontalPadding;
			} else {
				// Wrap if the (rest of the) word would be too long for the line.
				String word = "";
				for (int n = i; n < text.length() && text.charAt(n) != ' '; n ++) {
					word += text.charAt(n);
				}
				if (charX + Graphics.getFontWidth(word) >= width - horizontalPadding) {
					charY += lineSpacing;
					charX = horizontalPadding;
				}
				// Draw the character
				Graphics.print(text.substring(i, i+1), x + charX, y + charY);
				charX += Graphics.getFontWidth(text.substring(i, i+1));
			}
		}
	}

}
