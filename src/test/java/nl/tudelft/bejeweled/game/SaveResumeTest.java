package nl.tudelft.bejeweled.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javafx.application.Platform;
import javafx.stage.Stage;
import nl.tudelft.bejeweled.CustomBoardLauncher;
import nl.tudelft.bejeweled.board.Board;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import java.io.File;

/**
 * Created by Amrit on 24-9-2015.
 * Test class for save resume testing.
 */
public class SaveResumeTest extends ApplicationTest {
    
    private CustomBoardLauncher launcher = new CustomBoardLauncher();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        launcher.launchGame(primaryStage);
    }
    
    /**
     * Quit the user interface when we're done.
     */
    @After
    public void tearDown() {
        closeCurrentWindow();
    }
    
    
    /**
     * Test of a user story where the user starts the game,
     * press save button and file is saved.
     */
    @Test
    public void testStartSave() {
        File boardFile = new File("board.mine");
        File scoreFile = new File("score.mine");
        if (boardFile.exists() || scoreFile.exists()) {
            boardFile.delete();
            scoreFile.delete();
        }
        BejeweledGame game = (BejeweledGame)launcher.getGame();
        assertFalse(game.isInProgress());
        
        clickOn("#buttonStart");
        clickOn("#buttonSave");
        
        assertTrue(boardFile.exists());
        assertTrue(scoreFile.exists());
        
        clickOn("#buttonExit");
    }   
    
    /**
     * Test of a user story where the user starts the game,
     * exit and game is saved.
     */
    @Test
    public void testStartExit() {
        File boardFile = new File("board.mine");
        File scoreFile = new File("score.mine");
        if (boardFile.exists() || scoreFile.exists()) {
            boardFile.delete();
            scoreFile.delete();
        }
        BejeweledGame game = (BejeweledGame)launcher.getGame();
        assertFalse(game.isInProgress());
        
        clickOn("#buttonStart");
        clickOn("#buttonExit");
        
        assertTrue(boardFile.exists());
        assertTrue(scoreFile.exists());
    }
    

    
    /**
     * Test of a user story where the user 
     * could resume the game when when saved
     * game state exists
     */
    @Test
    public void testResume() {
    	File boardFile = new File("board.mine");
        File scoreFile = new File("score.mine");
    	assertSame(boardFile.exists(),true);
        assertSame(scoreFile.exists(),true);
        
        BejeweledGame game = (BejeweledGame)launcher.getGame();
        assertFalse(game.isInProgress());
        
        clickOn("#buttonResume");
        
        assertTrue(game.isInProgress());
        clickOn("#buttonExit");
    }
    
    /**
     * Test of a user story where the user
     * cannot resume the old game after new game
     * has started, though saved game state exists.
     */
    @Test
    public void testNotResume() {
    	
    	File boardFile = new File("board.mine");
        File scoreFile = new File("score.mine");
    	assertTrue(boardFile.exists());
        assertTrue(scoreFile.exists());
    	
        BejeweledGame game = (BejeweledGame)launcher.getGame();
        assertFalse(game.isInProgress());
        
        clickOn("#buttonStart");
        clickOn("#buttonStop");
        clickOn("#buttonResume");
        
        assertFalse(boardFile.exists());
        assertFalse(scoreFile.exists());
    }
}