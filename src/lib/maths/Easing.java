package lib.maths;

public class Easing {
	private Easing() {}

	// Linear
	public static double linear(double timer, double distance, double duration) {
		if (timer > duration) return distance;
		return distance * timer / duration;
	}
	
	// Quadratic
	public static double quadIn(double timer, double distance, double duration) {
		if (timer > duration) return distance;
		timer /= duration;
		return distance * timer * timer;
	}
	
	public static double quadOut(double timer, double distance, double duration) {
		if (timer > duration) return distance;
		timer /= duration;
		return -distance * timer * (timer-2);
	}
	
	public static double quadInOut(double timer, double distance, double duration) {
		if (timer > duration) return distance;
		timer /= (duration / 2);
		if (timer < 1) {
			return distance / 2 * timer * timer;
		} else {
			timer -= 1;
			return -distance / 2 * (timer * (timer - 2) - 1);
		}
	}
	
	// Cubic
	public static double cubicIn(double timer, double distance, double duration) {
		if (timer > duration) return distance;
		timer /= duration;
		return distance * timer * timer * timer;
	}
	
	public static double cubicOut(double timer, double distance, double duration) {
		if (timer > duration) return distance;
		timer /= duration;
		timer -= 1;
		return distance * (timer * timer * timer + 1);
	}
	
	public static double cubicInOut(double timer, double distance, double duration) {
		if (timer > duration) return distance;
		timer /= (duration / 2);
		if (timer < 1) {
			return distance / 2 * timer * timer * timer;
		} else {
			timer -= 1;
			return distance / 2 * (timer * timer * timer + 2);
		}
	}
	
	// Sinusoidal
	public static double sinIn(double timer, double distance, double duration) {
		if (timer > duration) return distance;
		return -distance * Math.cos(timer / duration * Math.PI / 2) + distance;
	}
	
	public static double sinOut(double timer, double distance, double duration) {
		if (timer > duration) return distance;
		return distance * Math.sin(timer / duration * Math.PI / 2);
	}
	
	public static double sinInOut(double timer, double distance, double duration) {
		if (timer > duration) return distance;
		return -distance / 2 * (Math.cos(Math.PI * timer / duration) - 1);
	}

	public static double bezierCurve(double timer, double distance, double duration, double... points) {
		if (timer > duration) return distance;
		double t = timer / duration;
		int n = points.length - 1;
		double outcome = 0;
		for (int i = 0; i <= n; i ++) {
			outcome += (choose(n, i) * Math.pow(t, i) * Math.pow(1 - t, n - i) * points[i]);
		}
		return outcome * distance;
	}
	
	private static double choose(double n, double k) {
		double outcome = 1;
		for (int i = 1; i <= k; i ++) {
			outcome *= (n + 1 - i) / i;
		}
		return outcome;
	}
	
}
