package video;

/**
 *
 * @author Jacek
 */
public interface Positioned {
	
    /**
     *
     * @return
     */
    public double getX();

    /**
     *
     * @return
     */
    public int getXint();

    /**
     *
     * @return
     */
    public double getY();

    /**
     *
     * @return
     */
    public int getYint();

    /**
     *
     * @param x
     */
    public void setX(double x);

    /**
     *
     * @param y
     */
    public void setY(double y);

    /**
     *
     * @param x
     * @param y
     */
    public void setXY(double x, double y);

    /**
     *
     */
    public void centerPosition();
	
    /**
     *
     * @return
     */
    public int getXOffset();

    /**
     *
     * @return
     */
    public int getYOffset();

    /**
     *
     * @param xOffset
     */
    public void setXOffset(int xOffset);

    /**
     *
     * @param yOffset
     */
    public void setYOffset(int yOffset);

    /**
     *
     * @param xOffset
     * @param yOffset
     */
    public void setOffset(int xOffset, int yOffset);

    /**
     *
     */
    public void centerOffset();
	
    /**
     *
     */
    public void center();
	
    /**
     *
     */
    public void draw();

}
