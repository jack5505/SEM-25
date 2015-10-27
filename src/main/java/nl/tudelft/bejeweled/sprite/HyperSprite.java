package nl.tudelft.bejeweled.sprite;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** Class that encapsulates the sprite for the Hyper Power Up.
 * @author Pim
 *
 */
public class HyperSprite extends JewelSprite {

	 /**
     * Constructor for ExplosiveSprite class.
     * @param x The X position of this SelectionCursor on the board grid (in number of squares).
     * @param y The Y position of this SelectionCursor on the board grid (in number of squares).
     */
	public HyperSprite(int x, int y) {
		super(1, x, y);		
        ImageView explosiveImageView = new ImageView();
        Image explosiveImage = new Image(SelectionCursor.class.getResourceAsStream("/hyper.png"));
        explosiveImageView.setImage(explosiveImage);
        explosiveImageView.setStyle("-fx-background-color:transparent;");

        setNode(explosiveImageView);
	}
	
	
	
		
}
