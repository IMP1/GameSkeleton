package jog;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {

	private static final Object keyDownMonitor = new Object();
	private static boolean[] keysDown;
	
	protected KeyboardListener() {
		keysDown = new boolean[KeyEvent.CHAR_UNDEFINED+1];
	}

	protected void setKeyDown(int key, boolean down) {
		synchronized (keyDownMonitor) {
			keysDown[key] = down;
		}
	}
	
	public boolean isKeyDown(int key) {
		synchronized (keyDownMonitor) {
			return keysDown[key];
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		setKeyDown(e.getKeyCode(), true);
		int key = e.getKeyCode();
		Event.push(Event.EventType.KEY_PRESSED, key);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		setKeyDown(e.getKeyCode(), false);
		int key = e.getKeyCode();
		Event.push(Event.EventType.KEY_RELASED, key);
	}

}
