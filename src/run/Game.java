package run;

import java.awt.event.KeyEvent;

import jog.Input;
import jog.Timer;
import jog.Event.KeyboardEventHandler;
import jog.Event.MouseEventHandler;
import jog.Event.WindowEventHandler;
import jog.Window.WindowMode;
import scn.*;

public abstract class Game implements KeyboardEventHandler, MouseEventHandler, WindowEventHandler {
	
	public static boolean LOGGING = true;
	
	private static final String DEFAULT_TITLE = "Jog Game";
	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 600;
	private static final int DEFAULT_MINIMUM_FPS = 10;
	private static final WindowMode DEFAULT_WINDOW_MODE = WindowMode.WINDOWED;
	
	public Game(Scene startingScene) {
		this(startingScene, DEFAULT_TITLE);
	}
	public Game(Scene startingScene, String title) {
		this(startingScene, title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	public Game(Scene startingScene, String title, int width, int height) {
		this(startingScene, title, width, height, DEFAULT_MINIMUM_FPS);
	}
	public Game(Scene startingScene, String title, int width, int height, int minimumFPS) {
		this(startingScene, title, width, height, minimumFPS, DEFAULT_WINDOW_MODE);
	}
	public Game(Scene startingScene, String title, int width, int height, int minimumFPS, jog.Window.WindowMode windowMode) {
		this(startingScene, title, width, height, minimumFPS, DEFAULT_WINDOW_MODE, false);
	}
	public Game(Scene startingScene, String title, int width, int height, int minimumFPS, jog.Window.WindowMode windowMode, boolean fixedTimestep) {
		double maxDT = 1.0 / minimumFPS;
		initialise(width, height, title, windowMode);
		setup();
		begin(startingScene);
		gameLoop(fixedTimestep, maxDT);
		close();
	}
	
	/**
	 * Sets up jog classes.
	 * @param width the width of the window.
	 * @param height the height of the window.
	 * @param title the title of the window.
	 * @param windowMode the WindowMode of the window.
	 */
	protected void initialise(final int width, final int height, final String title, final jog.Window.WindowMode windowMode) {
		jog.Window.initialise(width, height, title, windowMode);
		jog.Input.initialise();
		jog.Event.addWindowHandler(this);
		jog.Event.addKeyboardHandler(this);		
		jog.Event.addMouseHandler(this);
		jog.Graphics.initialise();
	}
	
	/**
	 * Is run after initialise, and is used for loading resources/data and maintainace before the game begins.
	 */
	protected void setup() {}
	
	/**
	 * Starts the first scene.
	 * @param startingScene
	 */
	protected void begin(Scene startingScene) {
		SceneManager.changeScene(startingScene);
	}
	
	/**
	 * Calculates dt and performs updates and draw calls.
	 * @param fixedTimestep whether to use fixed timesteps.
	 * @param maxDT the highest time between frames that is allowed before calling update multiple times.
	 */
	protected void gameLoop(final boolean fixedTimestep, final double maxDT) {
		Timer.getDeltaTime();
		while (jog.Window.isOpen()) {
			if (fixedTimestep) {
				int steps = jog.Timer.getTimesteps();
				if (steps < 1) sleep(); // pause a bit so that we don't choke the system
				for (int i = 0; i < steps; i ++) {
					update(Timer.getFixedTimestep());
				}
			} else {
				// Update multiple times rather than with a dangerously large delta-time
				double deltaTime = Timer.getDeltaTime();
				if (deltaTime < maxDT) sleep(); // pause a bit so that we don't choke the system
				while (deltaTime > maxDT) {
					update(maxDT);
					deltaTime -= maxDT;
				}
				if (deltaTime > 0) update(deltaTime);
			}
			updateInput();
			// If we've just quit as a result of an event or update:
			if (!jog.Window.isOpen()) break;
			// Clear & Draw
			jog.Graphics.clear();
			draw();
		}
	}
	
	private void sleep() {
		try { Thread.sleep(10); } catch (Exception e) {};
	}
	
	protected void updateInput() {
		jog.Event.pump();
	}
	
	protected void close() {
		while (SceneManager.scene() != null) {
			SceneManager.returnScene();
		}
		if (Game.LOGGING)
			System.out.println("[Game] Closed successfully.");
		jog.Window.close();
		System.exit(0);
	}
	
	protected void update(double dt) {
		if (SceneManager.scene() != null)
			SceneManager.scene().update(dt);
	}
	
	protected void draw() {
		if (SceneManager.scene() != null)
			SceneManager.scene().draw();
	}
	
	/*
	 * The following methods pass on event handlers to the scene.
	 */

	@Override
	public void focus(boolean gained) {
		if (SceneManager.scene() != null)
			SceneManager.scene().focus(gained);
	}

	@Override
	public void mouseFocus(boolean gained) {
		if (SceneManager.scene() != null)
			SceneManager.scene().mouseFocus(gained);
	}

	@Override
	public void resize(int oldWidth, int oldHeight) {
		if (SceneManager.scene() != null)
			SceneManager.scene().resize(oldWidth, oldHeight);
	}
	
	@Override
	public void move(int dx, int dy) {
		if (SceneManager.scene() != null)
			SceneManager.scene().move(dx, dy);
	}

	@Override
	public void keyPressed(int key) {
		if (SceneManager.scene() != null)
			SceneManager.scene().keyPressed(key);
		if (key == KeyEvent.VK_F4 && Input.isKeyDown(KeyEvent.VK_ALT)) {
			close();
		}
	}

	@Override
	public void keyReleased(int key) {
		if (SceneManager.scene() != null)
			SceneManager.scene().keyReleased(key);
	}

	@Override
	public void keyTyped(char text) {
		if (SceneManager.scene() != null)
			SceneManager.scene().keyTyped(text);
	}
	
	@Override
	public void mouseMoved(int x, int y) {
		if (SceneManager.scene() != null)
			SceneManager.scene().mouseMoved(x, y);
	}

	@Override
	public void mouseScrolled(int x, int y, int scroll) {
		if (SceneManager.scene() != null)
			SceneManager.scene().mouseScrolled(x, y, scroll);
	}

	@Override
	public void mousePressed(int x, int y, int key) {
		if (SceneManager.scene() != null)
			SceneManager.scene().mousePressed(x, y, key);
	}

	@Override
	public void mouseReleased(int x, int y, int key) {
		if (SceneManager.scene() != null)
			SceneManager.scene().mouseReleased(x, y, key);
	}

	@Override
	public boolean quit() {
		if (SceneManager.scene() != null)
			return SceneManager.scene().quit();
		else
			return false;
	}	

}
