package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Jacek
 */
public abstract class TextFiles {

    /**
     *
     * @param fileName
     */
    public static void print(String fileName) {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			System.out.printf("File '%s' not found!", fileName);
		} catch (IOException e) {
			System.out.printf("Could not close file '%s'.", fileName);
		}
			
	}
	
//	public static void print(String fileName) {
//		try (Scanner scanner = new Scanner(new File(fileName))) {
//			while (scanner.hasNextLine()) {
//				System.out.println(scanner.nextLine());
//			}
//		} catch (FileNotFoundException e) {
//			System.out.printf("File '%s' not found!", fileName);
//		}
//	}
	
}
