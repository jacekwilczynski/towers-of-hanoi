package app;

import java.util.concurrent.TimeUnit;

import game.Game;
import game.Score;
import io.IO;
import io.TextFiles;
import io.exceptions.NumberOutOfRangeException;

/**
 *
 * @author Jacek
 */
public class App {

	private static Game game;
	private static StringBuilder message;
	private static boolean seenHelp = false;
	private static ScoreTable scores = new ScoreTable("data/scores.dat");

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
		showMenu();
	}

    /**
     *
     */
    public static void showMenu() {

		mainMenuLoop: while (true) {
			// If there was a game played since application launch:
			if (game != null) {
				switch (game.getConclusion().getStatus()) {
				case IN_PROGRESS:
					// do nothing
					break;
				case EXIT:
					// get out of the main loop
					break mainMenuLoop;
				case AUTO_SOLVED:
					// just forget the game
					game = null;
					break;
				case SUCCESS:
					Score score = game.getConclusion().getScore();
					game = null;
					int place = scores.getPlace(score);
					message = new StringBuilder();
					message.append("Congratulations!\n");
					message.append("You have succeeded at moving ").append(score.getNumberOfDisks())
							.append(" disks in ").append(score.getNumberOfMoves()).append(" moves,\nwhich took you ")
							.append(IO.msToNiceString(score.getTime())).append(", earning you ")
							.append(score.getPointsString()).append(" points.\n");
					System.out.println(message.toString());
					if (place > 0) {
						System.out.printf("This puts you at the %s position in our hall of fame!\n",
								IO.getOrdinal(place));
						System.out.printf("Enter your name: ");
						String name = IO.in.nextLine();
						if (name.length() > 20) {
							name = name.substring(0, 20);
						}
						score.setName(name);
						scores.addScore(score);
						showScores();
					} else {
						System.out.printf("Unfortunately, this is not enough to make it into the high scores.\n");
					}

					break;
				}
			}

			// Get user input and launch appropriate sub-routines
			message = new StringBuilder();
			message.append("\n\n\nWelcome to TOWERS OF HANOI by Jack Wolf!\n\n").append("Choose action:\n")
					// .append(game != null ? " 0) " : " ")
					// .append("Return to the current game")
					// .append(game != null ? "\n" : " (unavailable)\n")
					.append(game != null ? " 0) Return to current game\n" : "").append(" 1) Start a new game\n")
					.append(" 2) Display high scores\n").append(" 3) Show help\n").append(" 4) Quit\n");
			System.out.println(message.toString());

			int choice;
			while (true) {
				try {
					choice = IO.getInt(0, 4);
					System.out.println();
					switch (choice) {
					case 0:
						if (game == null) {
							throw new NumberOutOfRangeException();
						} else {
							game.play();
						}
						break;
					case 1:
						if (!seenHelp) {
							System.out.println("To get back to this menu once the game has started, type 'menu'.");
						}
						game = getGame();
						game.play();
						break;
					case 2:
						showScores();
						break;
					case 3:
						showHelp();
						break;
					case 4:
						break mainMenuLoop;
					}
					break;
				} catch (NumberFormatException | NumberOutOfRangeException e) {
					System.out.println("Bad input. Try again.");
				}
			}
		}

		System.out.println("See you next time!");

	}

    /**
     *
     */
    public static void showHelp() {
		seenHelp = true;
		TextFiles.print("data/help.txt");
	}

    /**
     *
     */
    public static void showScores() {
		scores.show();
	}

    /**
     *
     * @return
     */
    public static Game getGame() {
		int pegs = Game.DEFAULT_NUMBER_OF_PEGS;
		message = new StringBuilder()
				.append(String.format("How many disks would you like to play with (%d - %d, default: %d)?\n",
						Game.MIN_DISKS, Game.MAX_DISKS, Game.DEFAULT_NUMBER_OF_DISKS));
		System.out.printf(message.toString());
		int disks;
		try {
			disks = IO.getInt(Game.MIN_DISKS, Game.MAX_DISKS);
		} catch (NumberFormatException e) {
			disks = Game.DEFAULT_NUMBER_OF_DISKS;
			message = new StringBuilder();
			message.append(String.format("Hmm... that doesn't seem like an integer between %d and %d.\n",
					Game.MIN_DISKS, Game.MAX_DISKS, disks));
			message.append(String.format("Let's go with the standard number of %d.\n\n", disks));
			System.out.println(message.toString());
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (NumberOutOfRangeException e) {
			disks = e.getSuggestedValue();
			message = new StringBuilder();
			message.append("Number out of range. The closest we can get is ").append(disks).append(".");
			System.out.println(message.toString());
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return new Game(pegs, disks);
	}

}
