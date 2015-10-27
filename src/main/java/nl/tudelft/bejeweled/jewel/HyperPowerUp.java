package nl.tudelft.bejeweled.jewel;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import nl.tudelft.bejeweled.sprite.HyperSprite;
import nl.tudelft.bejeweled.sprite.JewelSprite;

/**
 * Power Up that adds Hyper capabilities to a Jewel.
 * @author Pim
 *
 */
public class HyperPowerUp extends JewelPowerUp {

	private JewelSprite powerUpSprite;
	
	/**
	 * Constructor for the hyper power up. 
	 * Uses the decorator pattern, so should be applied on top of a base Jewel.
	 * @param baseJewel the jewel the power up is applied to.
	 */
	public HyperPowerUp(Jewel baseJewel) {
		super(baseJewel);
		this.powerUpSprite = new HyperSprite(super.getSprite().getxPos(),
											 super.getSprite().getyPos()); 
	}
	
	@Override
	public void update() {
		super.update();
		powerUpSprite.update();
	}

	@Override
	public List<Node> getNodes() {
		List<Node> nodes = new ArrayList<Node>();
		nodes.addAll(super.getNodes());
		nodes.add(powerUpSprite.getNode());
		return nodes;
	}
	
	@Override
	public List<JewelSprite> getSprites() {
		List<JewelSprite> sprites = new ArrayList<JewelSprite>();
		sprites.addAll(super.getSprites());
		sprites.add(powerUpSprite);
		return sprites;
	}
	
	@Override
	public void relativeMoveTo(int i, int j) {
		super.relativeMoveTo(i, j);
		powerUpSprite.relativeMoveTo(i, j);
	}
	
	@Override
	public void implode(Group sceneGroup) {
		super.explode(sceneGroup);
		powerUpSprite.explode(sceneGroup);
		
	}
	
	@Override
	public boolean isHyper() {
		return true;
	}
}
