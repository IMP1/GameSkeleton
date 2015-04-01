package lib;

public class Parallax {
	
	private final int VIEWPORT_WIDTH = jog.Window.getWidth();
	private final int VIEWPORT_HEIGHT = jog.Window.getHeight();
	
	private jog.Image image;
	private double distance;
	private double x, y;
	private boolean scrollHorizontal, scrollVertical;
	private double autoScrollX, autoScrollY;

	public Parallax(jog.Image image, int x, int y, double distance, boolean scrollHorizontal, boolean scrollVertical) {
		this.image = image;
		this.distance = distance;
		this.x = x;
		this.y = y;
		this.scrollHorizontal = scrollHorizontal;
		this.scrollVertical = scrollVertical;
		this.autoScrollX = 0;
		this.autoScrollY = 0;
	}
	
	public void setAutoScroll(double dx, double dy) {
		autoScrollX = dx;
		autoScrollY = dy;
	}
	
	public double getAutoScrollX() {
		return autoScrollX;
	}
	
	public double getAutoScrollY() {
		return autoScrollY;
	}
	
	public void update(double dt) {
		if (autoScrollX != 0 || autoScrollY != 0) {
			scroll(autoScrollX * dt, autoScrollY * dt);
		}
	}
	
	public void scroll(double dx, double dy) {
		x -= dx * distance;
		y -= dy * distance;
		x %= image.getWidth();
		y %= image.getHeight();
	}
	
	public void draw() {
		jog.Graphics.draw(image, x, y);
		if (scrollHorizontal && scrollVertical) {
			if (x > 0) {
				if (y > 0) {
					jog.Graphics.draw(image, x - image.getWidth(), y - image.getHeight());
				}
				if (y + image.getHeight() < VIEWPORT_HEIGHT) {
					jog.Graphics.draw(image, x - image.getWidth(), y + image.getHeight());
				}
			}
			if (x + image.getWidth() < VIEWPORT_WIDTH) {
				if (y > 0) {
					jog.Graphics.draw(image, x + image.getWidth(), y - image.getHeight());
				}
				if (y + image.getHeight() < VIEWPORT_HEIGHT) {
					jog.Graphics.draw(image, x + image.getWidth(), y + image.getHeight());
				}
			}
		}
		if (scrollHorizontal) {
			if (x > 0) {
				jog.Graphics.draw(image, x - image.getWidth(), y);
			}
			if (x + image.getWidth() < VIEWPORT_WIDTH) {
				jog.Graphics.draw(image, x + image.getWidth(), y);
			}
		}
		if (scrollVertical) {
			if (y > 0) {
				jog.Graphics.draw(image, x, y - image.getHeight());
			}
			if (y + image.getHeight() < VIEWPORT_HEIGHT) {
				jog.Graphics.draw(image, x, y + image.getHeight());
			}
		}
	}

}
