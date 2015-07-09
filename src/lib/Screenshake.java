package lib;

import java.util.ArrayList;

public class Screenshake {
	
	private final double strengthY;
	private final double strengthX;
	private final double duration;
	private double timer;

	private Screenshake(double strengthX, double strengthY, double duration) {
		this.strengthX = strengthX;
		this.strengthY = strengthY;
		this.duration = duration;
		this.timer = 0;
	}
	
	private static ArrayList<Screenshake> shakes = new ArrayList<Screenshake>();
	private static double rumbleX = 0;
	private static double rumbleY = 0;
	private static double totalShakeX = 0;
	private static double totalShakeY = 0;
	
	public static void addScreenshake(double strengthX, double strengthY, double duration) {
		Screenshake s = new Screenshake(strengthX, strengthY, duration);
		shakes.add(s);
	}
	
	public static void addRumble(double strengthX, double strengthY) {
		rumbleX += strengthX;
		rumbleY += strengthY;
	}
	
	public static void removeRumble(double strengthX, double strengthY) {
		addRumble(-strengthX, -strengthY);
	}
	
	public static void update(double dt) {
		totalShakeX = rumbleX;
		totalShakeY = rumbleY;
		for (Screenshake s : shakes) {
			s.timer += dt;
			totalShakeX += s.strengthX;
			totalShakeY += s.strengthY;
		}
		for (int i = shakes.size() - 1; i >= 0; i --) {
			if (shakes.get(i).timer >= shakes.get(i).duration) {
				shakes.remove(i);
			}
		}
	}
	
	public static void set() {
		jog.Graphics.push();
		double x = totalShakeX * 2 * Math.random() - totalShakeX;
		double y = totalShakeY * 2 * Math.random() - totalShakeY;
		jog.Graphics.translate(x, y);
	}
	
	public static void unset() {
		jog.Graphics.pop();
	}

}
