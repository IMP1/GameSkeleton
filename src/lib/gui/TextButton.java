package lib.gui;

import java.awt.Color;
import java.awt.Rectangle;

import jog.Graphics.HorizontalAlign;
import jog.Graphics.VerticalAlign;

public class TextButton extends lib.Button {
	
	public final static Color DEFAULT_COLOUR  = new Color(0, 128, 192);
	public final static Color DISABLED_COLOUR = new Color(128, 128, 128);
	public final static Color HOVER_COLOUR    = new Color(32, 192, 224);
	public final static Color PRESSED_COLOUR  = new Color(64, 224, 255);
	
	public final String text;

	public TextButton(String text, int x, int y, int width, int height) {
		super(new Rectangle(x, y, width, height));
		this.text = text;
	}
	
	private void drawText() {
		Rectangle r = (Rectangle)activeArea;
		jog.Graphics.print(text, r.x, r.y, r.width, r.height, HorizontalAlign.CENTRE, VerticalAlign.MIDDLE);
		jog.Graphics.rectangle(false, r.x, r.y, r.width, r.height);
	}

	@Override
	protected void drawDisabled() {
		jog.Graphics.setColour(DISABLED_COLOUR);
		drawText();
	}

	@Override
	protected void drawDefault() {
		jog.Graphics.setColour(DEFAULT_COLOUR);
		drawText();
	}

	@Override
	protected void drawHover() {
		jog.Graphics.setColour(HOVER_COLOUR);
		drawText();
	}

	@Override
	protected void drawPressed() {
		jog.Graphics.setColour(PRESSED_COLOUR);
		drawText();
	}

}
