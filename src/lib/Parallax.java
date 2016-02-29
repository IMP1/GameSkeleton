package lib;

public class Parallax {
	
	private jog.Image image;
	private double distance;
	public double x, y;
	public int width, height;
	private boolean scrollHorizontal, scrollVertical;
	private double autoScrollX, autoScrollY;
	private int viewportWidth, viewportHeight; 

	public Parallax(jog.Image image, int x, int y, double distance, boolean scrollHorizontal, boolean scrollVertical) {
		setPosition(x, y);
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.image = image;
		this.distance = distance;
		scroll(width, height);
		this.scrollHorizontal = scrollHorizontal;
		this.scrollVertical = scrollVertical;
		this.viewportWidth = jog.Window.getWidth();
		this.viewportHeight = jog.Window.getHeight();
		this.autoScrollX = 0;
		this.autoScrollY = 0;
	}
	
	public void setViewport(int w, int h) {
		viewportWidth = w;
		viewportHeight = h;
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
	
	public void setPosition(double x, double y) {
		this.x = this.y = 0;
		scroll(x, y);
	}
	
	public void scroll(double dx, double dy) {
		x -= dx * distance;
		y -= dy * distance;
		if (scrollHorizontal)
			x %= width;
		if (scrollVertical)
			y %= height;
	}
	
	public void draw() {
		jog.Graphics.draw(image, x, y);
		if (scrollHorizontal && scrollVertical) {
			if (x > 0) {
				if (y > 0) {
					jog.Graphics.draw(image, x - image.getWidth(), y - image.getHeight());
				}
				if (y + height < viewportHeight) {
					jog.Graphics.draw(image, x - image.getWidth(), y + image.getHeight());
				}
			}
			if (x + width < viewportWidth) {
				if (y > 0) {
					jog.Graphics.draw(image, x + image.getWidth(), y - image.getHeight());
				}
				if (y + image.getHeight() < viewportHeight) {
					jog.Graphics.draw(image, x + image.getWidth(), y + image.getHeight());
				}
			}
		}
		if (scrollHorizontal) {
			if (x > 0) {
				jog.Graphics.draw(image, x - image.getWidth(), y);
			}
			if (x + width < viewportWidth) {
				jog.Graphics.draw(image, x + image.getWidth(), y);
			}
		}
		if (scrollVertical) {
			if (y > 0) {
				jog.Graphics.draw(image, x, y - image.getHeight());
			}
			if (y + height < viewportHeight) {
				jog.Graphics.draw(image, x, y + image.getHeight());
			}
		}
	}

}
