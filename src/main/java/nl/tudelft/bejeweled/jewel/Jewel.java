package nl.tudelft.bejeweled.jewel;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import nl.tudelft.bejeweled.sprite.JewelSprite;

/**
 * Created by Pim on 23-10-2015.
 * Interface for Jewels.
 */
public interface Jewel {
    /**
     * Updates the Jewel graphics.
     */
    void update();
       
   /**
    * Getter method for the sprite.
    * @return The current sprite.
    */
    JewelSprite getSprite();
    
    /**
     * Getter method for boardX.
     * @return The X position of the Jewel on the board's grid.
     */
    int getBoardX();
    /**
     * Getter method for boardY.
     * @return The Y position of the Jewel on the board's grid.
     */
    int getBoardY();

    /**
     * Getter method for Jewel type.
     * @return The type identifier for this Jewel.
     */
    int getType();

    /**
     * Setter method for boardX.
     * @param boardX Jewel's horizontal coordinate on the boards grid.
     */
    void setBoardX(int boardX);

    /**
     * Setter method for boardY.
     * @param boardY Jewel's vertical coordinate on the boards grid.
     */
    void setBoardY(int boardY);

    /**
     * Animate an implosion. Once done remove from the game world
     * @param sceneGroup Game scene group to remove the Jewel from.
     */
    void implode(Group sceneGroup);
    
    /**
     * Animate an explosion. Once done remove from the game world
     * @param sceneGroup Game scene group to remove the Jewel from.
     */
    void explode(Group sceneGroup);

    /**
     * Simple version of implode, to remove the jewel from the game.
     * @param sceneGroup Game scene group to remove the Jewel from.
     */
    void remove(Group sceneGroup);
    
  /**
   * Animate a fade in for the Jewel.
   * @param sceneGroup the sceneGroup which displays the jewel fading in.
   */
    void fadeIn(Group sceneGroup);

    /**
     * Converts the position information to a string for logging.
     * 
     * @return String describing the position of the Jewel on the board
     */
    String toString();
    
    /**
     * Getter method for the nodes associated with the sprites of this Jewel. 
     * @return the nodes associated with the Jewel.
     */
    List<Node> getNodes();

    /**
     * Move the jewel by a number of grid spots.
     * @param i Horizontal spots to move.
     * @param j Vertical spots to move.
     */
	void relativeMoveTo(int i, int j);

	/**
	 * Getter method for the sprites of this Jewel.
	 * @return the sprites of this Jewel.
	 */
	List<JewelSprite> getSprites();
	
	/**
	 * Test whether or not the Jewel is a Hyper Jewel.
	 * @return true if the jewel is Hyper.
	 */
	boolean isHyper();
	
	/**
	 * Test whether or not the Jewel is an Explosive Jewel.
	 * @return true if the jewel is Explosive.
	 */
	boolean isExplosive();
}
