package nl.tudelft.bejeweled.sprite;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.FadeTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * Created by Jeroen on 5-9-2015.
 * Jewel class that holds all sprite information.
 */
public class Jewel extends Sprite {
	public static final double MAX_SPEED_X = 4;
	public static final double MAX_SPEED_Y = 4;
	public static final int ANIMATION_DURATION = 300;
	
    private final int type;
    private int boardX, boardY;
    private Image jewelImage;

    /**
     * Constructor for Jewel class.
     * @param type The type of Jewel created.
     * @param boardX The X position of this Jewel on the board grid (in number of squares).
     * @param boardY The Y position of this Jewel on the board grid (in number of squares).
     */
    public Jewel(int type, int boardX, int boardY) {
        this.type = type;
        this.boardX = boardX;
        this.boardY = boardY;

        String imagePath = "/" + Integer.toString(type) + ".png";
        jewelImage = new Image(Jewel.class.getResourceAsStream(imagePath));
        ImageView jewelImageView = new ImageView();
        jewelImageView.setImage(jewelImage);
        jewelImageView.setStyle("-fx-background-color:transparent;");

        setNode(jewelImageView);
    }

    /**
     * Updates the Jewel graphics.
     */
    @Override
    public void update() {
    	updateVelocity();
        getNode().setTranslateX(getNode().getTranslateX() - getvX());
        getNode().setTranslateY(getNode().getTranslateY() - getvY());
        getNode().setLayoutX(getxPos());
        getNode().setLayoutY(getyPos());
    }
    
    /**
     * Updates the Jewels velocity based on its current position and desired position.
     */
    private void updateVelocity() {
    	//Update x velocity
    	if (getNode().getTranslateX() > MAX_SPEED_X) {
    		setvX(MAX_SPEED_X);
    	} else if (getNode().getTranslateX() < -MAX_SPEED_X) {
        		setvX(-MAX_SPEED_X);
        } else {
        		setvX(getNode().getTranslateX());
    	}

    	//Update x velocity
    	if (getNode().getTranslateY() > MAX_SPEED_Y) {
    		setvY(MAX_SPEED_Y);
    	} else if (getNode().getTranslateY() < -MAX_SPEED_Y) {
        		setvY(-MAX_SPEED_Y);
    	} else {
        		setvY(getNode().getTranslateY());
        	}
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
        setvX(0);
        setvY(0);
        FadeTransitionBuilder.create()
                .node(getNode())
                .duration(Duration.millis(ANIMATION_DURATION))
                .fromValue(getNode().getOpacity())
                .toValue(0)
                .onFinished(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent arg0) {
                        setDead(true);
                        sceneGroup.getChildren().remove(getNode());
                    }
                })
                .build()
                .play();
    }

    /**
     * Simple version of implode.
     * Just sets the isdead to true so the update() method handles
     * not showing the sprite anymore.
     */
    public void simpleImplode() {
        setDead(true);
    }
}
