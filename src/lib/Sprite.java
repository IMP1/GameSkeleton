package lib;

import java.awt.Rectangle;

public class Sprite {

	private double frameDuration;
	private int poseCount;
	private int frameCount;
	
	private jog.Image image;
	private Rectangle[][] quads;
	
	private int currentFrame;
	private int currentPose;
	private double timer;
	
	public Sprite(jog.Image img, int poses, int frames, double frameTime) {
		image = img;
		frameCount = frames;
		poseCount = poses;
		frameDuration = frameTime;
		quads = new Rectangle[poseCount][frameCount];
		int w = image.getWidth() / frameCount;
		int h = image.getHeight() / poseCount;
		for (int j = 0; j < poseCount; j ++) {
			for (int i = 0; i < frameCount; i ++) {
				int x = i * w;
				int y = j * h;
				quads[j][i] = new Rectangle(x, y, w, h);
			}
		}
		reset();
		setPose(0);
	}
	
	public void reset() {
		currentFrame = 0;
		timer = 0;
	}
	
	public void setPose(int pose) {
		currentPose = pose;
	}
	
	public void update(double dt) {
		timer += dt;
		if (timer >= currentFrameDuration()) {
			timer -= currentFrameDuration();
			nextFrame();
		}
	}
	
	public void draw(double x, double y) {
		jog.Graphics.draw(image, quads[currentPose][currentFrame], x, y);
	}
	
	private double currentFrameDuration() {
		return frameDuration;
	}
	
	private void nextFrame() {
		currentFrame ++;
		currentFrame %= frameCount;
	}

}
