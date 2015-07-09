package jog;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseListener implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener, java.awt.event.MouseWheelListener {

	private static final Object mouseDownMonitor = new Object();
	private static final Object mouseOverMonitor = new Object();

	private static boolean[] mouseDown;
	private static boolean mouseOver;
	
	protected MouseListener() {
		mouseDown = new boolean[8];
	}

	public boolean isMouseDown(int key) {
		synchronized (mouseDownMonitor) {
			return mouseDown[key];
		}
	}

	protected void setMouseDown(int key, boolean down) {
		synchronized (mouseDownMonitor) {
			mouseDown[key] = down;
		}
	}
	
	public boolean isMouseOver() {
		synchronized (mouseOverMonitor) {
			return mouseOver;
		}
	}
	
	protected void setMouseOver(boolean over) {
		synchronized (mouseOverMonitor) {
			mouseOver = over;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		setMouseDown(e.getButton(), true);
		int button = e.getButton();
		int x = e.getX();
		int y = e.getY();
		Event.push(Event.EventType.MOUSE_PRESSED, x, y, button);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setMouseDown(e.getButton(), false);
		int button = e.getButton();
		int x = e.getX();
		int y = e.getY();
		Event.push(Event.EventType.MOUSE_RELEASED, x, y, button);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		setMouseOver(true);
		Event.push(Event.EventType.MOUSE_FOCUS, true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setMouseOver(false);
		Event.push(Event.EventType.MOUSE_FOCUS, false);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Event.push(Event.EventType.MOUSE_MOVED, x, y);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int x = e.getX();
		int y = e.getX();
		int scroll = -e.getWheelRotation();
		Event.push(Event.EventType.MOUSE_SCROLLED, x, y, scroll);
	}


}
