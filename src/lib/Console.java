package lib;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;

public class Console implements jog.Event.KeyboardEventHandler {

	public interface Action {
		public void act(String... args);
	}
	
	public static final int FONT_SIZE = 24;
	public static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);
	
	private final int activationKey = KeyEvent.VK_BACK_QUOTE;
	private final int runKey = KeyEvent.VK_ENTER;
	private final int stayModifier = KeyEvent.VK_CONTROL;
	private final int keepModifier = KeyEvent.VK_SHIFT;
	private final char[] ignoreList = new char[] { '\r', '\n', '`', '\b', 127 };
	private final HashMap<String, Action> actions;
	
	private boolean active;
	private StringBuilder currentText;
	private int caretPosition;
	
	public Console() {
		this.actions = new HashMap<String, Action>();
		this.currentText = new StringBuilder();
		reset();
		hide();
	}
	
	public boolean isActive() {
		return active;
	}
	
	private void hide() {
		active = false;
	}
	
	private void reset() {
		currentText.delete(0, currentText.length());
		caretPosition = currentText.length();
	}
	
	public void addAction(String key, Action action) {
		actions.put(key, action);
	}

	@Override
	public void keyReleased(int key) {
		if (key == activationKey) active = !active;
		if (!active) return;
		if (key == runKey) {
			act();
		}
		if (key == KeyEvent.VK_BACK_SPACE && caretPosition > 0) {
			currentText.delete(caretPosition - 1, caretPosition);
			caretPosition --;
		}
		if (key == KeyEvent.VK_DELETE && caretPosition < currentText.length()) {
			currentText.delete(caretPosition, caretPosition + 1);
		}
		if (key == KeyEvent.VK_LEFT && caretPosition > 0) caretPosition --;
		if (key == KeyEvent.VK_RIGHT && caretPosition < currentText.length()) caretPosition ++;
	}
	
	private void act() {
		String line = currentText.toString();
		String tokens[] = line.split(" ");
		String action = tokens[0];
		if (isRegisteredAction(action)) {
			String args[] = Arrays.copyOfRange(tokens, 1, tokens.length);
			actions.get(action).act(args);
		}
		if (!jog.Input.isKeyDown(keepModifier)) {
			reset();
		}
		if (!jog.Input.isKeyDown(stayModifier)) {
			hide();
		}
	}
	
	private boolean isRegisteredAction(String action) {
		return actions.containsKey(action);
	}

	@Override
	public void keyPressed(int key) {}
	
	public void draw() {
		if (!active) return;
		final int height = 48;
		final int padding = 8;
		final int y = jog.Window.getHeight() - height;
		String text = currentText.toString();
		jog.Graphics.setColour(0, 0, 0, 128);
		jog.Graphics.rectangle(true, 0, y, jog.Window.getWidth(), height);
		jog.Graphics.setColour(255, 255, 255, 128);
		jog.Graphics.setFont(FONT);
		jog.Graphics.print(text, padding, y);
		
		int caretX = jog.Graphics.getFontWidth(text.substring(0, caretPosition));
		jog.Graphics.print("|", caretX + padding - 6, y);
	}

	@Override
	public void keyTyped(char text) {
		if (!active) return;
		for (char c : ignoreList) if (c == text) return;
		currentText.insert(caretPosition, text);
		caretPosition ++;
	}

}
