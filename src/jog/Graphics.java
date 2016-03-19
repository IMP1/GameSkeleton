package jog;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import run.Game;

public abstract class Graphics {

	private static BufferStrategy strategy;
	private static Graphics2D screen;
	private static int width;
	private static int height;
	private static Composite previousComposite;
	
	private static Stack<AffineTransform> transformations;
	private static AffineTransform defaultTransform;
	private static Color backgroundColour;
	private static Font defaultFont;
	private static Composite defaultComposite;
	private static Graphics2D currentCanvas;
	private static HorizontalAlign defaultHorizontalAlignment = HorizontalAlign.LEFT;
	private static VerticalAlign defaultVerticalAlignment = VerticalAlign.TOP;

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
	
	public static abstract class Drawable {
		
		protected final BufferedImage image;
		public Drawable(BufferedImage image) {
			this.image = image;
		}
		
		public Drawable(jog.Image img) {
			this(img.image);
		}
		
		public int getWidth() {
			return image.getWidth();
		}
		
		public int getHeight() {
			return image.getHeight();
		}

		public void clear() {
			clear(0, 0, image.getWidth(), image.getHeight());
		}
		
		public void clear(int x, int y, int width, int height) {
			Graphics2D g = (Graphics2D)image.getGraphics();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			g.setColor(new Color(0, 0, 0, 0));
			g.fillRect(x, y, width, height);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		}
		
		public void setPixel(int x, int y, int r, int g, int b) {
			setPixel(x, y, r, g, b, 255);
		}
		public void setPixel(int x, int y, int r, int g, int b, int a) {
			setPixel(x, y, new Color(r, g, b, a));
		}
		public void setPixel(int x, int y, Color colour) {
			image.setRGB(x, y, colour.getRGB());
		}
		
		public Image copy(int x, int y, int w, int h) {
			return new Image(image.getSubimage(x, y, w, h));
		}
		
		public void paste(Image img, int x, int y) {
			image.getGraphics().drawImage(img.image, x, y, null);
		}
		
		protected void draw(Graphics2D g, double x, double y) {
			g.drawImage(image, (int)x, (int)y, null);
		}
		
		protected void drawq(Graphics2D g, Rectangle quad, double x, double y) {
			g.drawImage(image, (int)x, (int)y, (int)x + quad.width, (int)y + quad.height, quad.x, quad.y, quad.x + quad.width, quad.y + quad.height, null);
		}
		
	}
	
	public static class Canvas extends Image {
		private final Graphics2D graphics;
		private Canvas(int width, int height) {
			super(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
			graphics = image.createGraphics();
		}
		public int getWidth()  { return image.getWidth(); }
		public int getHeight() { return image.getHeight(); }
	}
	
	public static void initialise() {
		strategy = Window.canvas.getBufferStrategy();
		width = Window.canvas.getWidth();
		height = Window.canvas.getHeight();
		screen = (Graphics2D)strategy.getDrawGraphics();
		defaultFont = screen.getFont();
		defaultComposite = screen.getComposite();
		previousComposite = null;
		currentCanvas = screen;
		transformations = new Stack<AffineTransform>();
		defaultTransform = currentCanvas.getTransform();
		setBackgroundColour(Color.BLACK);
		if (Game.LOGGING)
			System.out.println("[Graphics] Initialised.");
	}
	
	public static void clear() {
		strategy.show();
		screen.clearRect(0, 0, width, height);
		setColour(255, 255, 255, 255);
	}
	
	private static void setImageColour() {
		// TODO: add tint
		previousComposite = currentCanvas.getComposite();
		float alpha = (float)currentCanvas.getColor().getAlpha() / 255;
		Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		currentCanvas.setComposite(comp);
	}
	
	private static void resetImageColour() {
		if (previousComposite != null) {
			currentCanvas.setComposite(previousComposite);
			previousComposite = null;
		}
	}
	
	public static void draw(Drawable img, double x, double y, double scale) {
		push();
		translate(x, y);
		scale(scale);
		draw(img, 0, 0);
		pop();
	}
	
	public static void draw(Drawable img, double x, double y) {
		setImageColour();
		img.draw(currentCanvas, x, y);
		resetImageColour();
	}
	
	public static void draw(Drawable img, Rectangle quad, double x, double y, double scale) {
		push();
		translate(x, y);
		scale(scale);
		draw(img, quad, 0, 0);
		pop();
	}

	public static void draw(Drawable img, Rectangle quad, double x, double y) {
		setImageColour();
		img.drawq(currentCanvas, quad, x, y);
		resetImageColour();
	}
	
	public static Color inverse(Color c) {
		return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
	}
	
	public static void setComposite() {
		currentCanvas.setComposite(defaultComposite);
	}
	
	public static void setComposite(int comp) {
		currentCanvas.setComposite(AlphaComposite.getInstance(comp, 1));
	}
	
	public static Color getColour() {
		return currentCanvas.getColor();
	}
	
	public static void setColour(Color colour) {
		currentCanvas.setColor(colour);
	}
	
	public static void setColour(int r, int g, int b) {
		setColour(r, g, b, 255);
	}
	
