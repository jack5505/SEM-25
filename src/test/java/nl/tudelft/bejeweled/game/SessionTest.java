package nl.tudelft.bejeweled.game;

import static org.junit.Assert.assertEquals;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;

import javafx.scene.Group;

import nl.tudelft.bejeweled.sprite.SpriteStore;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Pim on 9-10-2015.
 * Test class for the Session class.
 */
public class SessionTest {
	private SpriteStore mockSpriteStore;
	private Group mockGroup;
	
	/**
	 * Setup for tests.
	 */
	@Before
	public void setUp() {
		mockSpriteStore = mock(SpriteStore.class);
		mockGroup = new Group();
	}
	
	/**
	 * Test if leveling up works once.
	 */
	@Test
	public void testLevelUpOnce() {
		//Test parameters
		final int numberOfJewelsScored = 50;
		final int expectedLevel = 2;
		//Create a session where the displayText() method is overridden
		final Session testSession = spy(new Session(mockSpriteStore, mockGroup));
		doNothing().when(testSession).displayText(anyString());
		//Simulate combos
		for (int i = 0; i < numberOfJewelsScored; i++) {
			testSession.boardJewelRemoved();
			testSession.update();
		}
		assertEquals(expectedLevel, testSession.getLevel());
	}
	/**
	 * 
	 * Test if leveling up a lot works.
	 */
	@Test
	public void testLevelUpAlot() {
		//Test parameters
		final int numberOfJewelsScored = 1000;
		final int expectedLevel = 12;
		//Create a session where the displayText() method is overridden
		final Session testSession = spy(new Session(mockSpriteStore, mockGroup));
		doNothing().when(testSession).displayText(anyString());
		//Simulate combos
		for (int i = 0; i < numberOfJewelsScored; i++) {
			testSession.boardJewelRemoved();
			testSession.update();
		}
		assertEquals(expectedLevel, testSession.getLevel());
	}

}
