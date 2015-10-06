package nl.tudelft.bejeweled.game;


import java.io.Serializable;

import javafx.scene.Group;
import nl.tudelft.bejeweled.board.Board;
import nl.tudelft.bejeweled.board.BoardFactory;
import nl.tudelft.bejeweled.logger.Logger;
import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Created by Pim on 6-10-2015.
 * The LevelManager manages the board and the score to realize progression through different levels.
 */
public class LevelManager implements Serializable {

	private Board board;
	
	private static final int BASE_TARGET_POINTS = 100;

	private BoardFactory boardFactory;
	private SpriteStore spriteStore;
	
	private int score;
	private int level;

	/**
     * Constructor for the level manager.
     * @param spriteStore Managing class for all the sprites in the game.
     * @param sceneNodes The JavaFX group container for the Jewel Nodes.
     */
	public LevelManager(SpriteStore spriteStore, Group sceneNodes) {
		this.spriteStore = spriteStore;
		boardFactory = new BoardFactory(spriteStore);
		setBoard(boardFactory.generateBoard(sceneNodes));
	}
	
	/**
	* Setup a new game in terms of level and score.
	*/
	public void newGame() {
		resetLevel();
		score = 0;
		level = 1;
	}

	/**
	 * Reset to a new level.
	 */
	private void resetLevel() {
		board.clearGrid();
		board.fillNullSpots();
	}

	
	/**
	 * Progress to a new level.
	 */
	private void newLevel() {
		board.implodeGrid();
	}

	/**
	 * Returns the current score.
	 * @return the current score.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Sets the current score.
	 * @param score the score to be set
	 */
	void setScore(int score) {
		this.score = score;
	}

	/**
	 * Updates the board and the level progression.
	 */
	public void update() {
		getBoard().update();

		int target = BASE_TARGET_POINTS * level * level;
		if (score >= target) {
			level++;
			newLevel();
			System.out.println("LEVEL UP!"); //TODO display this on screen
		}
	}

	/**
	 * Returns the current board.
	 * @return the current board.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Sets the current board.
	 * @param board the board to be set
	 */
	public void setBoard(Board board) {
		this.board = board;
	}


	/**
	 * Adds to the score the amount of points for one removed Jewel.
	 */
	//TODO implement more complex scoring
	public void addScore() {
		final int point = 10;
		score += point * level; // add 10 points per jewel removed

	}
}
