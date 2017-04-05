package game;

import java.util.ArrayList;

import video.PositionedTextImage;

final class Peg extends Item implements video.Drawable {

	private ArrayList<Disk> disks = new ArrayList<Disk>();
	private static int lastID;

	public static void resetIdCounter() {
		lastID = -1;
	}

	public Peg(Game game) {
		id = lastID + 1;
		lastID++;
		this.game = game;

		image = new PositionedTextImage(1, game.disks.length * Disk.height + 2);
		image.assign(game.screen);
		image.fill('|');
		image.setChar(0, 0, (char) (id + 65));
		image.setOffset(0, image.getHeight() - 1);
		image.setXY(image.screen.getWidth() / (game.pegs.length * 2) * (id * 2 + 1), image.screen.getHeight() - 1);
	}

	public Peg addDiskBelow(Disk disk) {
		disks.add(0, disk);
		return this;
	}

	public Peg addDiskOnTop(Disk disk) {
		disks.add(disk);
		return this;
	}

	public void removeTopDisk() {
		disks.remove(disks.size() - 1);
	}

	public Disk getTopDisk() {
		if (disks.size() == 0) {
			return null;
		}
		return disks.get(disks.size() - 1);
	}

	public int getTopDiskID() {
		if (disks.size() > 0) {
			return disks.get(disks.size() - 1).getID();
		} else {
			return Integer.MAX_VALUE;
		}
	}

	private int getDiskLevel(Disk disk) {
		return disks.indexOf(disk);
	}

	public int getDiskY(Disk disk) {
		return image.getYint() - getDiskLevel(disk) * Disk.height;
	}

	public boolean hasAllDisks() {
		return disks.size() == game.disks.length;
	}

	public Disk getDiskAbove(Disk caller) {
		if ((this.getTopDisk() == caller) || (caller == null)) {
			return null;
		}

		for (int i = 0; i < disks.size(); i++) {
			if (disks.get(i) == caller) {
				return disks.get(i + 1);
			}
		}

		// We shouldn't get here at all.
		return null;
	}

	public Disk findLargestObstructor(Disk caller) {
		for (Disk disk : disks) {
			if (disk.id < caller.id) {
				return disk;
			}
		}
		return null;
	}

	public String toString() {
		return String.valueOf((char) (65 + id));
	}

}
