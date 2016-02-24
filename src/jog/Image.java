package jog;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import run.Game;

public class Image extends Graphics.Drawable {

	private static BufferedImage readImage(String path) {
		try {
			return ImageIO.read(Filesystem.getURL(path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Image(String path) {
		super(readImage(path));
		if (Game.LOGGING)
			System.out.println("[Image] " + path + " loaded.");
	}
	
	public Image(int width, int height) {
		super(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
	}
	
	private Image(BufferedImage img) {
		super(img);
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

}
