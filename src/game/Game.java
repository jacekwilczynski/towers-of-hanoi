package game;

import java.util.ArrayList;

import game.exceptions.SmallerDiskOnTargetPegException;
import game.exceptions.SourcePegEmptyException;
import io.IO;
import video.Drawable;
import video.NotificationArea;
import video.TextModeScreen;

/**
 *
 * @author Jacek
 */
public final class Game {

    /**
     *
     */
    public static final int MIN_DISKS = 1;

    /**
     *
     */
    public static final int MAX_DISKS = 23;

    /**
     *
     */
    public static final int MIN_PEGS = 2;

    /**
     *
     */
    public static final int MAX_PEGS = 10;

    /**
     *
     */
    public static final int DEFAULT_NUMBER_OF_DISKS = 6;

    /**
     *
     */
    public static final int DEFAULT_NUMBER_OF_PEGS = 3;

    /**
     *
     */
    public static final int MIN_SCREEN_WIDTH = 64;

    /**
     *
     */
    public static final int STATUS_BAR_HEIGHT = 2;

    /**
     *
     */
    public static final int NOTIFICATION_AREA_HEIGHT = 4;
	private static final String SEPARATOR = "-";

	Peg[] pegs;
	Disk[] disks;
	TextModeScreen screen;
	ArrayList<Drawable> allItems = new ArrayList<Drawable>();
	NotificationArea statusBar, notificationArea;
	Conclusion conclusion;
	boolean usedAutoSolve;
	int minimumNumberOfMoves;

    /**
     *
     * @param pegs
     * @param disks
     */
    public Game(int pegs, int disks) {
		Peg.resetIdCounter();
		Disk.resetIdCounter();
		this.pegs = new Peg[pegs];
		this.disks = new Disk[disks];
		usedAutoSolve = false;
		if (disks <= 5) {
			Disk.height = 3;
			if (disks <= 4) {
				Disk.initialWidth = 7;
			} else
				Disk.initialWidth = 5;
			Disk.widthMultiplier = 4;
//		} else if (disks <= 10) {
//			Disk.height = 2;
//			Disk.initialWidth = 5;
//			Disk.widthMultiplier = 2;
		} else {
			Disk.height = 1;
			Disk.initialWidth = 3;
			if (disks <= 11) {
				Disk.widthMultiplier = 2;
			} else {
				Disk.widthMultiplier = 1;
			}
		}

		// Create screen
		int w = pegs * (Disk.calculateWidth(disks));
		this.screen = new TextModeScreen(w > MIN_SCREEN_WIDTH ? w : MIN_SCREEN_WIDTH,
				Disk.height * disks + STATUS_BAR_HEIGHT + NOTIFICATION_AREA_HEIGHT + 5);

		// Create pegs
		for (int i = 0; i < pegs; i++) {
			allItems.add(this.pegs[i] = new Peg(this));
		}

		// Create disks
		for (int i = 0; i < disks; i++) {
			allItems.add(this.disks[i] = new Disk(this));
		}

		// Set up status bar
		statusBar = new NotificationArea(screen.getWidth() - 4, STATUS_BAR_HEIGHT);
		statusBar.assign(screen);
		statusBar.setXY(2, 1);
		allItems.add(statusBar);

		// Set up notification area
		notificationArea = new NotificationArea(screen.getWidth() - 4, NOTIFICATION_AREA_HEIGHT);
		notificationArea.assign(screen);
		notificationArea.setXY(2, statusBar.getY() + STATUS_BAR_HEIGHT + 1);
		allItems.add(notificationArea);

		// Create conclusion
		conclusion = new Conclusion(this);

		// Store the minimum number of moves for future reference
		minimumNumberOfMoves = conclusion.score.getMinimumNumberOfMoves();

	}

    /**
     *
     */
    public void refresh() {
		statusBar.out.printf("Moves: %-7d Optimal number of moves: %-7d\nTime: %s", conclusion.score.getNumberOfMoves(),
				minimumNumberOfMoves, IO.msToNiceString(conclusion.score.updateTime()));

		drawAll();
		screen.refresh();
		screen.clear();
	}

