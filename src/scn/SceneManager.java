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
		startScene();
	}
	
	private static void setScene(Scene newScene) {
		currentScene = newScene;
		if (run.Game.LOGGING && newScene != null)
			System.out.println("Changing to " + newScene.getClass().getSimpleName() + " scene.");
	}

	public static void addScene(Scene newScene) {
		sceneStack.push(currentScene);
		setScene(newScene);
		startScene();
	}
	
	public static void returnScene() {
		closeScene();
		try {
			setScene(sceneStack.pop());
		} catch (EmptyStackException e) {
			setScene(null);
		}
	}

	private static void startScene() {
		if (currentScene != null)
			currentScene.start();
	}
	
	private static void closeScene() {
		if (currentScene != null)
			currentScene.close();
	}

}
