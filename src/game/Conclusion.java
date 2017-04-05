package game;

/**
 *
 * @author Jacek
 */
public class Conclusion {

	Score score;
	private GameStatus status;
	
    /**
     *
     * @param game
     */
    public Conclusion(Game game) {
		score = new Score(game.disks.length);
		status = GameStatus.IN_PROGRESS;
	}
	
    /**
     *
     * @return
     */
    public Score getScore() {
		return score;
	}
	
    /**
     *
     * @return
     */
    public GameStatus getStatus() {
		return status;
	}
	
    /**
     *
     * @param status
     */
    public void conclude(GameStatus status) {
		this.status = status;
		score.calculatePoints();
	}
	
}
