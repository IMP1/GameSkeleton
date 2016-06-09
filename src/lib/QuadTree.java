package lib;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class QuadTree<T extends Object2D> implements Collection<T> {
	
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
		clear();
	}

	private void subdivide() {
		subdivided = true;
		northWest = new QuadTree<T>(new Rectangle(boundary.x, boundary.y, boundary.width/2, boundary.height/2));
		northEast = new QuadTree<T>(new Rectangle((int)boundary.getCenterX(), boundary.y, boundary.width/2, boundary.height/2));
		southWest = new QuadTree<T>(new Rectangle(boundary.x, (int)boundary.getCenterY(), boundary.width/2, boundary.height/2));
		southEast = new QuadTree<T>(new Rectangle((int)boundary.getCenterX(), (int)boundary.getCenterY(), boundary.width/2, boundary.height/2));
	}
	
	public boolean add(T obj) {
		if (!boundary.contains(obj.getX(), obj.getY())) {
			return false;
		}
		if (objects.size() < maximumPoints) {
			objects.add(obj);
			return true;
		}
		if (!subdivided) subdivide();
		if (northWest.add(obj)) return true;
		if (northEast.add(obj)) return true;
		if (southWest.add(obj)) return true;
		if (southEast.add(obj)) return true;
		
		throw new RuntimeException("[QuadTree] This should never happen.");
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
		if (subdivided) {
			results.addAll(northWest.getPoints(rect));
			results.addAll(northEast.getPoints(rect));
			results.addAll(southWest.getPoints(rect));
			results.addAll(southEast.getPoints(rect));
		}
		return results;
	}

	@Override
	public int size() {
		int size = 0;
		if (subdivided) {
			size += northWest.size();
			size += northEast.size();
			size += southWest.size();
			size += southEast.size();
		}
		size += objects.size();
		return size;
	}

	@Override
	public boolean isEmpty() {
		return objects.isEmpty() && !subdivided;
	}

	@Override
	public boolean contains(Object o) {
		if (objects.contains(o)) return true;
		if (subdivided) {
			if (northWest.contains(o)) return true;
			if (northEast.contains(o)) return true;
			if (southWest.contains(o)) return true;
			if (southEast.contains(o)) return true;
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return getPoints().iterator();
	}

	@Override
	public Object[] toArray() {
		return getPoints().toArray();
	}

	@Override
	public <U> U[] toArray(U[] a) {
		return getPoints().toArray(a);
	}
	
	@Override
	public boolean remove(Object o) {
		if (objects.remove(o)) return true;
		if (subdivided) {
			if (northWest.remove(o)) return true;
			if (northEast.remove(o)) return true;
			if (southWest.remove(o)) return true;
			if (southEast.remove(o)) return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object element : c) {
			if (!contains(element)) return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean change = false;
		for (T element : c) {
			add(element);
			change = true;
		}
		return change;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean change = false;
		for (Object element : c) {
			remove(element);
			change = true;
		}
		return change;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean change = false;
		for (T element : getPoints()) {
			if (!c.contains(element)) {
				remove(element);
				change = true;
			}
		}
		return change;
	}

	@Override
	public void clear() {
		objects.clear();
		if (subdivided) {
			northWest.clear();
			northEast.clear();
			southWest.clear();
			southEast.clear();
		}
		subdivided = false;
	}
	
}
