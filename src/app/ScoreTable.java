package app;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import game.Score;
import io.IO;

class ScoreTable {

	public int maxScores = 10;
	private ArrayList<Score> scores;
	private File file;

	public ScoreTable(File file) {
		this.file = file;
		loadScores();
	}

	public ScoreTable(String fileName) {
		// this(new File(file));
		this.file = new File(fileName);
		loadScores();
	}

	public void show() {
		if (scores.isEmpty()) {
			System.out.println("No scores to show yet.");
			return;
		}
		scores.sort(null);

		System.out.println("/=============================================================================\\");
		System.out.println("|                   TOWER OF HANOI     ---     HALL OF FAME                   |");
		System.out.println("|=============================================================================|");
		System.out.printf("|%-4s %-20s %6s %6s %11s %20s     |\n", " ", "NAME", "DISKS", "MOVES", "TIME", "POINTS");
		System.out.println("|-----------------------------------------------------------------------------|");
		Score score;
		for (int i = 0; i < getNumberOfScores(); i++) {
			score = scores.get(i);
			System.out.printf("|%3s) %-20s %6s %6s %11s %24s |\n", i + 1, score.getName(), score.getNumberOfDisks(),
					score.getNumberOfMoves(), IO.msToNiceStringNoUnits(score.getTime()), score.getPointsString());
		}
		System.out.println("\\-----------------------------------------------------------------------------/");
	}

	public int getPlace(Score score) {
		// Returns the place or 0 if didn't make it into the table
		for (int i = 0; i < getNumberOfScores(); i++) {
			if (scores.get(i).getPoints() < score.getPoints()) {
				return i + 1;
			}
		}
		if (getNumberOfScores() < maxScores) {
			return getNumberOfScores() + 1;
		}
		return 0;
	}

	public void addScore(Score score) {
		scores.sort(null);
		int place = getPlace(score);
		if (place > 0) {
			scores.add(place - 1, score);
			saveScores();
		}
	}

	private int getNumberOfScores() {
		return Math.min(scores.size(), maxScores);
	}

	private void loadScores() {
		scores = new ArrayList<>();
		String fileName = file.toString();

		try (FileInputStream fiStream = new FileInputStream(file)) {
			try (ObjectInputStream oiStream = new ObjectInputStream(fiStream)) {
				Score score;
				while ((score = (Score) oiStream.readObject()) != null) {
					score.calculatePoints();
					scores.add(score);
				}
			} catch (EOFException e) {
			} catch (ClassNotFoundException | IOException e) {
				fiStream.close();
				String backupFileName = IO.backupFile(fileName);
				if (!IO.getIOException()) {
					System.out.printf(
							"Warning! High scores file \"%s\" is corrupt or incompatible with the current\n"
									+ "version of Towers Of Hanoi. The old file has been renamed to \"%s\",\n"
									+ "and a new empty file has been created in its place.\n",
							fileName, backupFileName);
					loadScores();
				} else {
					System.out.printf(
							"Error! Cannot read from scores file \"%s\".\n" + "Unable to back up the file as \"%s\".\n",
							fileName, backupFileName);
					System.out.printf("Type 'delete' if you want to proceed. THIS WILL DELETE THE HIGH SCORES.\n> ");
					if (IO.in.nextLine().equalsIgnoreCase("delete")) {
						file.delete();
						if (!file.delete()) {
							System.out.printf("Error! Could not delete file \"%s\".", fileName);
						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			try {
				file.createNewFile();
				loadScores();
			} catch (IOException e1) {
				System.out.printf(
						"Error! High scores file \"%s\" not found and cannot create a new one with this name.\n",
						fileName);
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

	private void saveScores() {
		try (ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file))) {
			for (int i = 0; i < getNumberOfScores(); i++) {
				ooStream.writeObject(scores.get(i));
			}
		} catch (FileNotFoundException e) {
			try {
				file.createNewFile();
				saveScores();
			} catch (IOException e1) {
				System.out.printf("Error! High scores file \"%s\" not found and cannot create a new one with this name.\n",
						file.toString());
			}
		} catch (IOException e) {
			System.out.printf("Error! High scores file \"%s\" not found and cannot create a new one with this name.\n",
					file.toString());
		}
	}

}
