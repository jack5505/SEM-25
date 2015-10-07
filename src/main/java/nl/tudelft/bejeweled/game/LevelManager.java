package nl.tudelft.bejeweled.game;


import java.io.Serializable;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
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
	
	// Text parameters
	private static final int TEXT_SIZE = 55;
    private static final int TEXT_DURATION = 4000;
    private static final int TEXT_XPOS = 100;
    private static final int TEXT_YPOS = 200;

	private BoardFactory boardFactory;
	private SpriteStore spriteStore;
	
	private int score;
	private int level;

	transient Label levelLabel;
	transient Group sceneNodes;

	/**
     * Constructor for the level manager.
     * @param spriteStore Managing class for all the sprites in the game.
     * @param sceneNodes The JavaFX group container for the Jewel Nodes.
	 * @param levelLabel 
     */
	public LevelManager(SpriteStore spriteStore, Group sceneNodes, Label levelLabel) {
		this.spriteStore = spriteStore;
		this.sceneNodes = sceneNodes;
		this.levelLabel = levelLabel;
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
		levelLabel.setText(Integer.toString(level));
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
			levelLabel.setText(Integer.toString(level));
			displayText("LEVEL "+level);
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
	 * Set the sceneNodes which is needed to display Text.
	 * @param sceneNodes Existing sceneNodes
	 */
	public void setSceneNodes(Group sceneNodes) {
		this.sceneNodes = sceneNodes;
	}

	/**
	 * Adds to the score the amount of points for one removed Jewel.
	 */
	//TODO implement more complex scoring
	public void addScore() {
		final int point = 10;
		score += point * level; // add 10 points per jewel removed

	}
	
	
	/**
	 * Sets the levelLabel.
	 * @param levelLabel Label to display the level in the window
	 */
	public void setLevelLabel(Label levelLabel) {
		this.levelLabel = levelLabel;
		levelLabel.setText(Integer.toString(level));
	}
	
	
	/**
	 * Shows a label on top of the board for a number of seconds.
	 * 
	 * @param text String to be shown
	 */
	public void displayText(String text) {
		final Label label = new Label(text);

        label.setFont(new Font("Arial", TEXT_SIZE));
        label.setTextFill(Color.WHITE);

        // position the label
        label.setLayoutX(TEXT_XPOS);
        label.setLayoutY(TEXT_YPOS);
        sceneNodes.getChildren().add(label);

        FadeTransition ft = new FadeTransition(Duration.millis(TEXT_DURATION), label);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                sceneNodes.getChildren().remove(label);
            }
        });
        ft.play();
	}
}
