package lib.gui;

public class Window {
	
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
	
	public final static int TITLE_HEIGHT = 24;
	
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected String title = "Window";
	
	public Window(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void mousePressed(int mx, int my, int key) {
		
	}
	
	public void mouseReleased(int mx, int my, int key) {
		
	}
	
	public void update() {
		
	}
	
	public void draw() {
		int x = this.x;
		int y = this.y;
		int w = this.width;
		int h = this.height;
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
