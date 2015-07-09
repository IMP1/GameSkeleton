package lib;

import java.awt.Rectangle;

import jog.Graphics;

public class Camera {
	
	public final static int SPEED = 460;
	
	private double x;
	private double y;
	private double zoom;
	
	private Rectangle bounds;
	private Rectangle viewport;
	
	public Camera() {
		x = 0;
		y = 0;
		zoom = 1;
		bounds = null;
		viewport = new Rectangle(jog.Window.getWidth(), jog.Window.getHeight());
	}
	
	public Camera(int minX, int minY, int maxX, int maxY) {
		this();
		setBounds(minX, minY, maxX - viewport.width, maxY - viewport.height);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void set() {
		Graphics.push();
		Graphics.scale(zoom, zoom);
		Graphics.translate(-x, -y);
	}
	
	public void unset() {
		Graphics.pop();
	}
	
	public void move(double dx, double dy) {
		setX(x + dx);
		setY(y + dy);
	}
	
	public void setX(double newX) {
		if (bounds != null) { 
			x = Math.max(bounds.getMinX(), Math.min(newX, bounds.getMaxX()));
		} else {
			x = newX;
		}
	}
	
	public void setY(double newY) {
		if (bounds != null) { 
			y = Math.max(bounds.getMinY(), Math.min(newY, bounds.getMaxY()));
		} else {
			y = newY;
		}
	}
	
	public void zoom(double z) {
		setZoom(zoom * z);
	}
	
	public void setZoom(double newZoom) {
		zoom = newZoom;
	}
	
	public void centreOn(double x, double y) {
		setX(x - viewport.width / 2);
		setY(y - viewport.height / 2);
	}
	
	public void setBounds(int minX, int minY, int maxX, int maxY) {
		bounds = new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

}
