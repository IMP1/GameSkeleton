package lib;

import java.awt.Rectangle;

public class Animation {

	private double[] frameDurations;
	private int frameCount;
	private boolean looping;
	
	private jog.Image image;
	private Rectangle[] quads;
	
	private int currentFrame;
	private double timer;
	private boolean started;
	private boolean finished;
	
	public Animation(jog.Image img, int framesWide, int framesHigh, int frames, boolean loop, double... frameTimes) {
		image = img;
		frameCount = frames;
		frameDurations = frameTimes;
		looping = loop;
		quads = new Rectangle[frameCount];
		int w = image.getWidth() / framesWide;
		int h = image.getHeight() / framesHigh;
		for (int i = 0; i < frameCount; i ++) {
			int x = (i % framesWide) * w;
			int y = (i / framesWide) * h;
			quads[i] = new Rectangle(x, y, w, h);
		}
		reset();
	}
	
	public void reset() {
		currentFrame = 0;
		timer = 0;
		finished = false;
		started = false;
	}
	
	public void start() {
		started = true;
	}
	
	public boolean isPlaying() {
		return started && !finished;
	}
	
	public void update(double dt) {
		if (!started) return;
		if (finished) return;
		timer += dt;
		if (timer >= currentFrameDuration()) {
			timer -= currentFrameDuration();
			nextFrame();
		}
	}
	
	public void draw(double x, double y) {
		jog.Graphics.drawq(image, quads[currentFrame], x, y);
	}
	
	public boolean isOnFrame(int n) {
		return currentFrame == n;
	}
	
	private double currentFrameDuration() {
		if (frameDurations.length == frameCount) {
			return frameDurations[currentFrame];
		} else {
			return frameDurations[0];
		}
	}
	
	private void nextFrame() {
		currentFrame ++;
		if (currentFrame >= frameCount) {
			if (looping) {
				currentFrame %= frameCount;
			} else {
				currentFrame --;
				finished = true;
			}
		}
	}

}
