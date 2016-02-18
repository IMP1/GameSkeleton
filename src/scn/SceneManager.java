package scn;

import java.util.EmptyStackException;

public class SceneManager {
	
	private SceneManager() {}
	
	private static Scene currentScene = null;
	private static java.util.Stack<Scene> sceneStack = new java.util.Stack<Scene>();
	
	public static void clear() {
		sceneStack.clear();
	}
	
	public static Scene scene() {
		return currentScene;
	}
	
	public static void changeScene(Scene newScene) {
		closeScene();
		setScene(newScene);
	}
	
	private static void setScene(Scene newScene) {
		currentScene = newScene;
		if (run.Game.LOGGING)
			System.out.println("Changing scene to " + newScene);
		if (currentScene != null)
			currentScene.start();
	}

	public static void addScene(Scene newScene) {
		sceneStack.push(currentScene);
		setScene(newScene);
	}
	
	public static void returnScene() {
		closeScene();
		try {
			currentScene = sceneStack.pop();
		} catch (EmptyStackException e) {
			currentScene = null;
		}
	}
	
	private static void closeScene() {
		if (currentScene != null)
			currentScene.close();
	}

}
