package nl.tudelft.bejeweled.game;

import java.util.Optional;

import javax.xml.bind.JAXBException;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import nl.tudelft.bejeweled.board.Board;
import nl.tudelft.bejeweled.board.BoardFactory;
import nl.tudelft.bejeweled.board.BoardObserver;
import nl.tudelft.bejeweled.logger.Logger;
import nl.tudelft.bejeweled.sprite.SpriteStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.File;


/**
 * Created by Jeroen on 6-9-2015.
 * Bejeweled Game class.
 */
public class BejeweledGame extends Game implements BoardObserver, Serializable {
	public static final int GRID_WIDTH = 8;
    public static final int GRID_HEIGHT = 8;
    public static final int SPRITE_WIDTH = 64;
    public static final int SPRITE_HEIGHT = 64;

    private static final int GAME_OVER_SIZE = 55;
    private static final int GAME_OVER_DURATION = 3000;
    private static final int GAME_OVER_XPOS = 100;
    private static final int GAME_OVER_YPOS = 200;

    private static final String SAVE_FILE = "save.mine";


    /** The board class that maintains the jewels. */
    private LevelManager levelManager;

    private boolean isStop = false;
    
    private boolean isResume = true;
    
	private SpriteStore spriteStore;
	
    /**
     * <code>true</code> if the game is in progress.
     */
    private boolean inProgress;

    private Pane gamePane;

    private Label scoreLabel;


    /**
     * The constructor for the bejeweled game.
     * @param framesPerSecond - The number of frames per second the game will attempt to render.
     * @param windowTitle - The title displayed in the window.
     * @param spriteStore - The spriteStore.
    */
    public BejeweledGame(int framesPerSecond, String windowTitle, SpriteStore spriteStore) {
        super(framesPerSecond, windowTitle);
        this.spriteStore = spriteStore;
        try {
        	setHighScore(new HighScore());
        
        	getHighScore().loadHighScores();
        }
        catch (JAXBException ex) {
        	ex.printStackTrace();
        	Logger.logError("HighScore system encountered an error");
        }
    }

    /**
     *  Starts the game.
     */
    @Override
    public void start() {
        if (inProgress) {
            return;
        }
        inProgress = true;
        Logger.logInfo("Game started");
        
        levelManager.newGame();
        scoreLabel.setText(Integer.toString(levelManager.getScore())); 

        // Create the group that holds all the jewel nodes and create a game scene
        gamePane.getChildren().add(new Scene(getSceneNodes(),
                gamePane.getWidth(),
                gamePane.getHeight()).getRoot());

    
        isResume = false;
        removeSaveGame();
    }

