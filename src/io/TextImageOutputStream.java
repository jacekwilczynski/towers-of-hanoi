package io;

import java.io.OutputStream;

import video.TextImage;

/**
 *
 * @author Jacek
 */
public class TextImageOutputStream extends OutputStream {

    /**
     *
     */
    public TextImage target;

    /**
     *
     */
    public int cursorX = 0;

    /**
     *
     */
    public int cursorY = 0;

    /**
     *
     */
    public int tabWidth = 10;

    /**
     *
     * @param target
     */
    public TextImageOutputStream(TextImage target) {
		this.target = target;
	}

	// private int spacesLeftInRow() {
	// return cursorX + target.getWidth();
	// }

	private int lastWordX = 0;

	@Override
	public void write(int b) {
		if (b == '\n') {
			cursorX = 0;
			cursorY++;
		} else if (b == '\r') {
			// Do nothing as \r always comes together with \n
		} else if (b == '\t') {
			cursorX = ((cursorX / tabWidth) + 1) * tabWidth;
		} else {
			// Check if there's space in the line or do we need to do word wrapping
			if (cursorX < target.getWidth() - 1) {
				target.putText(cursorX, cursorY, String.valueOf((char) b));
				if (b == ' ') {
					lastWordX = cursorX;
				}
				cursorX++;
			} else {
				String lastWord = target.readLine(lastWordX + 1, cursorX, cursorY); // Remember what of the last word we've written so far
				target.putText(0, cursorY + 1, lastWord); // Copy this to the beginning of the next line
				target.hLine(lastWordX, cursorX, cursorY, ' '); // Clear the remaining part of the word from the end of current line
				cursorY++; // Go to next line
				cursorX = lastWord.length() - 1; // Set the cursor in the appropriate position to write the next character
				write(b); // Write the lext character
			}
		}
	}

    /**
     *
     */
    public void reset() {
		cursorX = 0;
		cursorY = 0;
	}

}