	public static void setColour(int r, int g, int b, int a) {
		r = Math.max(0, Math.min(r, 255));
		g = Math.max(0, Math.min(g, 255));
		b = Math.max(0, Math.min(b, 255));
		a = Math.max(0, Math.min(a, 255));
		currentCanvas.setColor(new Color(r, g, b, a));
	}
	
	public static Color getBackgroundColour() {
		return backgroundColour;
	}
	
	public static void setBackgroundColour(Color colour) {
		backgroundColour = colour;
		currentCanvas.setBackground(backgroundColour);
	}
	
	public static void setBackgroundColour(int r, int g, int b) {
		backgroundColour = new Color(r, g, b);
	}
	
	public static void setScissor() {
		currentCanvas.setClip(null);
	}
	
	public static void setScissor(Rectangle rect) {
		currentCanvas.setClip(rect);
	}
	
	public static Canvas newCanvas() {
		return newCanvas(width, height);
	}
	
	public static void setCanvas() {
		currentCanvas = screen;
	}
	
	public static void setCanvas(Canvas canvas) {
		currentCanvas = canvas.graphics;
	}
	
	public static Canvas newCanvas(int width, int height) {
		return new Canvas(width, height);
	}
	
	public static void setFont(Font font) {
		currentCanvas.setFont(font);
	}
	
	public static void setFont(double fontSize) {
		currentCanvas.setFont(getFont().deriveFont((float)fontSize));
	}
	
	public static void setFont(Font font, double fontSize) {
		currentCanvas.setFont(font.deriveFont((float)fontSize));
	}
	
	public static void setFont() {
		currentCanvas.setFont(defaultFont);
	}
	
	public static Font getFont() {
		return currentCanvas.getFont();
	}
	
	public static Font newFont(String filename) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(jog.Filesystem.getPath(filename)));
			ge.registerFont(font);
			return font;
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getFontWidth(String text) {
		return currentCanvas.getFontMetrics().stringWidth(text);
	}
	
	public static int getFontHeight(String text) {
		int lines = text.split("\n").length;
		return currentCanvas.getFontMetrics().getHeight() * lines;
	}
	
	public static void rectangle(boolean fill, Rectangle rect) {
		rectangle(fill, rect.x, rect.y, rect.width, rect.height);
	}
	
	public static void rectangle(boolean fill, double x, double y, double width, double height) {
		if (fill) {
			currentCanvas.fillRect((int)x, (int)y, (int)width, (int)height);
		} else {
			currentCanvas.drawRect((int)x, (int)y, (int)width, (int)height);
		}
	}
	
	public static void roundedRectangle(boolean fill, double x, double y, double width, double height, double radius) {
		if (fill) {
			currentCanvas.fillRoundRect((int)x, (int)y, (int)width, (int)height, (int)radius, (int)radius);
		} else {
			currentCanvas.drawRoundRect((int)x, (int)y, (int)width, (int)height, (int)radius, (int)radius);
		}
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
			currentCanvas.fillArc(xPos, yPos, width, height, angleBegin, angleSize);
		} else {
			currentCanvas.drawArc(xPos, yPos, width, height, angleBegin, angleSize);
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
		currentCanvas.drawPolyline(xPoints, yPoints, xPoints.length);
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
			currentCanvas.fillPolygon(xPoints, yPoints, xPoints.length);
		} else {
			currentCanvas.drawPolygon(xPoints, yPoints, xPoints.length);
		}
	}

	public static void printCentred(String text, double x, double y) {
		print(text, x, y, 0, 0, HorizontalAlign.CENTRE, VerticalAlign.MIDDLE);
	}
	
	public static void print(String text, double x, double y) {
		print(text, x, y, 0, 0, defaultHorizontalAlignment, defaultVerticalAlignment);
	}
	
	public static void print(String text, double x, double y, HorizontalAlign horizAlign) {
		print(text, x, y, horizAlign, defaultVerticalAlignment);
	}
	
	public static void print(String text, double x, double y, VerticalAlign vertAlign) {
		print(text, x, y, defaultHorizontalAlignment, vertAlign);
	}
	
	public static void print(String text, double x, double y, HorizontalAlign horizAlign, VerticalAlign vertAlign) {
		print(text, x, y, 0, 0, horizAlign, vertAlign);
	}
	
	public static void print(String text, double x, double y, double w, double h, HorizontalAlign horizAlign) {
		print(text, x, y, w, h, horizAlign, defaultVerticalAlignment);
	}
	
	public static void print(String text, double x, double y, double w, double h, VerticalAlign vertAlign) {
		print(text, x, y, w, h, defaultHorizontalAlignment, vertAlign);
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
		currentCanvas.drawString(text, (int)x, (int)y);
	}

	public static void translate(double x, double y) {
		currentCanvas.translate(x, y);
	}
	
	public static void scale(double sx, double sy) {
		currentCanvas.scale(sx, sy);
	}
	
	public static void scale(double scaleFactor) {
		scale(scaleFactor, scaleFactor);
	}
		
	public static void rotate(double angle) {
		currentCanvas.rotate(angle);
	}
	
	public static void shear(double shx, double shy) {
		currentCanvas.shear(shx, shy);
	}

	public static void push() {
		transformations.push(currentCanvas.getTransform());
	}
	
	public static void pop() {
		currentCanvas.setTransform(transformations.pop());
	}
	
	public static void origin() {
		currentCanvas.setTransform(defaultTransform);
	}

}
