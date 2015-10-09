package nl.tudelft.bejeweled.game;

/**
 * Created by Pim on 9-10-2015.
 * Interface for the Session
 * */
public interface SessionObserver {

    /**
     * The session is over.
     */
    void gameOver();
    
    /**
     * Update the Score label.
     */
    void updateScore();
    
    /**
     * Update the Level label.
     */
    void updateLevel();
    
}