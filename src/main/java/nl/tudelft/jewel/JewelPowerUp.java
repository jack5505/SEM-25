package nl.tudelft.jewel;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import nl.tudelft.bejeweled.sprite.JewelSprite;
import nl.tudelft.bejeweled.sprite.Sprite;

/**
 * A Decorator class that adds a powerup to a jewel.
 * @author Pim
 *
 */
public abstract class JewelPowerUp implements Jewel {
	protected Jewel baseJewel;
	
	public JewelPowerUp (Jewel baseJewel) {
		this.baseJewel = baseJewel;
	}

	@Override
	public void update() {
		baseJewel.update();
	}

	@Override
	public JewelSprite getSprite() {
		return baseJewel.getSprite();
	}

	@Override
	public int getBoardX() {
		return baseJewel.getBoardX();
	}

	@Override
	public int getBoardY() {
		return baseJewel.getBoardY();
	}

	@Override
	public int getType() {
		return baseJewel.getType();
	}

	@Override
	public void setBoardX(int boardX) {
		baseJewel.setBoardX(boardX);
	}

	@Override
	public void setBoardY(int boardY) {
		baseJewel.setBoardY(boardY);
	}

	@Override
	public void implode(Group sceneGroup) {
		baseJewel.implode(sceneGroup);
	}

	@Override
	public void remove(Group sceneGroup) {
		baseJewel.remove(sceneGroup);
	}

	@Override
	public void fadeIn(Group sceneGroup) {
		baseJewel.fadeIn(sceneGroup);
	}
	
	@Override
	public List<Node> getNodes() {
		return baseJewel.getNodes();
	}
	
	@Override
	public void relativeMoveTo(int i, int j) {
		baseJewel.relativeMoveTo(i, j);
	}
	
	@Override
	public List<JewelSprite> getSprites() {
		return baseJewel.getSprites();
	}

}
