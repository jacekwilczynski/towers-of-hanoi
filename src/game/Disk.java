package game;

import game.exceptions.SmallerDiskOnTargetPegException;
import io.IO;
import video.PositionedTextImage;

class Disk extends Item implements video.Drawable {

	public static int height = 3;
	public static int initialWidth = 7;
	public static int widthMultiplier = 4;

	static int calculateWidth(int id) {
		return initialWidth + id * widthMultiplier;
	}

	private static int lastID;
	private Peg peg;
	private static boolean breakSolving = false;
	private static boolean solveMoveWithoutAsking = false;

	public static void resetSolveSettings() {
		breakSolving = false;
		solveMoveWithoutAsking = false;
	}

	public static void resetIdCounter() {
		lastID = -1;
	}

	public Disk(Game game) {
		this.game = game;
		id = lastID + 1;
		lastID++;

		image = new PositionedTextImage(calculateWidth(id), height);
		image.assign(game.screen);
		if (height <= 1) {
			image.hLine(0, '=');
			image.setChar(0, 0, '<');
			image.setChar(image.getWidth() - 1, 0, '>');
		} else {
			image.hLine(0, '-');
			image.hLine(image.getHeight() - 1, '-');
			if (height > 2) {
				image.vLine(0, '|');
				image.vLine(image.getWidth() - 1, '|');
			}
			image.setChar(0, 0, '/');
			image.setChar(image.getWidth() - 1, 0, '\\');
			image.setChar(0, image.getHeight() - 1, '\\');
			image.setChar(image.getWidth() - 1, image.getHeight() - 1, '/');
		}
		if (height != 2) {
			image.putText(image.getWidth() / 2, image.getHeight() / 2, String.valueOf(id + 1));
		} else {
			String strId = String.valueOf(id + 1);
			image.putText(image.getWidth() / 2, 0, strId);
			image.putText(image.getWidth() / 2, 1, strId);
		}
		image.setOffset(image.getWidth() / 2, image.getHeight() - 1);

		peg = game.pegs[0].addDiskBelow(this);
	}

	public void draw() {
		image.setXY(peg.getX(), peg.getDiskY(this));
		image.draw();
	}

	public void move(Peg targetPeg) throws SmallerDiskOnTargetPegException {
		if (targetPeg.getTopDiskID() < this.id)
			throw new SmallerDiskOnTargetPegException();
		peg.removeTopDisk();
		this.peg = targetPeg;
		peg.addDiskOnTop(this);
		game.conclusion.score.nextMove();
	}

	public Peg findSparePeg(Peg exclusion1, Peg exclusion2) {
		for (Peg peg : game.pegs) {
			if ((peg != exclusion1) && (peg != exclusion2)) {
				return peg;
			}
		}
		return null;
	}

	public void solveMove(Peg targetPeg) {
		if (breakSolving) {
			return;
		}

		if (peg.getTopDisk() != this) {
			peg.getDiskAbove(this).solveMove(findSparePeg(this.peg, targetPeg));
		}

		if (breakSolving) {
			return;
		}

		try {
			if (!solveMoveWithoutAsking && (targetPeg.getTopDiskID() > this.id)) {
				game.notificationArea.out.printf(
						"AUTO-SOLVER: About to move disk %s from %s to %s. Press enter to confirm. Type 'break' to discontinue or 'complete' to follow through all the remaining steps.",
						this, peg, targetPeg);
				game.refresh();
				System.out.print("> ");
				String input = IO.in.nextLine();
				if (input.equalsIgnoreCase("break")) {
					breakSolving = true;
					return;
				} else if (input.equalsIgnoreCase("complete")) {
					solveMoveWithoutAsking = true;
				}
			}
			this.move(targetPeg);
		} catch (SmallerDiskOnTargetPegException e) {
			if (breakSolving) {
				return;
			}
			targetPeg.findLargestObstructor(this).solveMove(findSparePeg(targetPeg, this.peg));
			if (breakSolving) {
				return;
			}
			this.solveMove(targetPeg);
		}

	}

	public Peg getPeg() {
		return peg;
	}

	public int getID() {
		return id;
	}

	public String toString() {
		return String.valueOf(id + 1);
	}

	public boolean isOnTop() {
		return peg.getTopDisk() == this;
	}

}
