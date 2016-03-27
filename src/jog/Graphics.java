package jog;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
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
	private static Canvas currentCanvas;
	private static Graphics2D currentGraphics;
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
		currentCanvas = null;
		currentGraphics = screen;
		transformations = new Stack<AffineTransform>();
		defaultTransform = currentGraphics.getTransform();
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
		previousComposite = currentGraphics.getComposite();
		float alpha = (float)currentGraphics.getColor().getAlpha() / 255;
		Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		currentGraphics.setComposite(comp);
	}
	
	private static void resetImageColour() {
		if (previousComposite != null) {
			currentGraphics.setComposite(previousComposite);
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
		img.draw(currentGraphics, x, y);
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
		img.drawq(currentGraphics, quad, x, y);
		resetImageColour();
	}
	
	public static Color inverse(Color c) {
		return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
	}
	
	public static void setComposite() {
		currentGraphics.setComposite(defaultComposite);
	}
	
	public static void setComposite(int comp) {
		currentGraphics.setComposite(AlphaComposite.getInstance(comp, 1));
	}
	
	public static Color getColour() {
		return currentGraphics.getColor();
	}
	
	public static void setColour(Color colour) {
		currentGraphics.setColor(colour);
	}
	
	public static void setColour(int r, int g, int b) {
		setColour(r, g, b, 255);
	}
	
	public static void setColour(int r, int g, int b, int a) {
		r = Math.max(0, Math.min(r, 255));
		g = Math.max(0, Math.min(g, 255));
		b = Math.max(0, Math.min(b, 255));
		a = Math.max(0, Math.min(a, 255));
		currentGraphics.setColor(new Color(r, g, b, a));
	}
	
	public static Color getBackgroundColour() {
		return backgroundColour;
	}
	
	public static void setBackgroundColour(Color colour) {
		backgroundColour = colour;
		currentGraphics.setBackground(backgroundColour);
	}
	
	public static void setBackgroundColour(int r, int g, int b) {
		backgroundColour = new Color(r, g, b);
	}
	
	public static int getWidth() {
		if (currentCanvas == null) {
			return Window.getWidth();
		} else {
			return currentCanvas.getWidth();
		}
	}
	
	public static int getHeight() {
		if (currentCanvas == null) {
			return Window.getHeight();
		} else {
			return currentCanvas.getHeight();
		}
	}
	
	public static void setScissor() {
		currentGraphics.setClip(null);
	}
	
	public static void setScissor(Rectangle rect) {
		currentGraphics.setClip(rect);
	}
	
	public static Rectangle getScissor() {
		if (currentGraphics.getClipBounds() == null) {
			return new Rectangle(0, 0, getWidth(), getHeight());
		} else {
			return currentGraphics.getClipBounds();
		}
	}
	
	public static Canvas newCanvas() {
		return newCanvas(width, height);
	}
	
	public static void setCanvas() {
		currentGraphics = screen;
	}
	
	public static void setCanvas(Canvas canvas) {
		currentGraphics = canvas.graphics;
	}
	
	public static Canvas newCanvas(int width, int height) {
		return new Canvas(width, height);
	}
	
	public static void setFont(Font font) {
		currentGraphics.setFont(font);
	}
	
	public static void setFont(double fontSize) {
		currentGraphics.setFont(getFont().deriveFont((float)fontSize));
	}
	
	public static void setFont(Font font, double fontSize) {
		currentGraphics.setFont(font.deriveFont((float)fontSize));
	}
	
	public static void setFont() {
		currentGraphics.setFont(defaultFont);
	}
	
	public static Font getFont() {
		return currentGraphics.getFont();
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
		return currentGraphics.getFontMetrics().stringWidth(text);
	}
	
	public static int getFontHeight(String text) {
		int lines = text.split("\n").length;
		return currentGraphics.getFontMetrics().getHeight() * lines;
	}
	
	public static void setLineWidth(int lineWidth) {
		currentGraphics.setStroke(new BasicStroke(lineWidth));
	}
	
	public static void rectangle(boolean fill, Rectangle rect) {
		rectangle(fill, rect.x, rect.y, rect.width, rect.height);
	}
	
	public static void rectangle(boolean fill, double x, double y, double width, double height) {
		if (fill) {
			currentGraphics.fillRect((int)x, (int)y, (int)width, (int)height);
		} else {
			currentGraphics.drawRect((int)x, (int)y, (int)width, (int)height);
		}
	}
	
	public static void roundedRectangle(boolean fill, double x, double y, double width, double height, double radius) {
		if (fill) {
			currentGraphics.fillRoundRect((int)x, (int)y, (int)width, (int)height, (int)radius, (int)radius);
		} else {
			currentGraphics.drawRoundRect((int)x, (int)y, (int)width, (int)height, (int)radius, (int)radius);
		}
	}
	
	public static void ellipse(boolean fill, double x, double y, double xRadius, double yRadius) {
		int width = (int)xRadius * 2;
		int height = (int)yRadius * 2;
		x -= width / 2;
		y -= height / 2;
		if (fill) {
			currentGraphics.fillOval((int)x, (int)y, width, height);
		} else {
			currentGraphics.drawOval((int)x, (int)y, width, height);
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
			currentGraphics.fillArc(xPos, yPos, width, height, angleBegin, angleSize);
		} else {
			currentGraphics.drawArc(xPos, yPos, width, height, angleBegin, angleSize);
		}
	}
	
	public static void circle(boolean fill, double x, double y, double radius) {
		arc(fill, x, y, radius, 0, Math.PI * 2);
	}
	
	public static void point(double x, double y) {
		line(x, y, x, y);
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
		currentGraphics.drawPolyline(xPoints, yPoints, xPoints.length);
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
			currentGraphics.fillPolygon(xPoints, yPoints, xPoints.length);
		} else {
			currentGraphics.drawPolygon(xPoints, yPoints, xPoints.length);
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
		currentGraphics.drawString(text, (int)x, (int)y);
	}

	public static void translate(double x, double y) {
		currentGraphics.translate(x, y);
	}
	
	public static void scale(double sx, double sy) {
		currentGraphics.scale(sx, sy);
	}
	
	public static void scale(double scaleFactor) {
		scale(scaleFactor, scaleFactor);
	}
		
	public static void rotate(double angle) {
		currentGraphics.rotate(angle);
	}
	
	public static void shear(double shx, double shy) {
		currentGraphics.shear(shx, shy);
	}

	public static void push() {
		transformations.push(currentGraphics.getTransform());
	}
	
	public static void pop() {
		currentGraphics.setTransform(transformations.pop());
	}
	
	public static void origin() {
		currentGraphics.setTransform(defaultTransform);
	}

}
