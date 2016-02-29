package lib.maths;

public class Util {
	private Util() {}
	
	public final static double TAU = Math.PI * 2;
	public final static double ROOT_2 = Math.sqrt(2);
	
	public static double binarySin(double x) {
		return (Math.sin(x - Math.PI/2) + 1) / 2;
	}
	
}
