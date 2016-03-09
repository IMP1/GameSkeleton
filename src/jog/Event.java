package jog;

import java.util.ArrayList;

import run.Game;

public abstract class Event {
	
	private static Object eventMonitor = new Object();
	private static Object listenerMonitor = new Object();
	protected static ArrayList<BaseEvent> eventBuffer = new ArrayList<BaseEvent>();
	private static BaseEvent[] queue = new BaseEvent[0];
	
	private static ArrayList<KeyboardEventHandler> keyboardHandlers = new ArrayList<KeyboardEventHandler>();
	private static ArrayList<MouseEventHandler> mouseHandlers = new ArrayList<MouseEventHandler>();
	private static ArrayList<WindowEventHandler> windowHandlers = new ArrayList<WindowEventHandler>();
	
	public static void addKeyboardHandler(KeyboardEventHandler newHandler) {
		if (Game.LOGGING)
			System.out.println("Adding a keyboard handler.");
		synchronized (listenerMonitor) {
			keyboardHandlers.add(newHandler);
		}
	}
	
	public static void addMouseHandler(MouseEventHandler newHandler) {
		if (Game.LOGGING)
			System.out.println("Adding a mouse handler.");
		synchronized (listenerMonitor) {
			mouseHandlers.add(newHandler);
		}
	}
	
	public static void addWindowHandler(WindowEventHandler newHandler) {
		if (Game.LOGGING)
			System.out.println("Adding a window handler.");
		synchronized (listenerMonitor) {
			windowHandlers.add(newHandler);
		}
	}
	
	public enum EventType {
		// Keyboard
		KEY_PRESSED,
		KEY_RELASED,
		KEY_TYPED,
		// Mouse
		MOUSE_MOVED,
		MOUSE_SCROLLED,
		MOUSE_PRESSED,
		MOUSE_RELEASED,
		MOUSE_FOCUS,
		// Window
		FOCUS,
		RESIZE,
		WINDOW_MOVED,
		QUIT,
	}
	
	private static class BaseEvent {
		private final EventType type;
		private final Object[] params;
		protected BaseEvent(EventType type, Object... params) {
			this.type = type;
			this.params = params;
		}
	}
	
	public interface KeyboardEventHandler {
		public void keyPressed(int key);
		public void keyReleased(int key);
		public void keyTyped(char text);
	}
	
	public interface MouseEventHandler {
		public void mouseMoved(int x, int y);
		public void mouseScrolled(int x, int y, int scroll);
		public void mousePressed(int x, int y, int key);
		public void mouseReleased(int x, int y, int key);
	}
	
	public interface WindowEventHandler {
		public void focus(boolean gained);
		public void mouseFocus(boolean gained);
		public void resize(int oldWidth, int oldHeight);
		public void move(int dx, int dy);
		public boolean quit();
	}
	
	public static void push(EventType type, Object... params) {
		synchronized (eventMonitor) {
			eventBuffer.add(new BaseEvent(type, params));
		}
	}
	
	public static void pump() {
		synchronized (eventMonitor) {
			queue = eventBuffer.toArray(new BaseEvent[eventBuffer.size()]);
			eventBuffer.clear();
		}
		synchronized (listenerMonitor) {
			for (int i = 0; i < queue.length; i ++) {
				poll(queue[i]);
			}
		}
	}
	
	private static void poll(BaseEvent e) {
		if (windowHandlers.isEmpty()) {
			if (e.type == EventType.QUIT) Window.close();
			return;
		}
		ArrayList<KeyboardEventHandler> keyboardHandlers = new ArrayList<KeyboardEventHandler>(Event.keyboardHandlers);
		ArrayList<WindowEventHandler> windowHandlers = new ArrayList<WindowEventHandler>(Event.windowHandlers);
		ArrayList<MouseEventHandler> mouseHandlers = new ArrayList<MouseEventHandler>(Event.mouseHandlers);
		switch(e.type) {
			case FOCUS:
				for (WindowEventHandler h : windowHandlers) {
					h.focus((boolean)e.params[0]);
				}
				break;
			case MOUSE_FOCUS:
				for (WindowEventHandler h : windowHandlers) {
					h.mouseFocus((boolean)e.params[0]);
				}
				break;
			case MOUSE_MOVED:
				for (MouseEventHandler h : mouseHandlers) {
					h.mouseMoved((int)e.params[0], (int)e.params[1]);
				}
				break;
			case MOUSE_SCROLLED:
				for (MouseEventHandler h : mouseHandlers) {
					h.mouseScrolled((int)e.params[0], (int)e.params[1], (int)e.params[2]);
				}
				break;
			case MOUSE_PRESSED:
				for (MouseEventHandler h : mouseHandlers) {
					h.mousePressed((int)e.params[0], (int)e.params[1], (int)e.params[2]);
				}
				break;
			case MOUSE_RELEASED:
				for (MouseEventHandler h : mouseHandlers) {
					h.mouseReleased((int)e.params[0], (int)e.params[1], (int)e.params[2]);
				}
				break;
			case KEY_PRESSED:
				for (KeyboardEventHandler h : keyboardHandlers) {
					h.keyPressed((int)e.params[0]);
				}
				break;
			case KEY_RELASED:
				for (KeyboardEventHandler h : keyboardHandlers) {
					h.keyReleased((int)e.params[0]);
				}
				break;
			case KEY_TYPED:
				for (KeyboardEventHandler h : keyboardHandlers) {
					h.keyTyped((char)e.params[0]);
				}
				break;
			case RESIZE:
				for (WindowEventHandler h : windowHandlers) {
					h.resize((int)e.params[0], (int)e.params[1]);
				}
				break;
			case WINDOW_MOVED:
				for (WindowEventHandler h : windowHandlers) {
					 h.move((int)e.params[0], (int)e.params[1]);
				}
				break;
			case QUIT:
				boolean abortQuitting = false;
				for (WindowEventHandler h : windowHandlers) {
					abortQuitting = abortQuitting || h.quit();
				}
				if (!abortQuitting) {
					Window.close();
				}
				break;
			default:
				System.err.println("[Event] Unrecognised event: " + e.toString());
				break;
		}
	}

}
