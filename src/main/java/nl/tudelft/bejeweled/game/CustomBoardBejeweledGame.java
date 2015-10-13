package nl.tudelft.bejeweled.game;

import javafx.scene.Group;
import javafx.scene.Scene;
import nl.tudelft.bejeweled.Launcher;
import nl.tudelft.bejeweled.logger.Logger;
import nl.tudelft.bejeweled.sprite.SpriteStore;
/**
 * Created by Pim on 13-10-2015.
 * Extension of the BejeweledGame class to load custom boards from text.
 * Primarily used for testing purposes.
 */
public class CustomBoardBejeweledGame extends BejeweledGame {
	
	private String boardLocation;

	/**
	 * Constructor for a Bejeweld game with a custom board
     * @param framesPerSecond - The number of frames per second the game will attempt to render.
     * @param windowTitle - The title displayed in the window.
     * @param spriteStore - The spriteStore.
     * @param boardLocation The location of the boardfile to be used 
     * to create the board at the start of this session.
	 */
	public CustomBoardBejeweledGame(int framesPerSecond, String windowTitle, SpriteStore spriteStore, String boardLocation) {
		super(framesPerSecond, windowTitle, spriteStore);
		this.boardLocation = boardLocation;
		}
	
	 /**
     *  Starts the game.
     */
    @Override
    public void start() {
    	Logger.logInfo("Game started");
    	//Clean up existing sprites
    	gamePane.getChildren().remove(sceneNodes);
        spriteStore.removeAllSprites();    
        
        sceneNodes = new Group();
        session = new Session(spriteStore, sceneNodes, boardLocation);
        session.addObserver(this);
        updateLevel();
        updateScore();
        
        // Create the group that holds all the jewel nodes and create a game scene
        gamePane.getChildren().add(new Scene(getSceneNodes(),
                gamePane.getWidth(),
                gamePane.getHeight()).getRoot());

    
        removeSaveGame();
    }
	


}