    /**
     *
     */
    public void play() {
		String input;

		do {
			refresh();

			if (isFinished()) {
				if (!usedAutoSolve) {
					conclusion.conclude(GameStatus.SUCCESS);
				} else {
					conclusion.conclude(GameStatus.AUTO_SOLVED);
				}
				break;
			}

			System.out.print("> ");
			input = IO.in.nextLine().toUpperCase();

			if (input.equals("EXIT")) {
				conclusion.conclude(GameStatus.EXIT);
				break;
			} else if (input.equals("MENU")) {
				break;
			} else {
				String source, target = new String();
				boolean solve = false;

				if (input.equals("SOLVE ALL")) {
					for (int i = disks.length - 1; i >= 0; i--) {
						disks[i].solveMove(pegs[pegs.length - 1]);
					}
					usedAutoSolve = true;
					Disk.resetSolveSettings();
					notificationArea.out.printf("Auto-solving ended.");
					continue;
				} else if ((input.length() >= 5) && (input.substring(0, 5).equals("SOLVE"))) {
					input = input.substring(6);
					solve = true;
				}

				int separatorPosition = input.indexOf(SEPARATOR);

				if (input.length() == 2) {
					source = input.substring(0, 1);
					target = input.substring(1);
				} else if ((input.length() > 2) && (separatorPosition > 0)
						&& (separatorPosition < input.length() - 1)) {
					source = input.substring(0, separatorPosition);
					target = input.substring(separatorPosition + 1, input.length());
				} else {
					notificationArea.out.printf("Bad input.");
					continue;
				}

				Peg sourcePeg = null;
				Disk sourceDisk = null;
				Peg targetPeg = null;
				Disk targetDisk = null;

				try {
					sourcePeg = pegs[Integer.parseInt(String.valueOf(source.charAt(0) - 65))];
				} catch (Exception e) {
					try {
						sourceDisk = disks[Integer.parseInt(source) - 1];
					} catch (Exception e2) {
						notificationArea.out.printf("%s is neither a peg nor a disk.", source);
						continue;
					}
				}

				try {
					targetPeg = pegs[Integer.parseInt(String.valueOf(target.charAt(0) - 65))];
				} catch (Exception e) {
					try {
						targetDisk = disks[Integer.parseInt(target) - 1];
					} catch (Exception e2) {
						notificationArea.out.printf("%s is neither a peg nor a disk.", target);
						continue;
					}
				}

				try {
					if (sourcePeg != null) {
						sourceDisk = sourcePeg.getTopDisk();
					}
					if (sourceDisk == null) {
						throw new SourcePegEmptyException();
					}
					if (targetDisk != null) {
						targetPeg = targetDisk.getPeg();
					}
					if (targetPeg == null) {
						// This should never happen!
						System.out.printf("Error! The program cannot access the target peg! Please kick the author's ass.");
					}
					
					if (sourceDisk.getPeg() == targetPeg) {
						notificationArea.out.printf("Nothing moved.");
						continue;
					}
					
					if (solve) {
						usedAutoSolve = true;
						sourceDisk.solveMove(targetPeg);
						Disk.resetSolveSettings();
						notificationArea.out.printf("Auto-solving ended.");
					} else {
						if (sourceDisk.isOnTop()) {
							sourceDisk.move(targetPeg);
							notificationArea.out.printf("Moved disk %s onto peg %s.", sourceDisk, targetPeg);
						} else {
							notificationArea.out.printf(
									"Disk %s can't be picked up at the moment because it's not at the top.",
									sourceDisk);
						}
					}
				} catch (SourcePegEmptyException e) {
					notificationArea.out.printf("There is no disk on peg %s.", source);
					continue;
				} catch (SmallerDiskOnTargetPegException e) {
					notificationArea.out.printf("Can't move a bigger disk onto a smaller disk.", source);
					continue;
				}
			}

		} while (true);
	}

    /**
     *
     */
    public void drawAll() {
		for (Drawable item : allItems) {
			item.draw();
		}
	}

	private boolean isFinished() {
		return pegs[pegs.length - 1].hasAllDisks();
	}

    /**
     *
     * @return
     */
    public Conclusion getConclusion() {
		return conclusion;
	}

}
