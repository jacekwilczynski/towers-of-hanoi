package video;

import java.io.File;

/**
 *
 * @author Jacek
 */
public class PositionedTextImage extends TextImage implements Positioned, Drawable {

	/*
	 * PRIVATE & PROTECTED DATA
	 */

	private double x = 0;
	private double y = 0;

	private int xOffset = 0;
	private int yOffset = 0;

    /**
     *
     */
    public TextModeScreen screen = null;

	/*
	 * PRIVATE METHODS
	 */

	private void readOffset() {
		try {
			xOffset = Integer.parseInt(readLine(getHeight() - 2).trim());
			yOffset = Integer.parseInt(readLine(getHeight() - 1).trim());
			trimBottom(2);
		} catch (Exception e) {
			centerOffset();
		}
	}

	/*
	 * CONSTRUCTORS
	 */

    /**
     *
     */

	
	public PositionedTextImage() {
		super();
	}

    /**
     *
     * @param sourceFile
     * @throws Exception
     */
    public PositionedTextImage(File sourceFile) throws Exception {
		super(sourceFile);
		readOffset();
	}

    /**
     *
     * @param width
     * @param height
     */
    public PositionedTextImage(int width, int height) {
		super(width, height);
	}

    /**
     *
     * @param sourceFile
     * @throws Exception
     */
    public PositionedTextImage(String sourceFile) throws Exception {
		this(new File(sourceFile));
	}

	/*
	 * GETTERS
	 */

    /**
     *
     * @return
     */


	public double getX() {
		return x;
	}

    /**
     *
     * @return
     */
    public int getXint() {
		return (int) Math.round(x);
	}

    /**
     *
     * @return
     */
    public double getY() {
		return y;
	}

    /**
     *
     * @return
     */
    public int getYint() {
		return (int) Math.round(y);
	}

    /**
     *
     * @return
     */
    public int getXOffset() {
		return xOffset;
	}

    /**
     *
     * @return
     */
    public int getYOffset() {
		return yOffset;
	}

	/*
	 * SETTERS
	 */

    /**
     *
     * @param x
     */


	public void setX(double x) {
		this.x = x;
	}

    /**
     *
     * @param y
     */
    public void setY(double y) {
		this.y = y;
	}

    /**
     *
     * @param x
     * @param y
     */
    public void setXY(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
    /**
     *
     */
    public void centerPosition() {
		setXY(screen.getWidth() / 2, screen.getHeight() / 2);
	}

    /**
     *
     * @param xOffset
     */
    public void setXOffset(int xOffset) {
		this.xOffset = xOffset;
	}

    /**
     *
     * @param yOffset
     */
    public void setYOffset(int yOffset) {
		this.yOffset = yOffset;
	}

    /**
     *
     * @param xOffset
     * @param yOffset
     */
    public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

    /**
     *
     */
    public void centerOffset() {
		setOffset(getWidth() / 2, getHeight() / 2);
	}
	
    /**
     *
     */
    public void center() {
		centerPosition();
		centerOffset();
	}

    /**
     *
     * @param screen
     */
    public void assign(TextModeScreen screen) {
		this.screen = screen;
	}

	/*
	 * OUTPUT
	 */

    /**
     *
     */


	public void draw() {
		this.putOn(screen, x - xOffset, y - yOffset);
	}

}
