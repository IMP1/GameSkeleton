package lib;

import java.awt.Rectangle;
import java.util.ArrayList;

import jog.Graphics;

public class Camera {
	
	private ArrayList<Parallax> parallaxes;
	
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
		parallaxes = new ArrayList<Parallax>();
	}
	
	public Camera(int minX, int minY, int maxX, int maxY) {
		this();
		setBounds(minX, minY, maxX - viewport.width, maxY - viewport.height);
	}
	
	public void addParallax(Parallax p) {
		parallaxes.add(p);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

	public double getCentreX() {
		return x + viewport.width / 2;
	}
	
	public double getCentreY() {
		return y + viewport.height / 2;
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	public void set() {
		Graphics.push();
		Graphics.setScissor(viewport);
		Graphics.scale(zoom, zoom);
		Graphics.translate(viewport.x, viewport.y);
		for (Parallax p : parallaxes) {
			p.setPosition(getCentreX() - viewport.x, getCentreY() - viewport.y);
			p.draw();
		}
		Graphics.translate(-x, -y);
	}
	
	public void unset() {
		Graphics.setScissor();
		Graphics.pop();
	}
	
	public void move(double dx, double dy) {
		setX(x + dx);
		setY(y + dy);
		for (Parallax p : parallaxes) p.scroll(dx, dy);
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
	
	public double getMouseWorldX() {
		return getWorldX(jog.Input.getMouseX());
	}
	
	public double getMouseWorldY() {
		return getWorldY(jog.Input.getMouseY());
	}
	
	public double getScreenX(double worldX) {
		// TODO: these need to include scale
		return worldX - x;
	}
	
	public double getScreenY(double worldY) {
		return worldY - y;
	}
	
	public double getWorldX(double screenX) {
		return screenX + x;
	}
	
	public double getWorldY(double screenY) {
		return screenY + y;
	}
	
}
