package run;

import java.awt.event.KeyEvent;

import jog.Input;
import jog.Window.WindowMode;
import scn.*;

public abstract class Game implements jog.Event.EventHandler {
	
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
		double maxDT = 1.0 / minimumFPS;
		load(width, height, title, windowMode);
		setup(startingScene);
		gameLoop(maxDT);
		close();
	}
	
	protected void load(int width, int height, String title, jog.Window.WindowMode windowMode) {
		jog.Window.initialise(width, height, title, windowMode);
		jog.Input.initialise();
		jog.Event.setHandler(this);
		jog.Graphics.initialise();
	}
	
	protected void setup(Scene startingScene) {
		SceneManager.setScene(startingScene);
	}
	
	protected void gameLoop(double maxDT) {
		long lastTick = System.nanoTime();
		while (jog.Window.isOpen()) {
			try { Thread.sleep(1); } catch (Exception e) {}; // pause a bit so that we don't choke the system
			double deltaTime = (double)(System.nanoTime() - lastTick) / 1_000_000_000.0;
			lastTick = System.nanoTime();
			
			// Update multiple times rather than with a dangerously large delta-time
			while (deltaTime > maxDT) {
				update(maxDT);
				deltaTime -= maxDT;
			}
			if (deltaTime > 0)
				update(deltaTime);
			
			jog.Event.pump();
			// If we've just quit as a result of an event
			if (!jog.Window.isOpen()) break;
			
			jog.Graphics.clear();
			draw();
		}
	}
	
	protected void close() {
		while (SceneManager.scene() != null) {
			SceneManager.returnScene();
		}
		System.out.println("[Game] Closed successfully.");
		jog.Window.close();
		System.exit(0);
	}
	
	protected void update(double dt) {
		SceneManager.scene().update(dt);
	}
	
	protected void draw() {
		SceneManager.scene().draw();
	}
	
	/*
	 * The following methods pass on event handlers to the scene.
	 */

	@Override
	public void focus(boolean gained) {
		SceneManager.scene().focus(gained);
	}

	@Override
	public void mouseFocus(boolean gained) {
		SceneManager.scene().mouseFocus(gained);
	}

	@Override
	public void resize(int oldWidth, int oldHeight) {
		SceneManager.scene().resize(oldWidth, oldHeight);
	}

	@Override
	public void keyPressed(int key) {
		SceneManager.scene().keyPressed(key);
		if (key == KeyEvent.VK_F4 && Input.isKeyDown(KeyEvent.VK_ALT)) {
			close();
		}
	}

	@Override
	public void keyReleased(int key) {
		SceneManager.scene().keyReleased(key);
	}

	@Override
	public void mouseMoved(int x, int y) {
		SceneManager.scene().mouseMoved(x, y);
	}

	@Override
	public void mouseScrolled(int x, int y, int scroll) {
		SceneManager.scene().mouseScrolled(x, y, scroll);
	}

	@Override
	public void mousePressed(int x, int y, int key) {
		SceneManager.scene().mousePressed(x, y, key);
	}

	@Override
	public void mouseReleased(int x, int y, int key) {
		SceneManager.scene().mouseReleased(x, y, key);
	}

	@Override
	public boolean quit() {
		return SceneManager.scene().quit();
	}	

}
