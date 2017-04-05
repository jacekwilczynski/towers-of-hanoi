package game;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

import time.Stopwatch;

/**
 *
 * @author Jacek
 */
public class Score implements Comparable<Score>, Serializable {

	private static final long serialVersionUID = -9043358972968998490L;

	/*
	 * FIELDS
	 */

	private static final String NO_NAME = "nameless";
	private String name = new String();
	@SuppressWarnings("unused")
	private int numberOfPegs = Game.DEFAULT_NUMBER_OF_PEGS;
	private int numberOfDisks = 0;
	private int numberOfMoves = 0;
	private long time = 0;
	private long points;

	private transient Stopwatch stopwatch;

	/*
	 * PACKAGE-LEVEL CONSTRUCTOR - starts the stopwatch
	 */

	Score(int numberOfDisks) {
		this.numberOfDisks = numberOfDisks;
		stopwatch = new Stopwatch();
	}

	/*
	 * PACKAGE-LEVEL METHODS - to be launched only from within the game - can
	 * modify fields
	 */

	void nextMove() {
		this.numberOfMoves++;
	}

	void setTime(long time) {
		this.time = time;
	}

	/*
	 * PUBLIC METHODS - to be launched from outside of the game - can only read
	 * data
	 */

    /**
     *
     * @return
     */


	public int getNumberOfDisks() {
		return numberOfDisks;
	}

    /**
     *
     * @return
     */
    public int getNumberOfMoves() {
		return numberOfMoves;
	}

    /**
     *
     * @param numberOfDisks
     * @return
     */
    public static int getMinimumNumberOfMoves(int numberOfDisks) {
		return (int) Math.pow(2, numberOfDisks) - 1;
	}

    /**
     *
     * @return
     */
    public int getMinimumNumberOfMoves() {
		return getMinimumNumberOfMoves(getNumberOfDisks());
	}

    /**
     *
     * @return
     */
    public long updateTime() {
		return time = stopwatch.getTime();
	}

    /**
     *
     * @return
     */
    public long getTime() {
		return time;
	}

    /**
     *
     */
    public void calculatePoints() {
		points = (getMinimumNumberOfMoves() * 5000L) - getNumberOfMoves() * 500 - getTime();
	}

    /**
     *
     * @return
     */
    public long getPoints() {
		return points;
	}

    /**
     *
     * @return
     */
    public String getPointsString() {
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
		numberFormat.setMinimumFractionDigits(3);
		return numberFormat.format((double) points / 1000);
	}

    /**
     *
     * @return
     */
    public String getName() {
		return name;
	}

    /**
     *
     * @param name
     * @return
     */
    public boolean setName(String name) {
		if (this.name.isEmpty()) { // Can only set a name once
			this.name = name.isEmpty() ? NO_NAME : name;
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(Score o) {
		long other = o.getPoints();
		return points < other ? 1 : points > other ? -1 : 0;
	}

}
