package time;

import java.util.Date;

/**
 *
 * @author Jacek
 */
public class Stopwatch {

	private long start;
	private long last;
	
    /**
     *
     */
    public int scale = 1;
	
    /**
     *
     */
    public Stopwatch() {
		last = new Date().getTime();
		start = last;
	}
	
    /**
     *
     * @return
     */
    public long getmsSinceLastCall() {
		long now = new Date().getTime();
		long difference = now - last;
		last = now;
		return difference * scale;
	}
	
    /**
     *
     * @return
     */
    public long getTime() {
		long now = new Date().getTime();
		return (now - start) * scale;
	}

	public String toString() {
		return String.valueOf(getTime());
	}
	
}
