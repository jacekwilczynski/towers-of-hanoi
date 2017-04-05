package io;

import java.io.PrintStream;

/**
 *
 * @author Jacek
 */
public class TextImagePrintStream extends PrintStream {

	TextImageOutputStream out;

    /**
     *
     * @param out
     */
    public TextImagePrintStream(TextImageOutputStream out) {
		super(out);
		this.out = out;
		this.print("");
	}

    /**
     *
     */
    public void reset() {
		out.reset();
	}
	
}
