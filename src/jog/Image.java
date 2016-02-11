package jog;

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
	
	private Image(BufferedImage img) {
		super(img);
	}

}
