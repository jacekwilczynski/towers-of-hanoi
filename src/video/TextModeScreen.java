package video;

import java.io.PrintStream;

/**
 *
 * @author Jacek
 */
public class TextModeScreen extends TextImage {
	
	/*
	 * PRIVATE DATA
	 */
	
	private PrintStream out;
	
	/*
	 * PUBLIC DATA - for convenience's sake
	 */

    /**
     *
     */

	public boolean useBorder = true;

    /**
     *
     */
    public char[][] borderStyle = {
			{'/' , '=',  '\\'},
			{'|',  ' ', '|'},
			{'\\', '=',  '/'},
	};

    /**
     *
     */
    public int spacesAbove = 3;
	
	/*
	 * CONSTRUCTOR
	 */

    /**
     *
     * @param width
     * @param height
     */

	
	public TextModeScreen(int width, int height) {
		super(width, height);
		out = System.out;
	}
	
	/*
	 * PUBLIC METHODS
	 */

    /**
     *
     */

	
	public void clear() {
		fill(' ');
	}
	
    /**
     *
     */
    public void refresh() {
		int i;
		
		for (i = 0; i < spacesAbove; i++)
			out.println();
		
		if (useBorder) {
			out.print(borderStyle[0][0]);
			for (i = 0; i < getWidth(); i++) {
				out.print(borderStyle[0][1]);
			}
			out.println(borderStyle[0][2]);
		}
		
		for (i = 0; i < getHeight(); i++) {
			if (useBorder)
				out.print(borderStyle[1][0]);
			out.print(this.readLine(i));
			if (useBorder)
				out.print(borderStyle[1][2]);
			out.println();
		}
		
		if (useBorder) {
			out.print(borderStyle[2][0]);
			for (i = 0; i < getWidth(); i++) {
				out.print(borderStyle[2][1]);
			}
			out.print(borderStyle[2][2]);
			out.println();
		}
		
	}

}
