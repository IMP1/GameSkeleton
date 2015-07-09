package scn;

public abstract class Scene implements jog.Event.EventHandler {
	
	abstract public void start();
	
	abstract public void update(double dt);
	
	abstract public void draw();
	
	abstract public void close();
	
	@Override
	public void keyPressed(int key) {}
	
	@Override
	public void keyReleased(int key) {}
	
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
	public void mouseMoved(int x, int y) {} 

	@Override
	public boolean quit() { return false; }
		
}
