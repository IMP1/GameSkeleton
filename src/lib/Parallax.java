package lib;

public class Parallax {
	
	private jog.Image image;
	private double distance;
	private double x, y;
	private boolean scrollHorizontal, scrollVertical;

	public Parallax(jog.Image image, int x, int y, double distance, boolean scrollHorizontal, boolean scrollVertical) {
		this.image = image;
		this.distance = distance;
		this.x = x;
		this.y = y;
		this.scrollHorizontal = scrollHorizontal;
		this.scrollVertical = scrollVertical;
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
				if (y + image.getHeight() < jog.Window.getHeight()) {
					jog.Graphics.draw(image, x - image.getWidth(), y + image.getHeight());
				}
			}
			if (x + image.getWidth() < jog.Window.getWidth()) {
				if (y > 0) {
					jog.Graphics.draw(image, x + image.getWidth(), y - image.getHeight());
				}
				if (y + image.getHeight() < jog.Window.getHeight()) {
					jog.Graphics.draw(image, x + image.getWidth(), y + image.getHeight());
				}
			}
		}
		if (scrollHorizontal) {
			if (x > 0) {
				jog.Graphics.draw(image, x - image.getWidth(), y);
			}
			if (x + image.getWidth() < jog.Window.getWidth()) {
				jog.Graphics.draw(image, x + image.getWidth(), y);
			}
		}
		if (scrollVertical) {
			if (y > 0) {
				jog.Graphics.draw(image, x, y - image.getHeight());
			}
			if (y + image.getHeight() < jog.Window.getHeight()) {
				jog.Graphics.draw(image, x, y + image.getHeight());
			}
		}
	}

}
