package nl.tudelft.bejeweled.sprite;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nl.tudelft.bejeweled.jewel.Jewel;

/**
 * Created by Jeroen on 5-9-2015.
 * Jewel class that holds all sprite information.
 */
public class JewelSprite extends Sprite {

		public static final double MAX_SPEED_X = 4;
		public static final double MAX_SPEED_Y = 4;
		public static final int FADE_OUT_DURATION = 300;
		public static final int EXPLODE_DURATION = 600;
		public static final int FADE_IN_DURATION = 300;
		public static final int EXPLOSIVE_OFFSET = -100;
		
	    private final int type;
		private Image jewelImage;

	    /**
	     * Constructor for Jewel class.
	     * @param type The type of Jewel created.
	     * @param x The horizontal position of this Jewel on the board grid (in pixels).
	     * @param y The vertical position of this Jewel on the board grid (in pixels).
	     */
	    public JewelSprite(int type, int x, int y) {
	        this.type = type;
	        setxPos(x);
	        setyPos(y);

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
	    	if (getNode().getTranslateX() != 0 || getNode().getTranslateY() != 0) {
	    		setState(SpriteState.ANIMATION_ACTIVE);
		    	updateVelocity();
		    	getNode().setTranslateX(getNode().getTranslateX() - getvX());
		    	getNode().setTranslateY(getNode().getTranslateY() - getvY());
		        if (getNode().getTranslateX() == 0 && getNode().getTranslateY() == 0) {
		    		setState(SpriteState.IDLE);
		        }
	    	}
	     	
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
	     * Getter method for Jewel type.
	     * @return The type identifier for this Jewel.
	     */
	    public int getType() {
	        return type;
	    }

	    /**
	     * Animate an implosion. Once done remove from the game world
	     * @param sceneGroup Game scene group to remove the Jewel from.
	     */
	    public void implode(Group sceneGroup) {
	        setState(SpriteState.ANIMATION_ACTIVE);
	        setvX(0);
	        setvY(0);

	        FadeTransition ft = new FadeTransition(Duration.millis(FADE_OUT_DURATION), getNode());
	        ft.setFromValue(1.0);
	        ft.setToValue(0.0);
	        ft.setCycleCount(1);
	        ft.setAutoReverse(false);
	        ft.setOnFinished(event -> {
	            setState(SpriteState.TO_BE_REMOVED);
	            if (sceneGroup != null) {
	                sceneGroup.getChildren().remove(getNode());
	            }
	        });
	        ft.play();
	    }

	    /**
	     * Animate an explosion. Once done remove from the game world
	     * @param sceneGroup Game scene group to remove the Jewel from.
	     */
	    public void explode(Group sceneGroup) {
	        setState(SpriteState.ANIMATION_ACTIVE);
	        setvX(0);
	        setvY(0);

	        String imagePath = "/explode_29.png";
	        jewelImage = new Image(Jewel.class.getResourceAsStream(imagePath));
	        ImageView jewelImageView = new ImageView();
	        jewelImageView.setImage(jewelImage);
	        jewelImageView.setX(EXPLOSIVE_OFFSET);
	        jewelImageView.setY(EXPLOSIVE_OFFSET);
	        jewelImageView.setStyle("-fx-background-color:transparent;");
	        sceneGroup.getChildren().remove(getNode());
	        sceneGroup.getChildren().add(jewelImageView);
	        setNode(jewelImageView);
	        
	        FadeTransition ft = new FadeTransition(Duration.millis(EXPLODE_DURATION), getNode());
	        ft.setFromValue(1.0);
	        ft.setToValue(0.0);
	        ft.setCycleCount(1);
	        ft.setAutoReverse(false);
	        ft.setOnFinished(event -> {
	            setState(SpriteState.TO_BE_REMOVED);
	            if (sceneGroup != null) {
	                sceneGroup.getChildren().remove(getNode());
	            }
	        });
	        ft.play();
	    }
	    
	    /**
	     * Simple version of implode, to remove the jewel from the game.
	     * @param sceneGroup Game scene group to remove the Jewel from.
	     */
	    public void remove(Group sceneGroup) {
	        setState(SpriteState.TO_BE_REMOVED);
	        if (sceneGroup != null) {
	            sceneGroup.getChildren().remove(getNode());
	        }
	    }
	    
	  /**
	   * Animate a fade in for the Jewel.
	   * @param sceneGroup the sceneGroup which displays the jewel fading in.
	   */
	    public void fadeIn(Group sceneGroup) {
	        setState(SpriteState.ANIMATION_ACTIVE);
	        setvX(0);
	        setvY(0);

	        FadeTransition ft = new FadeTransition(Duration.millis(FADE_IN_DURATION), getNode());
	        ft.setFromValue(0.0);
	        ft.setToValue(1.0);
	        ft.setCycleCount(1);
	        ft.setAutoReverse(false);
	        ft.setOnFinished(event -> {
	            setState(SpriteState.IDLE);
	        });
	        ft.play();
	    }
	}