    /**
     *  Stops the game.
     */
    @Override
    public void stop() {
        if (!inProgress) {
            return;
        }

        Logger.logInfo("Final score: " + levelManager.getScore());
        int place = getHighScore().isHighScore(levelManager.getScore());
        if (place >  0) {
        	Optional<String> result = showTextInputDialog("Enter your name",
                    "Congratulations, you achieved a highscore." + " Please enter your name:");
			result.ifPresent(name -> {
				try {
                    getHighScore().addHighScore(levelManager.getScore(), name);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
        }
        Logger.logInfo("Game stopped");

        gamePane.getChildren().remove(getSceneNodes());
        spriteStore.removeAllSprites();
        
        inProgress = false;
        isStop = true;
        isResume = false;

        removeSaveGame();
    }

    /**
     * Initialise the game world by update the JavaFX Stage.
     * @param gamePane The primary scene.
     */
    @Override
    public void initialise(Pane gamePane, Label scoreLabel) {
        this.gamePane = gamePane;
        this.scoreLabel = scoreLabel;
        
        levelManager = new LevelManager(spriteStore, sceneNodes);

        // set initial score
        scoreLabel.setText(Integer.toString(levelManager.getScore()));

        // draw and stretch the board background
        gamePane.setStyle("-fx-background-image: url('/board.png'); -fx-background-size: cover;");

        // start observing the board for callback events
        levelManager.getBoard().addObserver(this);

        // set callback for clicking on the board
        gamePane.addEventFilter(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        if (isInProgress() && event.getTarget() instanceof Pane) {
                        	levelManager.getBoard().boardClicked(true);
                        }
                    }
                }
        );
    }
    
    /**
     * Updates all game logic, At this point this consists of updating 
     * the board that is loaded for the game if the game is in progress.
     */
    protected void updateLogic() {
        if (inProgress) {
        	levelManager.update();
        }
    }
    
    @Override
    public void boardOutOfMoves() {

        final Label label = new Label("Game Over");

        label.setFont(new Font("Arial", GAME_OVER_SIZE));

        // position the label
        //TODO Position this label nicely in the center
        label.setLayoutX(GAME_OVER_XPOS);
        label.setLayoutY(GAME_OVER_YPOS);
        gamePane.getChildren().add(label);

        FadeTransition ft = new FadeTransition(Duration.millis(GAME_OVER_DURATION), label);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                gamePane.getChildren().remove(label);
            }
        });
        ft.play();
        
        Logger.logInfo("Out of moves!");

    	stop();
    }

    @Override
    public void boardJewelRemoved() {
    	levelManager.addScore();
        scoreLabel.setText(Integer.toString(levelManager.getScore()));
    }
    
    @Override
    public void showHint() {
    	if (levelManager.getBoard() != null) {
    		levelManager.getBoard().showHint();
    	}
    }
    
    
    @SuppressWarnings("restriction")
	@Override
    public void save() {      
        if (!inProgress || isStop) {
            return;     
        }
        inProgress = false;
        try {
        	//Before writing, convert the board to a serializable state
        	levelManager.getBoard().setState(levelManager.getBoard().convertGrid());
        	
            OutputStream file = new FileOutputStream(SAVE_FILE);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(levelManager);
            output.flush();
            output.close();
       
        } catch (IOException e) {
            e.printStackTrace();
        }      
        gamePane.getChildren().remove(getSceneNodes());
        spriteStore.removeAllSprites();
        levelManager.getBoard().resetGrid();
        Logger.logInfo("Game saved");
        inProgress = false; isStop = true; isResume = true;           
    }
    
    @Override
    public void resume() {
        levelManager.getBoard().clearGrid();
    	File saveFile = new File(SAVE_FILE);
        if (inProgress || !isResume || !saveFile.exists() ) {
            return;
        }
        InputStream file;       
       try {
           file = new FileInputStream(SAVE_FILE);
           InputStream buffer = new BufferedInputStream(file);
           ObjectInput input = new ObjectInputStream(buffer);
           levelManager = (LevelManager) input.readObject();
           input.close();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       }
       //Restore the grid from its serialized form
       levelManager.getBoard().makeGrid(getSceneNodes()); 
       levelManager.getBoard().addObserver(this);

      inProgress = true; isResume = false; isStop = false; saveFile.delete(); 
        Logger.logInfo("Game resumed");       
       scoreLabel.setText(Integer.toString(levelManager.getScore()));
       gamePane.getChildren().add(new Scene(getSceneNodes(), gamePane.getWidth(), 
                                           gamePane.getHeight()).getRoot());
    }

    /**
     * Shows a text input dialog.
     * @param title Title of the dialog.
     * @param text Context text of the dialog.
     * @return The input by the user.
     */
    private Optional<String> showTextInputDialog(String title, String text) {
        Dialog<String> dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(text);

        return dialog.showAndWait();
    }

    /**
     * Removes the save game if it exists.
     */
    @Override
    public void removeSaveGame() {
        File saveFile = new File(SAVE_FILE);
        if (saveFile.exists() && saveFile.delete()) {
            Logger.logInfo("Save files deleted.");
        }
    }

    /**
     *Check if the game is still in progress.
     * @return true if the game is in progress.
     */
    public boolean isInProgress() {
        return inProgress;
    }
    
    public LevelManager getLevelManager() {
		return levelManager;
	}

	public void setLevelManager(LevelManager levelManager) {
		this.levelManager = levelManager;
	}
}
