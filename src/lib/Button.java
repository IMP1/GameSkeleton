package lib;

import java.awt.Shape;
import java.util.ArrayList;

public abstract class Button {
	
	public interface ButtonListener {
		public void onPressed();
	}
	
	protected ArrayList<ButtonListener> listeners = new ArrayList<ButtonListener>();
	
	public void addListener(ButtonListener listener) {
		listeners.add(listener);
	}

	protected boolean disabled;
	protected boolean hover;
	protected boolean pressed;
	protected Shape activeArea;
	
	public Button(Shape activeArea) {
		this(activeArea, false);
	}
	public Button(Shape activeArea, boolean disabled) {
		this.activeArea = activeArea;
		this.disabled = disabled;
		this.hover = false;
		this.pressed = false;
	}
	
	public void setEnabled(boolean enabled) {
		this.disabled = !enabled;
	}
	
	public void update() {
		int mx = jog.Input.getMouseX();
		int my = jog.Input.getMouseY();
		update(mx, my);
	}
	
	public boolean isMouseOver(int mx, int my) {
		return activeArea.contains(mx, my);
	}
	
	public void update(int mx, int my) {
		if (disabled) return;
		if (isMouseOver(mx, my)) {
			hover = true;
		} else {
			hover = false;
			pressed = false;
		}
	}
	
	public void mousePressed(int x, int y) {
		if (disabled) return;
		if (hover) {
			pressed = true;
		}
	}
	
	public void mouseReleased(int x, int y) {
		if (disabled) return;
		if (pressed) {
			act();
			pressed = false;
		}
	}
	
	public void act() {
		if (disabled) return;
		for (ButtonListener listener : listeners) {
			listener.onPressed();
		}
	}
	
	public void draw() {
		if (disabled) {
			drawDisabled();
		} else if (pressed) {
			drawPressed();
		} else if (hover) {
			drawHover();
		} else {
			drawDefault();
		}
	}
	
	protected abstract void drawDisabled();
	
	protected abstract void drawDefault();
	
	protected abstract void drawHover();
	
	protected abstract void drawPressed();

}
