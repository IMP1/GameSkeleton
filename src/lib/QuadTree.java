package lib;

import java.awt.Rectangle;
import java.util.ArrayList;

public class QuadTree<T extends Object2D> {
	
	private static final int DEFAULT_MAX_POINTS = 16;
	
	private final int maximumPoints;
	private Rectangle boundary;
	private ArrayList<T> objects;
	private boolean subdivided;
	private QuadTree<T> northWest;
	private QuadTree<T> northEast;
	private QuadTree<T> southWest;
	private QuadTree<T> southEast;
	
	public QuadTree(Rectangle rect) {
		this(rect, DEFAULT_MAX_POINTS);
	}
	
	public QuadTree(Rectangle rect, int maxPoints) {
		maximumPoints = maxPoints;
		boundary = rect;
		objects = new ArrayList<T>();
		subdivided = false;
	}
	
	public boolean insert(T obj) {
		if (!boundary.contains(obj.getX(), obj.getY())) {
			return false;
		}
		if (objects.size() < maximumPoints) {
			objects.add(obj);
			return true;
		}
		if (!subdivided) subdivide();
		if (northWest.insert(obj)) return true;
		if (northEast.insert(obj)) return true;
		if (southWest.insert(obj)) return true;
		if (southEast.insert(obj)) return true;
		
		throw new RuntimeException("[QuadTree] This should never happen.");
	}
	
	private void subdivide() {
		subdivided = true;
		northWest = new QuadTree<T>(new Rectangle(boundary.x, boundary.y, boundary.width/2, boundary.height/2));
		northEast = new QuadTree<T>(new Rectangle((int)boundary.getCenterX(), boundary.y, boundary.width/2, boundary.height/2));
		southWest = new QuadTree<T>(new Rectangle(boundary.x, (int)boundary.getCenterY(), boundary.width/2, boundary.height/2));
		southEast = new QuadTree<T>(new Rectangle((int)boundary.getCenterX(), (int)boundary.getCenterY(), boundary.width/2, boundary.height/2));
	}
	
	public ArrayList<T> getPoints() {
		return getPoints(boundary);
	}
	
	public ArrayList<T> getPoints(Rectangle rect) {
		ArrayList<T> results = new ArrayList<T>();
		if (!boundary.intersects(rect)) return results;
		
		for (T obj : objects) {
			if (rect.contains(obj.getX(), obj.getY())) {
				results.add(obj);
			}
		}
		if (!subdivided) return results;
		
		results.addAll(northWest.getPoints(rect));
		results.addAll(northEast.getPoints(rect));
		results.addAll(southWest.getPoints(rect));
		results.addAll(southEast.getPoints(rect));
		
		return results;
	}
	
}
