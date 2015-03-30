package lib.gui;

import java.awt.event.MouseEvent;

public class WindowMovable extends Window {
	
	protected class Drag {
		int x, y, dx, dy;
		boolean active;
		protected Drag() {
			x = 0;
			y = 0;
			dx = 0;
			dy = 0;
			active = false;
		}
	}
	
	public final static int HOVER_LEEWAY = 4; 
	
	protected Drag drag;
	
	public WindowMovable(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.drag = new Drag();
	}
	
	public void mousePressed(int mx, int my, int key) {
		if (!hover(mx, my)) return;
		if (key == MouseEvent.BUTTON1) {
			handleMoving(mx, my);
		}
	}
	
	protected boolean hover(int mx, int my) {
		return (mx >= x - HOVER_LEEWAY || mx <= x + width + HOVER_LEEWAY || my >= y - HOVER_LEEWAY || my <= y + width + HOVER_LEEWAY);
	}

	protected boolean hoverMove(int mx, int my) {
		return (mx >= x && mx <= x + width && my >= y && my <= y + TITLE_HEIGHT) && hover(mx, my);
	}
	
	private void handleMoving(int mx, int my) {
		if (drag.active) return;
		if (mx >= x && mx <= x + width && my >= y && my <= y + TITLE_HEIGHT) {
			drag.active = true;
			drag.x = mx;
			drag.y = my;
			drag.dx = drag.dy = 0;
			jog.Window.setMouseCursor(java.awt.Cursor.MOVE_CURSOR);
		}
	}
	
	public void mouseReleased(int mx, int my, int key) {
		if (drag.active) {
			x += drag.dx;
			y += drag.dy;
		}
		jog.Window.setMouseCursor();
		drag.active = false;
	}
	
	public void update() {
		int mx = jog.Input.getMouseX();
		int my = jog.Input.getMouseY();
		if (drag.active) {
			drag.dx = mx - drag.x;
			drag.dy = my - drag.y;
		} else if (hoverMove(mx, my)) {
			jog.Window.setMouseCursor(java.awt.Cursor.MOVE_CURSOR);
		}
	}
	
	public void draw() {
		int x = this.x;
		int y = this.y;
		int w = this.width;
		int h = this.height;
		if (drag.active) {
			x += drag.dx;
			y += drag.dy;
		}
		jog.Graphics.setColour(0, 0, 0, 192);
		jog.Graphics.rectangle(false, x - 1, y - 1, w + 2, h + 2);
		jog.Graphics.rectangle(false, x, y, w + 2, h + 2);
		jog.Graphics.setColour(32, 32, 32);
		jog.Graphics.rectangle(true, x, y, w, h);
		jog.Graphics.setColour(255, 255, 255);
		jog.Graphics.rectangle(false, x, y, w, h);
		jog.Graphics.rectangle(false, x, y, w, TITLE_HEIGHT);
		jog.Graphics.print(title, x + 4, y + 4);
	}

}
