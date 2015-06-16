package jog;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.Stack;

public abstract class Graphics {

	private static BufferStrategy strategy;
	private static Graphics2D graphics;
	private static int width;
	private static int height;
	private static Composite previousComposite;
	private static Stack<AffineTransform> transformations;
	private static Color backgroundColour;
	private static Font defaultFont;
	
	public enum HorizontalAlign {
		LEFT,
		RIGHT,
		CENTRE;
	}
	
	public enum VerticalAlign {
		TOP,
		MIDDLE,
		BOTTOM;
	}
	
	public static void initialise() {
		strategy = Window.canvas.getBufferStrategy();
		width = Window.canvas.getWidth();
		height = Window.canvas.getHeight();
		graphics = (Graphics2D)strategy.getDrawGraphics();
		defaultFont = graphics.getFont();
		transformations = new Stack<AffineTransform>();
		previousComposite = null;
		setBackgroundColour(Color.BLACK);
	}
	
	public static void clear() {
		strategy.show();
		graphics.clearRect(0, 0, width, height);
		setColour(255, 255, 255, 255);
	}
	
	private static void setAlpha() {
		previousComposite = graphics.getComposite();
		float alpha = (float)graphics.getColor().getAlpha() / 255;
		Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        graphics.setComposite(comp);
	}
	
	private static void resetAlpha() {
		if (previousComposite != null) {
			graphics.setComposite(previousComposite);
		}
	}
	
	public static void draw(Image img, double x, double y, double scale) {
		push();
		translate(x, y);
		scale(scale);
		draw(img, 0, 0);
		pop();
	}
	
	public static void draw(Image img, double x, double y) {
		setAlpha();
//		img.setTint(graphics.getColor()).draw(graphics, x, y);
		img.draw(graphics, x, y);
		resetAlpha();
	}
	
	public static void drawq(Image img, Rectangle quad, double x, double y, double scale) {
		push();
		translate(x, y);
		scale(scale);
		drawq(img, quad, 0, 0);
		pop();
	}

	public static void drawq(Image img, Rectangle quad, double x, double y) {
		setAlpha();
		img.drawq(graphics, quad, x, y);
		resetAlpha();
	}
	
	public static Color inverse(Color c) {
		return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
	}
	
	public static Color getColour() {
		return graphics.getColor();
	}
	
	public static void setColour(Color colour) {
		graphics.setColor(colour);
	}
	
	public static void setColour(int r, int g, int b) {
		setColour(r, g, b, 255);
	}
	
	public static void setColour(int r, int g, int b, int a) {
		r = Math.max(0, Math.min(r, 255));
		g = Math.max(0, Math.min(g, 255));
		b = Math.max(0, Math.min(b, 255));
		a = Math.max(0, Math.min(a, 255));
		graphics.setColor(new Color(r, g, b, a));
	}
	
	public static Color getBackgroundColour() {
		return backgroundColour;
	}
	
	public static void setBackgroundColour(Color colour) {
		backgroundColour = colour;
		graphics.setBackground(backgroundColour);
	}
	
	public static void setBackgroundColour(int r, int g, int b) {
		backgroundColour = new Color(r, g, b);
	}
	
	public static void setFont(Font font) {
		graphics.setFont(font);
	}
	
	public static void setFont(double fontSize) {
		graphics.setFont(getFont().deriveFont((float)fontSize));
	}
	
	public static void setFont() {
		graphics.setFont(defaultFont);
	}
	
	public static Font getFont() {
		return graphics.getFont();
	}
	
	public static int getFontWidth(String text) {
		return graphics.getFontMetrics().stringWidth(text);
	}
	
	public static int getFontHeight(String text) {
		int lines = text.split("\n").length;
		return graphics.getFontMetrics().getHeight() * lines;
	}
	
	public static void rectangle(boolean fill, double x, double y, double width, double height) {
		if (fill) {
			graphics.fillRect((int)x, (int)y, (int)width, (int)height);
		} else {
			graphics.drawRect((int)x, (int)y, (int)width, (int)height);
		}
	}
	
