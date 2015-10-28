package nl.tudelft.bejeweled.jewel;

import nl.tudelft.bejeweled.sprite.HyperSprite;

/**
 * Power Up that adds Hyper capabilities to a Jewel.
 * @author Pim
 *
 */
public class HyperPowerUp extends JewelPowerUp {
	/**
	 * Constructor for the hyper power up. 
	 * Uses the decorator pattern, so should be applied on top of a base Jewel.
	 * @param baseJewel the jewel the power up is applied to.
	 */
	public HyperPowerUp(Jewel baseJewel) {
		super(baseJewel);
		baseJewel.setSprite(new HyperSprite(super.getSprite().getxPos(),
											 super.getSprite().getyPos())); 
	}
	
	@Override
	public boolean isHyper() {
		return true;
	}
}
