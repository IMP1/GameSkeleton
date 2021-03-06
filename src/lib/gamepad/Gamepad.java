package lib.gamepad;

import java.util.ArrayList;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Gamepad extends Thread {
	
	private static final double DEAD_ZONE = 0.5;
	
	public static final int A = 0;
	public static final int B = 1;
	public static final int X = 2;
	public static final int Y = 3;
	public static final int LB = 4;
	public static final int RB = 5;
	public static final int SELECT = 6;
	public static final int START = 7;
	public static final int LS = 8;
	public static final int RS = 9;
	
	private static ArrayList<Controller> claimedControllers = new ArrayList<Controller>();
	protected static void reset() {
		claimedControllers.clear();
	}

	private static final Object buttonDownMonitor = new Object();
	
	private Controller controller;
	private ArrayList<Boolean> buttonValues;
	
	public Gamepad() {
		this(Controller.Type.GAMEPAD, Controller.Type.STICK);
	}

	public Gamepad(Controller.Type... controllerTypes) {
		super();
		buttonValues = new ArrayList<Boolean>();
		controller = getFirstValidController(controllerTypes);
		
		System.out.println("[Gamepad] " + getControllerName() + " created.");
		start();
	}
	
	private Controller getFirstValidController(Controller.Type... controllerTypes) {
        for (Controller c : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
        	if (claimedControllers.contains(c)) { 
        		continue;
        	}
        	for (Controller.Type type : controllerTypes) {
        		if (c.getType() == type) {
        			claimedControllers.add(c);
	                return c;
	            }
        	}
        }
        System.err.println("There was no controller for this gamepad object.");
        return null;
	}

	@Override
	public void run() {
		while (true) {
			poll();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void poll() {
		ArrayList<Boolean> oldValues = new ArrayList<Boolean>();
		if (!buttonValues.isEmpty()) { 
			oldValues = (ArrayList<Boolean>)buttonValues.clone();
		}
		buttonValues.clear();
		if (!isConnected()) {
			return;
		}
		int buttonId = 0;
		for (Component c : controller.getComponents()) {
			if (isButton(c)) {
				buttonValues.add(c.getPollData() > 0f);
				if (!oldValues.isEmpty() && buttonValues.get(buttonId) != oldValues.get(buttonId)) {
					if (buttonValues.get(buttonId)) {
						buttonPressed(buttonId);
					} else {
						buttonReleased(buttonId);
					}
				}
				buttonId ++;
			}
		}
	}

	protected void setKeyDown(int key, boolean down) {
		synchronized (buttonDownMonitor) {
			buttonValues.set(key, down);
		}
	}
	
	public boolean isKeyDown(int key) {
		synchronized (buttonDownMonitor) {
			return buttonValues.get(key);
		}
	}
	
	private void buttonPressed(int button) {
		setKeyDown(button, true);
		GamepadManager.push(GamepadManager.EventType.BUTTON_PRESSED, this, button);
	}
	
	private void buttonReleased(int button) {
		setKeyDown(button, false);
		GamepadManager.push(GamepadManager.EventType.BUTTON_RELEASED, this, button);
	}

	public boolean isConnected() {
		try {
			return controller.poll();
		} catch (Exception e) {
			return false;
		}
	}
	
	public Controller.Type getType() {
		return controller.getType();
	}
	
	public String getControllerName() {
		return controller.getName();
	}

	private boolean isButton(Component component) {
		return component.getName().contains("Button");
	}
	
	public int getButtonCount() {
		return buttonValues.size();
	}
	
	public boolean isButtonDown(int index) {
		return buttonValues.get(index);
	}
	
	public double getLeftAxisHorizontal() {
		return getLeftAxis()[0];
	}
	
	public double getLeftAxisVertical() { 
		return getLeftAxis()[1];
	}
	
	private double[] getLeftAxis() {
		return getAxis(Component.Identifier.Axis.X, Component.Identifier.Axis.Y);
	}

	public double getRightAxisHorizontal() {
		return getRightAxis()[0];
	}
	
	public double getRightAxisVertical() {
		return getRightAxis()[1];
	}
	
	private double[] getRightAxis() {
		if (controller.getType() == Controller.Type.STICK) {
			return getAxis(Component.Identifier.Axis.Z, Component.Identifier.Axis.RZ);
		} else {
			return getAxis(Component.Identifier.Axis.RX, Component.Identifier.Axis.RY);
		}
	}
	
	private double[] getAxis(Identifier horizontal, Identifier vertical) {
		double x = controller.getComponent(horizontal).getPollData();
		double y = controller.getComponent(vertical).getPollData();
		double[] axes = { x, y };
		double magnitude = Math.sqrt(x * x + y * y);
		if (magnitude < DEAD_ZONE) {
			axes[0] = 0;
			axes[1] = 0;
		} else {
			axes[0] = (x / magnitude) * (magnitude - DEAD_ZONE) / (1 - DEAD_ZONE);
			axes[1] = (y / magnitude) * (magnitude - DEAD_ZONE) / (1 - DEAD_ZONE);
		}
		return axes;
	}
	
}
