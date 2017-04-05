package video;

import java.io.File;

import io.TextImageOutputStream;
import io.TextImagePrintStream;

/**
 *
 * @author Jacek
 */
public class NotificationArea extends PositionedTextImage {
	
	private TextImageOutputStream outputStream = new TextImageOutputStream(this);

    /**
     *
     */
    public TextImagePrintStream out = new TextImagePrintStream(outputStream);
	
    /**
     *
     */
    public NotificationArea() {
		super();
	}

    /**
     *
     * @param sourceFile
     * @throws Exception
     */
    public NotificationArea(File sourceFile) throws Exception {
		super(sourceFile);
	}

    /**
     *
     * @param width
     * @param height
     */
    public NotificationArea(int width, int height) {
		super(width, height);
	}

    /**
     *
     * @param sourceFile
     * @throws Exception
     */
    public NotificationArea(String sourceFile) throws Exception {
		super(sourceFile);
	}
	
    /**
     *
     */
    @Override
	public void draw() {
		super.draw();
		reset();
	}
	
    /**
     *
     */
    public void reset() {
		out.reset();
		fill(' ');
	}

}
