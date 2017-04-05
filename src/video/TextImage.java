package video;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author Jacek
 */
public class TextImage {

	/*
	 * INNER CLASSES
	 */

	private class Matrix {
		public char[][] data;

		public void resize(int width, int height) {
			data = new char[width][height];
		}
	}

	private class ImageTextFileReader {

		File file;

		public ImageTextFileReader(File file) throws IOException {
			this.file = file;
			readDimensions();
		}

		public void readDimensions() throws IOException {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			width = 0;
			height = 0;
			while ((line = reader.readLine()) != null) {
				height++;
				if (line.length() > width)
					width = line.length();
			}
			reader.close();
		}

		public void readImage() throws IOException {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			for (int y = 0; y < height; y++) {
				line = reader.readLine();
				for (int x = 0; x < line.length(); x++) {
					matrix.data[x][y] = line.charAt(x);
				}
			}
			reader.close();
		}
	}

	/*
	 * PRIVATE & PROTECTED DATA
	 */

	private static char CHAR_TRANSPARENT = 0;

	private int width, height;
	private Matrix matrix;

	/*
	 * CONSTRUCTORS
	 */

    /**
     *
     */


	public TextImage() {
		matrix = new Matrix();
	}

    /**
     *
     * @param width
     * @param height
     */
    public TextImage(int width, int height) {
		this();
		this.width = width;
		this.height = height;
		matrix.resize(width, height);
	}

    /**
     *
     * @param sourceFile
     * @throws Exception
     */
    public TextImage(File sourceFile) throws Exception {
		this();
		loadFromFile(sourceFile);
	}

    /**
     *
     * @param sourceFile
     * @throws Exception
     */
    public TextImage(String sourceFile) throws Exception {
		this(new File(sourceFile));
	}

	/*
	 * GETTERS
	 */

    /**
     *
     * @return
     */


	public int getWidth() {
		return width;
	}

    /**
     *
     * @return
     */
    public int getHeight() {
		return height;
	}

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public char getChar(int x, int y) {
		try {
			return matrix.data[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return (char) -1;
		}
	}
	
    /**
     *
     * @param from
     * @param to
     * @param y
     * @return
     */
    public String readLine(int from, int to, int y) {
		StringBuilder line = new StringBuilder();
		for (int x = from; x <= to; x++) {
			line.append(matrix.data[x][y]);
		}
		return line.toString();
	}

    /**
     *
     * @param y
     * @return
     */
    public String readLine(int y) {
		return readLine(0, getWidth() - 1, y);
	}
	
	/*
	 * SETTERS
	 */

    /**
     *
     * @param x
     * @param y
     * @param c
     */


	public void setChar(int x, int y, char c) {
		try {
			matrix.data[x][y] = c;
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

    /**
     *
     * @param c
     */
    public void fill(char c) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				matrix.data[x][y] = c;
			}
		}
	}

	/*
	 * PROCESSING
	 */

    /**
     *
     * @param width
     * @param height
     */


	public void resize(int width, int height) {
		matrix.resize(width, height);
		this.width = width;
		this.height = height;
	}

    /**
     *
     * @param lines
     */
    public void trimBottom(int lines) {
		height -= lines;
	}

	/*
	 * DRAWING
	 */

    /**
     *
     * @param x
     * @param y
     * @param text
     */

	
	public void putText(int x, int y, String text) {
		for (int i = 0; i < text.length(); i++) {
			setChar(x + i, y, text.charAt(i));
		}
	}
	
    /**
     *
     * @param y
     * @param c
     */
    public void hLine(int y, char c) {
		for (int x = 0; x < width; x++)
			matrix.data[x][y] = c;
	}
	
    /**
     *
     * @param left
     * @param right
     * @param y
     * @param c
     */
    public void hLine(int left, int right, int y, char c) {
		for (int x = left; x <= right; x++)
			matrix.data[x][y] = c;
	}
	
    /**
     *
     * @param x
     * @param c
     */
    public void vLine(int x, char c) {
		for (int y = 0; y < height; y++)
			matrix.data[x][y] = c;
	}
	
    /**
     *
     * @param x
     * @param top
     * @param bottom
     * @param c
     */
    public void vLine(int x, int top, int bottom, char c) {
		for (int y = top; y <= bottom; y++)
			matrix.data[x][y] = c;
	}
	
	/*
	 * COPYING
	 */

    /**
     *
     * @param image
     * @param offsetX
     * @param offsetY
     */


	public void putOn(TextImage image, int offsetX, int offsetY) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.data[x][y] != CHAR_TRANSPARENT)
					image.setChar(offsetX + x, offsetY + y, matrix.data[x][y]);
			}
		}
	}

    /**
     *
     * @param image
     * @param offsetX
     * @param offsetY
     */
    public void putOn(TextImage image, double offsetX, double offsetY) {
		putOn(image, (int) Math.round(offsetX), (int) Math.round(offsetY));
	}

    /**
     *
     * @param stream
     */
    public void putOn(PrintStream stream) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				stream.print(matrix.data[x][y]);
			}
			stream.println();
		}
	}

    /**
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public TextImage subImage(int left, int top, int right, int bottom) {
		int newWidth = right - left + 1;
		int newHeight = bottom - top + 1;
		TextImage result = new TextImage(newWidth, newHeight);
		for (int y = 0; y < newHeight; y++) {
			for (int x = 0; x <= newWidth; y++) {
				try {
					result.setChar(x, y, matrix.data[left + x][top + y]);
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		return result;
	}

	/*
	 * FILES
	 */

    /**
     *
     * @param file
     * @throws Exception
     */


	public void loadFromFile(File file) throws Exception {

		ImageTextFileReader reader = new ImageTextFileReader(file);
		reader.readDimensions();
		matrix.resize(width, height);
		reader.readImage();

	}

    /**
     *
     * @param file
     * @throws Exception
     */
    public void loadFromFile(String file) throws Exception {
		loadFromFile(new File(file));
	}

}
