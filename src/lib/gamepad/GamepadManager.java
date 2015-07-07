package lib.gamepad;

import java.util.ArrayList;
import java.util.HashMap;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class GamepadManager {
	private GamepadManager() {}
	
	private static Object monitor = new Object();
	private static ArrayList<BaseEvent> eventBuffer = new ArrayList<BaseEvent>();
	private static BaseEvent[] queue = new BaseEvent[0];
	
	private static ArrayList<Gamepad> gamepads;
	
	private static GamepadHandler handler = null;
	private static HashMap<Gamepad, GamepadHandler> handlers = new HashMap<Gamepad, GamepadHandler>();

	public static void setHandler(GamepadHandler newHandler) {
		handler = newHandler;
	}
	
	public static void setHandler(Gamepad controller, GamepadHandler newHandler) {
		handlers.put(controller, newHandler);
	}
	
	private static class BaseEvent {
		private final EventType type;
		private final Object[] params;
		protected BaseEvent(EventType type, Object... params) {
			this.type = type;
			this.params = params;
		}
	}
	
	public enum EventType {
		BUTTON_PRESSED,
		BUTTON_RELEASED,
		DISCONNECTED,
		RECONNECTED,
	}

	public interface GamepadHandler {
		public void buttonPressed(Gamepad controller, int button);
		public void buttonReleased(Gamepad controller, int button);
	}

	public static void intitialise() {
		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller[] cs = ce.getControllers();
		Gamepad.reset();
		gamepads = new ArrayList<Gamepad>();
		for (Controller c : cs) {
			if (c.getType() == Controller.Type.GAMEPAD || 
				c.getType() == Controller.Type.STICK || 
				c.getType() == Controller.Type.FINGERSTICK) {
				gamepads.add(new Gamepad());
			}
		}
	}
	
	public static Gamepad[] getGamepads() {
		return gamepads.toArray(new Gamepad[gamepads.size()]);
	}

	public static void push(EventType type, Object... params) {
		synchronized (monitor) {
			eventBuffer.add(new BaseEvent(type, params));
		}
	}
	
	public static void pump() {
		synchronized (monitor) {
			queue = eventBuffer.toArray(new BaseEvent[0]);
			eventBuffer.clear();
		}
		for (int i = 0; i < queue.length; i ++) {
			System.out.println(queue[i]);
		}
	}

	public static void poll(BaseEvent e) {
		if (handler == null && handlers.isEmpty()) {
			return;
		}
		switch(e.type) {
			case BUTTON_PRESSED:
				if (handler != null) handler.buttonPressed((Gamepad)e.params[0], (int)e.params[1]);
				for (Gamepad g : handlers.keySet()) {
					handlers.get(g).buttonPressed((Gamepad)e.params[0], (int)e.params[1]);
				}
				break;
			case BUTTON_RELEASED:
				if (handler != null) handler.buttonReleased((Gamepad)e.params[0], (int)e.params[1]);
				for (Gamepad g : handlers.keySet()) {
					handlers.get(g).buttonReleased((Gamepad)e.params[0], (int)e.params[1]);
				}
				break;
			case DISCONNECTED:
				break;
			case RECONNECTED:
				break;
			default:
				System.err.println("[Gamepad] Unrecognised event: " + e.toString());
				break;
		}
	}
	
}
