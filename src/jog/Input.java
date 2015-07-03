package jog;

public abstract class Input {
	
	private static KeyboardListener keyListener;
	private static MouseListener mouseListener;
	
	public static void initialise() {
		keyListener = new KeyboardListener();
		mouseListener = new MouseListener();
		Window.setKeyboardListener(keyListener);
		Window.setMouseListener(mouseListener);
		Window.setMouseMotionListener(mouseListener);
		Window.setMouseWheelListener(mouseListener);
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
