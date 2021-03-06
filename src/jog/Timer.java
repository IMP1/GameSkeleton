package jog;

public abstract class Timer {
	
	private static double lastTime = System.nanoTime();
	private static double fixedTimestepTimer = 0;
	private static double fixedTimestep = 1.0 / 60.0;
	
	public static double getFixedTimestep() {
		return fixedTimestep;
	}
	
	public static double getSystemTime() {
		return (double)(System.nanoTime()) / 1_000_000_000;
	}
	
	public static double getDeltaTime() {
		double deltaTime = getSystemTime() - lastTime;
		lastTime = getSystemTime();
		return deltaTime;
	}
	
	/**
	 * Returns the amount of frames to update, calculated by the time elapsed divided
	 * by the duration of each timestep.
	 * This assumes that all updates are used and updates the timer accordingly.
	 * @return
	 */
	public static int getTimesteps() {
		fixedTimestepTimer += getDeltaTime();
		int steps = (int)(fixedTimestepTimer / fixedTimestep);
		fixedTimestepTimer -= steps * fixedTimestep;
		return steps;
	}

}
