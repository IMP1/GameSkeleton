package lib;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jog.Graphics.Drawable;
import jog.Image;

public class Animation extends Drawable {

	private double[] frameDurations;
	private int frameCount;
	private boolean looping;
	
	private Rectangle[] quads;
	
	private int currentFrame;
	private double timer;
	private boolean started;
	private boolean finished;
	

	public Animation(Image img, int framesWide, int framesHigh, int frames, boolean loop, double... frameTimes) {
		super(img);
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
	
	protected void draw(Graphics2D g, double x, double y) {
		int qx = quads[currentFrame].x;
		int qy = quads[currentFrame].y;
		int qw = quads[currentFrame].width;
		int qh = quads[currentFrame].height;
		g.drawImage(image, (int)x, (int)y, (int)x + qw, (int)y + qh, qx, qy, qx + qw, qy + qh, null);
	}
	
	@Override
	public int getWidth() {
		return quads[currentFrame].width;
	}
	
	@Override
	public int getHeight() {
		return quads[currentFrame].height;
	}

}
