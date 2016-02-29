package jog;

public abstract class Input {
	
	private static KeyboardListener keyListener;
	private static MouseListener mouseListener;
	private static WindowListener windowListener;
	
	public static void initialise() {
		keyListener = new KeyboardListener();
		mouseListener = new MouseListener();
		windowListener = new WindowListener();
		Window.setKeyboardListener(keyListener);
		Window.setMouseListener(mouseListener);
		Window.setMouseMotionListener(mouseListener);
		Window.setMouseWheelListener(mouseListener);
		Window.setWindowListener(windowListener);
	}

	public static boolean isKeyDown(int key) {
		return keyListener.isKeyDown(key);
	}
	
	public static boolean isMouseDown(int key) {
		return mouseListener.isMouseDown(key);
	}

	public static boolean isMouseOver() {
		return mouseListener.isMouseOver();
	}
	
	public static int getMouseX() {
		return Window.getMouseX();
	}
	
	public static int getMouseY() {
		return Window.getMouseY();
	}

}