	public static void roundedRectangle(boolean fill, double x, double y, double width, double height, double radius) {
		graphics.drawRoundRect((int)x, (int)y, (int)width, (int)height, (int)radius, (int)radius);
	}
	
	public static void arc(boolean fill, double x, double y, double radius, double startAngle, double endAngle) {
		// Angles negated so clockwise is positive.
		int angleBegin = -(int)Math.toDegrees(startAngle);
		int angleSize = -(int)Math.toDegrees(endAngle) - angleBegin;
		int width = (int)radius * 2;
		int height = width;
		int xPos = (int)x - width / 2;
		int yPos = (int)y - height / 2;
		if (fill) {
			graphics.fillArc(xPos, yPos, width, height, angleBegin, angleSize);
		} else {
			graphics.drawArc(xPos, yPos, width, height, angleBegin, angleSize);
		}
	}
	
	public static void circle(boolean fill, double x, double y, double radius) {
		arc(fill, x, y, radius, 0, Math.PI * 2);
	}
	
	public static void line(double... points) {
		int[] xPoints = new int[points.length / 2];
		int[] yPoints = new int[points.length / 2];
		for (int i = 0; i < points.length; i ++) {
			if (i % 2 == 0) {
				xPoints[i/2] = (int)points[i];
			} else {
				yPoints[i/2] = (int)points[i];
			}
		}
		graphics.drawPolyline(xPoints, yPoints, xPoints.length);
	}
	
	public static void polygon(boolean fill, double... points) {
		int[] xPoints = new int[points.length / 2];
		int[] yPoints = new int[points.length / 2];
		for (int i = 0; i < points.length; i ++) {
			if (i % 2 == 0) {
				xPoints[i/2] = (int)points[i];
			} else {
				yPoints[i/2] = (int)points[i];
			}
		}
		if (fill) {
			graphics.fillPolygon(xPoints, yPoints, xPoints.length);
		} else {
			graphics.drawPolygon(xPoints, yPoints, xPoints.length);
		}
	}
	
	public static void print(String text, double x, double y) {
		printText(text, x, y);
	}

	public static void printCentred(String text, double x, double y) {
		print(text, x, y, getFontWidth(text), getFontHeight(text), HorizontalAlign.CENTRE, VerticalAlign.TOP);
	}
	
	public static void print(String text, double x, double y, double w, double h, VerticalAlign vertAlign) {
		print(text, x, y, w, h, HorizontalAlign.LEFT, vertAlign);
	}
	
	public static void print(String text, double x, double y, double w, double h, HorizontalAlign horizAlign, VerticalAlign vertAlign) {
		int fontWidth = getFontWidth(text);
		switch (horizAlign) {
			case RIGHT:  x += w;
						 x -= fontWidth;
						 break;
			case CENTRE: x += w / 2;
						 x -= fontWidth / 2;
						 break;
			case LEFT:
			default:     break;
		}
		int fontHeight = getFontHeight(text);
		switch (vertAlign) {
			case TOP:    y += fontHeight;
						 break;
			case MIDDLE: y += h / 2;
						 y += fontHeight / 2;
						 break;
			case BOTTOM: y += h;
			default:     break;
		}
		printText(text, x, y);
	}
	
	private static void printText(String text, double x, double y) {
		graphics.drawString(text, (int)x, (int)y);
	}

	public static void translate(double x, double y) {
		graphics.translate(x, y);
	}
	
	public static void scale(double sx, double sy) {
		graphics.scale(sx, sy);
	}
	
	public static void scale(double scaleFactor) {
		scale(scaleFactor, scaleFactor);
	}
		
	public static void rotate(double angle) {
		graphics.rotate(angle);
	}
	
	public static void shear(double shx, double shy) {
		graphics.shear(shx, shy);
	}

	public static void push() {
		transformations.push(graphics.getTransform());
	}
	
	public static void pop() {
		graphics.setTransform(transformations.pop());
	}

}
