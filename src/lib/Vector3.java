package lib;

public class Vector3 {

	private static final double EPSILON = 0.0001; 
	
	public static final Vector3 i = new Vector3(1, 0, 0);
	public static final Vector3 j = new Vector3(0, 1, 0);
	public static final Vector3 k = new Vector3(0, 0, 1);
	public static final Vector3 ZERO = new Vector3(0, 0, 0);
	
	public final double x, y, z;

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3 scale(double scaleFactor) {
		return new Vector3(x * scaleFactor, y * scaleFactor, z * scaleFactor);
	}

	public double magnitudeSquared() {
		return dotProduct(this, this);
	}
	
	public double magnitude() {
		return Math.sqrt(magnitudeSquared());
	}
	
	public Vector3 negate() {
		return scale(-1);
	}
	
	public Vector3 normalise() {
		if (magnitudeSquared() < EPSILON) return Vector3.ZERO;
		return scale(1.0 / magnitude());
	}
	
	public static Vector3 add(Vector3 v1, Vector3 v2) {
		return new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	
	public static Vector3 subtract(Vector3 v1, Vector3 v2) {
		return add(v1, v2.negate());
	}
	
	public static double dotProduct(Vector3 v1, Vector3 v2) {
		return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
	}
	
	public static double angleBetween(Vector3 v1, Vector3 v2) {
		return Math.acos(dotProduct(v1, v2) / (v1.magnitude() * v2.magnitude()));
	}
	
	public static boolean equals(Vector3 v1, Vector3 v2) { return equals(v1, v2, false); }
	public static boolean equals(Vector3 v1, Vector3 v2, boolean allowEitherDirection) {
		if (v1.equals(v2)) return true;
		if (allowEitherDirection && v1.equals(v2.negate())) return true;
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Vector3)) return false;
		Vector3 v = (Vector3)obj;
		if (v.x != x) return false;
		if (v.y != y) return false;
		if (v.z != z) return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("(");
		s.append(x).append(", ");
		s.append(y).append(", ");
		s.append(z);
		s.append(")");
		return s.toString();
	}
	
}
