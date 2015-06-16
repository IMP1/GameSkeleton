package jog;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image extends Graphics.Drawable {

	public Image(String path) throws IOException {
		super(ImageIO.read(Filesystem.getURL(path)));
		System.out.println("[Image] " + path + " loaded.");
	}
	
	private Image(BufferedImage img) {
		super(img);
	}

}
