package nl.tudelft.bejeweled.game;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import nl.tudelft.bejeweled.board.BoardObserver;
import nl.tudelft.bejeweled.logger.Logger;
import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Created by Pim on 6-10-2015.
 * The Session manages the board and the score to realize progression through different levels.
 */
public class Session implements Serializable, BoardObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1775959171332511193L;

	private Board board;
	
	private static final int BASE_TARGET_POINTS = 500;
	
	// Text parameters
	private static final int TEXT_SIZE = 55;
    private static final int TEXT_DURATION = 4000;
    private static final int TEXT_XPOS = 100;
    private static final int TEXT_YPOS = 200;
   
	private BoardFactory boardFactory;
	
	private int score;
	private int level;

	private transient Group sceneNodes;
	
    private transient List<SessionObserver> observers;

	/**
     * Constructor for the level manager.
     * @param spriteStore Managing class for all the sprites in the game.
     * @param sceneNodes The JavaFX group container for the Jewel Nodes.
     */
	public Session(SpriteStore spriteStore, Group sceneNodes) {
		this.sceneNodes = sceneNodes;
		observers = new ArrayList<>();

        
		boardFactory = new BoardFactory(spriteStore);
		setBoard(boardFactory.generateBoard(sceneNodes));
		 // start observing the board for callback events
        board.addObserver(this);
        setScore(0);
		setLevel(1);
    }

    /**
     * Adds an observer of the session.
     * @param observer BoardObserver to be added to the list of observers
     */
    public void addObserver(SessionObserver observer) {
    	if (observers == null) {
            observers = new ArrayList<>();
    	}
    	if (!observers.contains(observer)) {
    		observers.add(observer);
    	}
    }
    
    
    /**
     * Removes an observer of the session.
     * @param observer SessionObserver to be removed
     */
    public void removeObserver(SessionObserver observer) {
    	observers.remove(observer);
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
	private void setScore(int score) {
		this.score = score;
		for (SessionObserver observer : observers) {
            observer.updateScore();
      }
	}

	/**
	 * Updates the board and the level progression.
	 */
	public void update() {
		getBoard().update();

		int target = BASE_TARGET_POINTS * getLevel() * getLevel();
		if (score >= target) {
			setLevel(getLevel() + 1);
			newLevel();
			for (SessionObserver observer : observers) {
	            observer.updateLevel();
	      }
			displayText("LEVEL " + getLevel());
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
		setScore(getScore() + point * getLevel()); // add 10 points per jewel removed
		
	}
	
	
	/**
	 * Shows a label on top of the board for a number of seconds.
	 * 
	 * @param text String to be shown
	 */
	public void displayText(String text) {
        Logger.logInfo("Display: " + text);

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
	
    @Override
    public void boardOutOfMoves() {

    	displayText("Out of moves!");      
    	for (SessionObserver observer : observers) {
              observer.gameOver();
        }
    }

    @Override
    public void boardJewelRemoved() {
    	addScore();
    }
    
    public void lockBoard(){
    	board.setLocked(true);
    }
    
    public void unlockBoard(){
    	board.setLocked(false);
    }

	public int getLevel() {
		return level;
	}

	private void setLevel(int level) {
		this.level = level;
		for (SessionObserver observer : observers) {
            observer.updateLevel();
      }
	}
}
