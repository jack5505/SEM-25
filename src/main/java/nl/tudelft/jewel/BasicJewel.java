package nl.tudelft.jewel;


import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nl.tudelft.bejeweled.sprite.JewelSprite;
import nl.tudelft.bejeweled.sprite.Sprite;

/**
 * Created by Jeroen on 5-9-2015.
 * Jewel class that holds all sprite information.
 */
public class BasicJewel implements Jewel {
    private final int type;
    private int boardX, boardY;
    private JewelSprite sprite;

    /**
     * Constructor for Jewel class.
     * @param type The type of Jewel created.
     * @param i The horizontal position of this Jewel on the board grid (in number of squares).
     * @param j The vertical position of this Jewel on the board grid (in number of squares).
     * @param x The horizontal position of this Jewel on the board grid (in pixels).
     * @param y The vertical position of this Jewel on the board grid (in pixels).
     */
    public BasicJewel(int type, int i, int j, int x, int y) {
        this.type = type;
        this.boardX = i;
        this.boardY = j;
        this.sprite = new JewelSprite(type, x, y);
    }

    /**
     * Updates the Jewel graphics.
     */
    @Override
    public void update() {
    	sprite.update();
    }   
    
    /**
     * Getter method for boardX.
     * @return The X position of the Jewel on the board's grid.
     */
    public int getBoardX() {
        return boardX;
    }

    /**
     * Getter method for boardY.
     * @return The Y position of the Jewel on the board's grid.
     */
    public int getBoardY() {
        return boardY;
    }

    /**
     * Getter method for Jewel type.
     * @return The type identifier for this Jewel.
     */
    public int getType() {
        return type;
    }

    /**
     * Setter method for boardX.
     * @param boardX Jewel's horizontal coordinate on the boards grid.
     */
    public void setBoardX(int boardX) {
        this.boardX = boardX;
    }

    /**
     * Setter method for boardY.
     * @param boardY Jewel's vertical coordinate on the boards grid.
     */
    public void setBoardY(int boardY) {
        this.boardY = boardY;
    }

    /**
     * Animate an implosion. Once done remove from the game world
     * @param sceneGroup Game scene group to remove the Jewel from.
     */
    public void implode(Group sceneGroup) {
        sprite.implode(sceneGroup);
    }

    /**
     * Simple version of implode, to remove the jewel from the game.
     * @param sceneGroup Game scene group to remove the Jewel from.
     */
    public void remove(Group sceneGroup) {
    	sprite.remove(sceneGroup);
    }
    
  /**
   * Animate a fade in for the Jewel.
   * @param sceneGroup the sceneGroup which displays the jewel fading in.
   */
    public void fadeIn(Group sceneGroup) {
    	getSprites().forEach((sprite) -> sprite.fadeIn(sceneGroup));
    }

    /**
     * Converts the position information to a string for logging.
     * 
     * @return String describing the position of the Jewel on the board
     */
    public String toString() {
    	return "Jewel @(" + boardX + ", " + boardY + ")";
	    }

    /**
     * Getter function for the sprite.
     * @return The current sprite
     */
	public JewelSprite getSprite() {
		return sprite;
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(getSprite().getNode());
		return nodes;
	}

	@Override
	public void relativeMoveTo(int i, int j) {
		sprite.relativeMoveTo(i, j);
		
	}

	@Override
	public List<JewelSprite> getSprites() {
		List<JewelSprite> sprites = new ArrayList<JewelSprite>();
		sprites.add(getSprite());
		return sprites;
	}
	
	@Override
	public boolean isHyper(){
		return false;
	}
	
	}

