package jog;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import run.Game;

public abstract class Window {
	
	public enum WindowMode {
		WINDOWED,
		FULLSCREEN,
		WINDOWED_FULLSCREEN,
		BORDERLESS_WINDOWED,
		BORDERLESS_FULLSCREEN,
	}
	
	private static JFrame window;
	private static boolean open = false;
	protected static Canvas canvas;
	
	public static int getWidth() {
		return window.getWidth() - window.getInsets().left - window.getInsets().right;
	}
	
	public static int getHeight() {
		return window.getHeight() - window.getInsets().bottom - window.getInsets().top;
	}
	
	private static GraphicsDevice getDefaultScreen() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	}
	
	public static DisplayMode[] getDisplayModes() {
		return getDefaultScreen().getDisplayModes();
	}
	
	public static Rectangle getScreenSize() {
		return getDefaultScreen().getDefaultConfiguration().getBounds();
	}
	
	public static int getPositionX() {
		return window.getX();
	}
	
	public static int getPositionY() {
		return window.getY();
	}
	
	public static void setPosition(int x, int y) {
		window.setLocation(x, y);
	}
	
	public static void setTitle(String title) {
		window.setTitle(title);
	}
	
	public static boolean isResizable() {
		return window.isResizable();
	}
	
	public static void setResizable(boolean resizable) {
		window.setResizable(resizable);
	}
	
	protected static int getMouseX() { 
		try {
			return window.getMousePosition().x - window.getInsets().left;
		} catch (NullPointerException e) {
			return -1;
		}
	}
	
	protected static int getMouseY() {
		try {
			return window.getMousePosition().y - window.getInsets().top;
		} catch (NullPointerException e) {
			return -1;
		}
	}
	
	public static boolean isOpen() {
		return open;
	}
	
	public static void close() {
		if (Game.LOGGING)
			System.out.println("[Window] Closed.");
		dispose();
		open = false;
	}
	
	public static void dispose() {
		window.dispose();
	}
	
	public static void initialise(int width, int height, String title) {
		Window.initialise(width, height, title, WindowMode.WINDOWED);
	}
	public static void initialise(int width, int height, String title, WindowMode mode) {
		Dimension size;
		if (mode == WindowMode.BORDERLESS_FULLSCREEN || mode == WindowMode.FULLSCREEN) {
			size = new Dimension(getScreenSize().width, getScreenSize().height);
		} else {
			size = new Dimension(width, height);
		}
		
		canvas = new Canvas();
		canvas.setPreferredSize(size);
		
		window = new JFrame();
		window.add(canvas);
		if (mode == WindowMode.BORDERLESS_FULLSCREEN || mode == WindowMode.BORDERLESS_WINDOWED) {
			window.setUndecorated(true);
		}
		
		window.pack();
		
		if (mode == WindowMode.FULLSCREEN && GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported()) {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(window);
		}
		
		if (mode == WindowMode.WINDOWED_FULLSCREEN) {
			Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			window.setSize(r.width, r.height);
		}
		
		window.setResizable(false);
		window.setTitle(title);
		window.setLocationRelativeTo(null);
		
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		window.setVisible(true);
		
		canvas.createBufferStrategy(2);
		Window.open = true;
		if (Game.LOGGING)
			System.out.println("[Window] Created.");
	}
	
	protected static void setKeyboardListener(KeyListener listener) {
		window.addKeyListener(listener);
		canvas.addKeyListener(listener);
	}
	
	protected static void setMouseListener(MouseListener listener) {
		window.addMouseListener(listener);
		canvas.addMouseListener(listener);
	}
	
	protected static void setMouseMotionListener(MouseMotionListener listener) {
		window.addMouseMotionListener(listener);
		canvas.addMouseMotionListener(listener);
	}
	
	protected static void setMouseWheelListener(MouseWheelListener listener) {
		window.addMouseWheelListener(listener);
		canvas.addMouseWheelListener(listener);
	}
	
	protected static void setWindowListener(WindowListener listener) {
		window.addWindowListener(listener);
		window.addWindowFocusListener(listener);
		window.addComponentListener(listener);
	}
	
	public static void setIcon(String... filepaths) {
		ArrayList<java.awt.Image> icons = new ArrayList<java.awt.Image>();
		for (String path : filepaths) {
			try {
				icons.add(ImageIO.read(Filesystem.getURL(path)));
			} catch (IOException e) {
				System.err.println("[Window] Could not read image from '" + path + "'.");
			}
		}
		window.setIconImages(icons);
	}

	public static void setMouseCursor() {
		setMouseCursor(Cursor.DEFAULT_CURSOR); 
	}
	
	public static void setMouseCursor(int cursorType) {
		Cursor cursor = Cursor.getPredefinedCursor(cursorType);
		window.setCursor(cursor);
	}

}
