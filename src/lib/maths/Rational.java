package lib.maths;

public class Rational extends Number implements Comparable<Rational> {

	public final static Rational ZERO = new Rational(0, 1);
	public final static Rational ONE  = new Rational(1, 1);
	
	private static final long serialVersionUID = 671941784686472206L;

	private static long greatestCommonDivisor(long a, long b) {
		if (b == 0) return a;
		return greatestCommonDivisor(b, a % b);
	}
	
	public static Rational max(Rational r1, Rational r2) {
		return (r1.compareTo(r2) < 0 ? r2 : r1);
	}
	
	public static Rational min(Rational r1, Rational r2) {
		return (r1.compareTo(r2) > 0 ? r2 : r1);
	}
	
	private long numerator;
	private long denominator;

	public Rational(long numerator) {
		this(numerator, 1);
	}
	
	public Rational(long numerator, long denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("denominator is zero");
        }
        if (denominator < 0) {
            numerator   *= -1;
            denominator *= -1;
        }
        long gdc = greatestCommonDivisor(numerator, denominator);
		this.numerator   = numerator   / gdc;
		this.denominator = denominator / gdc;
	}

	@Override
	public int intValue() {
		return (int)doubleValue();
	}

	@Override
	public long longValue() {
		return (long)doubleValue();
	}

	@Override
	public float floatValue() {
		return (float)doubleValue();
	}

	@Override
	public double doubleValue() {
		return ((double)numerator / (double)denominator);
	}
	
	@Override
	public int compareTo(Rational frac) {
		long a = this.numerator * frac.denominator;
		long b = frac.numerator * this.denominator;
		return Long.compare(a, b);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !(o instanceof Rational)) return false;
		return compareTo((Rational)o) == 0;
	}
	
	@Override
	public String toString() {
		return String.valueOf(numerator) + "/" + String.valueOf(denominator);
	}
	
	public int signum() {
		return Long.signum(numerator);
	}
	
	public Rational negate() {
		return new Rational(-numerator, denominator);
	}
	
	public Rational reciprocal() {
		if (numerator == 0)
			throw new ArithmeticException("Division by zero");
		return new Rational(denominator, numerator);
	}
	
	public Rational multiply(Rational frac) {
		return new Rational(this.numerator * frac.numerator,
							this.denominator * frac.denominator);
	}
	
	public Rational divide(Rational frac) {
		if (frac == Rational.ZERO)
			throw new ArithmeticException("Division by zero");
		return multiply(frac.reciprocal());
	}
	
	public Rational add(Rational frac) {
		return new Rational(this.numerator * frac.denominator + frac.numerator * this.denominator,
						   (this.denominator * frac.denominator));
	}
	
	public Rational subtract(Rational frac) {
		return add(frac.negate());
	}
	
	public Rational pow(int exponent) {
		if (exponent == 0) {
			return Rational.ONE;
		} else if (exponent == 1) {
			return this;
		} else if (exponent < 0) {
			return new Rational((long)Math.pow(numerator, -exponent), (long)Math.pow(denominator, -exponent)).reciprocal();
		} else {
			return new Rational((long)Math.pow(numerator, exponent), (long)Math.pow(denominator, exponent));
		}
	}
	
	public Rational complement() {
		return new Rational(denominator - numerator, denominator);
	}
	
	public Rational abs() {
		return (signum() < 0 ? negate() : this);
	}
	
}
