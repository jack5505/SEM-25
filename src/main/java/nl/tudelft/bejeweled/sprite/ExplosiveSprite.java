package nl.tudelft.bejeweled.sprite;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ExplosiveSprite extends JewelSprite{

	 /**
     * Constructor for ExplosiveSprite class.
     * @param x The X position of this SelectionCursor on the board grid (in number of squares).
     * @param y The Y position of this SelectionCursor on the board grid (in number of squares).
     */
	public ExplosiveSprite(int x, int y) {
		super(0, x, y);		
        ImageView explosiveImageView = new ImageView();
        Image explosiveImage = new Image(SelectionCursor.class.getResourceAsStream("/explosive.png"));
        explosiveImageView.setImage(explosiveImage);
        explosiveImageView.setStyle("-fx-background-color:transparent;");

        setNode(explosiveImageView);
	}
	
	
	
		
}
