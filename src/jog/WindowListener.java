package jog;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class WindowListener implements WindowFocusListener, ComponentListener, java.awt.event.WindowListener {

	private int windowWidth;
	private int windowHeight;
	private int windowX;
	private int windowY;
	
	protected WindowListener()  {
		updateSize();
		updatePosition();
	}
	
	private void updateSize() {
		windowWidth = jog.Window.getWidth();
		windowHeight = jog.Window.getHeight();
	}
	
	private void updatePosition() {
		windowX = jog.Window.getPositionX();
		windowY = jog.Window.getPositionY();
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		Event.push(Event.EventType.FOCUS, true);
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		Event.push(Event.EventType.FOCUS, false);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Event.push(Event.EventType.RESIZE, windowWidth, windowHeight);
		updateSize(); 
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Event.push(Event.EventType.QUIT);
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		if (!jog.Input.isMouseDown(MouseEvent.BUTTON1)) {
			Event.push(Event.EventType.WINDOW_MOVED, jog.Window.getPositionX() - windowX, jog.Window.getPositionY() - windowY);
			updatePosition();
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {}
	@Override
	public void componentHidden(ComponentEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {} // minimised
	@Override
	public void windowDeiconified(WindowEvent e) {} // restored
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}

}
