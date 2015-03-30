package scn;

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
	
	public static void setScene(Scene newScene) {
		currentScene = newScene;
		currentScene.start();
	}

	public static void addScene(Scene newScene) {
		sceneStack.push(currentScene);
		currentScene = newScene;
		currentScene.start();
	}
	
	public static void returnScene() {
		currentScene = sceneStack.pop();
	}

}
