package game;

import video.Drawable;
import video.PositionedTextImage;

abstract class Item implements Drawable {

	protected Game game;
	protected int id;
	protected PositionedTextImage image;
	
	@Override
	public void draw() {
		image.draw();
	}
	
	public double getX() {
		return image.getX();
	}
	
	public int getXint() {
		return image.getXint();
	}
	
	public double getY() {
		return image.getY();
	}
	
	public int getYint() {
		return image.getYint();
	}

}
