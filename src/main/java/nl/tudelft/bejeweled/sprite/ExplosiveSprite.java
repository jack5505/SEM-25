package nl.tudelft.bejeweled.sprite;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** Class that encapsulates the sprite for the Explosive Power Up.
 * @author Pim
 *
 */
public class ExplosiveSprite extends JewelSprite {

	 /**
     * Constructor for ExplosiveSprite class.
     * @param x The X position of this SelectionCursor on the board grid (in number of squares).
     * @param y The Y position of this SelectionCursor on the board grid (in number of squares).
     */
	public ExplosiveSprite(int x, int y) {
		super(1, x, y);		
        ImageView explosiveImageView = new ImageView();
        InputStream input = SelectionCursor.class.getResourceAsStream("/explosive.png");
        Image explosiveImage = new Image(input);
        explosiveImageView.setImage(explosiveImage);
        explosiveImageView.setStyle("-fx-background-color:transparent;");

        setNode(explosiveImageView);
	}
	
	
	
		
}
