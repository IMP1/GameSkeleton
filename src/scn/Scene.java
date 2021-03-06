package scn;

import jog.Event.*;

public abstract class Scene implements KeyboardEventHandler, MouseEventHandler, WindowEventHandler {
	
	public void start() {}
	
	public void update(double dt) {}
	
	public void draw() {}
	
	public void close() {}
	
	@Override
	public void keyPressed(int key) {}
	
	@Override
	public void keyReleased(int key) {}
	
	@Override
	public void keyTyped(char text) {}
	
	@Override
	public void mousePressed(int mouseX, int mouseY, int mouseKey) {}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseKey) {}

	@Override
	public void mouseScrolled(int x, int y, int scroll) {}

	@Override
	public void focus(boolean gained) {}

	@Override
	public void mouseFocus(boolean gained) {}

	@Override
	public void resize(int oldWidth, int oldHeight) {}
	
	@Override
	public void move(int dx, int dy) {}

	@Override
	public void mouseMoved(int x, int y) {} 

	@Override
	public boolean quit() { return false; }
		
}
