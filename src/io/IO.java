package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import io.exceptions.NumberOutOfRangeException;

/**
 *
 * @author Jacek
 */
public class IO {

	private static boolean ioException = false;

    /**
     *
     */
    public static Scanner in = new Scanner(System.in);

    /**
     *
     * @param lowerBound
     * @param upperBound
     * @return
     * @throws NumberFormatException
     * @throws NumberOutOfRangeException
     */
    public static int getInt(int lowerBound, int upperBound) throws NumberFormatException, NumberOutOfRangeException {
		System.out.print("> ");
		int number;
		number = Integer.parseInt(IO.in.nextLine());
		if (number < lowerBound)
			throw new NumberOutOfRangeException(lowerBound);
		if (number > upperBound)
			throw new NumberOutOfRangeException(upperBound);
		return number;
	}

    /**
     *
     * @param x
     * @return
     */
    public static String getOrdinal(int x) {
		return x + ((x % 100 >= 4) && (x % 100 <= 20) ? "th"
				: (x % 10) == 1 ? "st" : (x % 10) == 2 ? "nd" : (x % 10) == 3 ? "rd" : "th");
	}

	private static void flagIOException() {
		ioException = true;
	}

    /**
     *
     * @param fileName
     * @return
     */
    public static String backupFile(String fileName) {
		StringBuilder sb = new StringBuilder(fileName);
		int dot = sb.lastIndexOf(".");
		String dateString = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date());
		sb.insert(dot != -1 ? dot : sb.length(), " backup " + dateString);

		Path sourcePath = Paths.get(fileName);
		Path targetPath = Paths.get(sb.toString());
		try {
			Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			flagIOException();
		}
		return sb.toString();
	}

    /**
     *
     * @return
     */
    public static boolean getIOException() {
		if (ioException) {
			ioException = false;
			return true;
		} else
			return false;
	}

    /**
     *
     * @param ms
     * @return
     */
    public static String msToNiceString(long ms) {
		long hours = ms / 3600000;
		ms = ms % 3600000;
		long minutes = ms / 60000;
		ms = ms % 60000;
		long seconds = ms / 1000;
		ms = ms % 1000;

		if (hours > 0) {
			return String.format("%d:%02d:%02d.03%d", hours, minutes, seconds, ms);
		} else if (minutes > 0) {
			return String.format("%d:%02d.%03d", minutes, seconds, ms);
		} else if (seconds > 0) {
			return String.format("%d.%03ds", seconds, ms);
		} else {
			return String.format("%dms", ms);
		}

	}

    /**
     *
     * @param ms
     * @return
     */
    public static String msToNiceStringNoUnits(long ms) {
		long hours = ms / 3600000;
		ms = ms % 3600000;
		long minutes = ms / 60000;
		ms = ms % 60000;
		long seconds = ms / 1000;
		ms = ms % 1000;

		if (hours > 0) {
			return String.format("%d:%02d:%02d.03%d", hours, minutes, seconds, ms);
		} else if (minutes > 0) {
			return String.format("%d:%02d.%03d", minutes, seconds, ms);
		} else {
			return String.format("%d.%03d", seconds, ms);
		}

	}

}
