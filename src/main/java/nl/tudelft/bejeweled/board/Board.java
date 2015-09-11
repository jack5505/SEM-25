package nl.tudelft.bejeweled.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import nl.tudelft.bejeweled.game.BejeweledGame;
import nl.tudelft.bejeweled.sprite.Jewel;
import nl.tudelft.bejeweled.sprite.SelectionCursor;
import nl.tudelft.bejeweled.sprite.SpriteStore;

/**
 * Created by Jeroen on 4-9-2015.
 * Class that encapsulates the board.
 */
public class Board {
	private int gridWidth;
	private int gridHeight;
	private int spriteWidth;
	private int spriteHeight;
	private static final int MINIMAL_COMBO_LENGTH = 3;
	public static final int NUMBER_OF_JEWEL_TYPES = 7;


    private List<Jewel> selection = new ArrayList<Jewel>();
    
    private Random rand = new Random();
    
    private Jewel[][] grid;

    private final List<BoardObserver> observers;

    /** The JavaFX group containing all the jewels. */
    private Group sceneNodes;

	private SpriteStore spriteStore;
	private SelectionCursor selectionCursor;
	private boolean toReverseMove = false;
	private Jewel reverse1;
	private Jewel reverse2;

    /**
     * Constructor for the board class.
     * @param grid Two-dimensional grid holding all Jewel sprites.
     * @param sceneNodes The JavaFX group container for the Jewel Nodes.
     * @param gridWidth Width of the board in squares.
     * @param gridHeight Height of the board in squares.
     * @param spriteWidth Width of the sprites in pixels.
     * @param spriteHeight Height of the sprites in pixels.
     */

    public Board(Jewel[][] grid, Group sceneNodes) {
        this.gridWidth = BejeweledGame.GRID_WIDTH;
        this.gridHeight = BejeweledGame.GRID_HEIGHT;
        this.spriteWidth = BejeweledGame.SPRITE_WIDTH;
        this.spriteHeight = BejeweledGame.SPRITE_WIDTH;
        
        this.grid = grid;
        this.sceneNodes = sceneNodes;
        
        this.observers = new ArrayList<>();
    }

    /**
     * Adds an observer for the board.
     * @param observer BoardObserver to be added to the list of observers
     */
    public void addObserver(BoardObserver observer) {
    	if (!observers.contains(observer)) {
    		observers.add(observer);
    	}
    }
    
    
    /**
     * Removes an observer of the board.
     * @param observer BoardObserver to be removed
     */
    public void removeObserver(BoardObserver observer) {
    	observers.remove(observer);
    }

    /**
     * Updates the observers a jewel was removed.
     */
    private void updateScore() {
        for (BoardObserver observer : observers) {
            observer.boardJewelRemoved();
        }
    }

    /**
     * Handler method for detecting a click on the board
     * @param noJewelHit Boolean determining if mouse is over a Jewel or not
     */
    public void boardClicked(Boolean noJewelHit) {
        if(noJewelHit) {
            if(getSelectionCursor() != null) {
                // if there was a selection remove it
                sceneNodes.getChildren().remove(getSelectionCursor().getNode());
                selectionCursor = null;
            }
            getSelection().clear();
        }
    }

    /**
     * Add Jewel to the current selection.
     * After 2 Jewels are selected they are swapped.
     * @param jewel The Jewel to be added to the current selection.
     */
    public void addSelection(Jewel jewel) {
        getSelection().add(jewel);

        //TODO Cleanup this method with better logic.

        if (getSelection().size() == 1) {
           selectionCursor = new SelectionCursor(getSelection().get(0).getxPos(), getSelection().get(0).getyPos());
           spriteStore.addSprites(getSelectionCursor());
           sceneNodes.getChildren().add(0, getSelectionCursor().getNode());
        }
        
        // 2 gems are selected, see if any combo's are made
        if (getSelection().size() == 2) {

            if (moveWithinDomain(getSelection().get(0), getSelection().get(1))) {
                System.out.println("Swapping jewels");
                swapJewel(getSelection().get(0), getSelection().get(1));

                int comboCount = checkBoardCombos();
                System.out.println("Combo Jewels on board: " + comboCount);
                if (comboCount == 0) {
                	setToReverse(getSelection().get(0), getSelection().get(1));
                }

            }
            sceneNodes.getChildren().remove(getSelectionCursor().getNode());
            selectionCursor = null;
            getSelection().clear();

        }
    }
    
	/**
     * Set up two jewels to be swapped, intended to undo an invalid move.
     * @param j1 The first Jewel.
     * @param j2 The second Jewel.
     */
    private void setToReverse(Jewel jewel1, Jewel jewel2) {
		toReverseMove = true;
		reverse1 = jewel1;
		reverse2 = jewel2;
	}
    
	/**
     * Jewels set up to be swapped will swap if their current animations have finished.
     */
    private void tryToReverse() {
    	assert (reverse1 != null);
    	assert (reverse2 != null);
 		if (!reverse1.animationActive() && !reverse2.animationActive()) {
 			swapJewel(reverse1, reverse2);
 			toReverseMove = false;
 			reverse1 = null;
 			reverse2 = null;
 		}
 	}
     

	/**
     * Checks if two Jewels are exactly one apart either horizontally
     * or vertically.
     * @param j1 The first Jewel.
     * @param j2 The second Jewel.
     * @return True if the Jewels can be swapped.
     */
    public Boolean moveWithinDomain(Jewel j1, Jewel j2) {
        if (Math.abs(j1.getBoardX() - j2.getBoardX()) 
        		+ Math.abs(j1.getBoardY() - j2.getBoardY()) == 1) {
            return true;
        }
        return false;
    }

    /**
     * Swaps two Jewel's with one another.
     * Both the grid positions as the graphics positions are updated.
     * @param j1 First Jewel to be swapped.
     * @param j2 Second Jewel to be swapped.
     */
    public void swapJewel(Jewel j1, Jewel j2) {
        // TODO This can probably be done nicer
        int x1, x2, y1, y2;
        x1 = j1.getBoardX();
        y1 = j1.getBoardY();
        x2 = j2.getBoardX();
        y2 = j2.getBoardY();

        Jewel tmpJewel;
        int tmp;
        double previousJ1, previousJ2;
        // find out what direction the change is
        // horizontal swap
        if (Math.abs(j1.getBoardX() - j2.getBoardX()) == 1) {
            tmp = j1.getBoardX();
            j1.setBoardX(j2.getBoardX());
            j2.setBoardX(tmp);

            previousJ1 = j1.getxPos();
            j1.setxPos(j2.getxPos());
            j1.getNode().setTranslateX(previousJ1 - j1.getxPos());
            previousJ2 = j2.getxPos();
            j2.setxPos(previousJ1);
            j2.getNode().setTranslateX(previousJ2 - j2.getxPos());

           
        }
        else if (Math.abs(j1.getBoardY() - j2.getBoardY()) == 1) {
            // vertical swap
            tmp = j1.getBoardY();
            j1.setBoardY(j2.getBoardY());
            j2.setBoardY(tmp);

            previousJ1 = j1.getyPos();
            j1.setyPos(j2.getyPos());
            j1.getNode().setTranslateY(previousJ1 - j1.getyPos());
            previousJ2 = j2.getyPos();
            j2.setyPos(previousJ1);
            j2.getNode().setTranslateY(previousJ2 - j2.getyPos());

        }

        tmpJewel = j1;
        grid[x1][y1] = j2;
        grid[x2][y2] = tmpJewel;
    }

    /**
     * Check the board for any combo Jewels.
     * @return The number of Jewels removed from the game.
     */
    public int checkBoardCombos() {
        List<Jewel> comboList = new ArrayList<>();

        // list to hold current selection of combo
        Stack<Jewel> current = new Stack<>();

        //TODO Find nicer implementation to find combos, maybe with resursion.
        int count;
        int matches;
        int type;

        // first check columns for combos
        for (int col = 0; col < grid.length; col++) {
            matches = 0;
            type = 0; //Reserve 0 for the empty state. If we make it a normal gem type, then only 2 are needed to match for the start.
            for (int i = 0; i < grid[0].length; i++) {
                if (grid[col][i].getType() == type && type != 0) {
                    matches++;
                    current.push(grid[col][i]);
                }
                if (grid[col][i].getType() != type || i == grid[0].length - 1) { //subtract 1 because arrays start at 0
                    if (matches >= MINIMAL_COMBO_LENGTH) {
                        while (!current.empty()) {
                            comboList.add(current.pop());
                        }
                    }
                    current.clear();
                    type = grid[col][i].getType();
                    current.push(grid[col][i]);
                    matches = 1;
                }
            }
        }

        // then do the rows
        current.clear();
        for (int row = 0; row < grid[0].length; row++) {
            matches = 0;
            type = 0; //Reserve 0 for the empty state. If we make it a normal gem type, then only 2 are needed to match for the start.
            for (int i = 0; i < grid.length; i++) {
                if (grid[i][row].getType() == type && type != 0) {
                    matches++;
                    current.push(grid[i][row]);
                }
                if (grid[i][row].getType() != type || i == grid[0].length - 1) { //subtract 1 because arrays start at 0
                    if (matches >= MINIMAL_COMBO_LENGTH) {
                        while (!current.empty()) {
                            comboList.add(current.pop());
                        }
                    }
                    current.clear();
                    type = grid[i][row].getType();
                    current.push(grid[i][row]);
                    matches = 1;
                }
            }
        }

        // remove duplicates by putting the list in set
        Set<Jewel> comboSet = new HashSet<>();
        comboSet.addAll(comboList);
        comboList.clear();
        comboList.addAll(comboSet);
        count = comboList.size();

        for (Iterator<Jewel> jewelIterator = comboList.iterator(); jewelIterator.hasNext(); ) {
            Jewel jewel = jewelIterator.next();
            // remove the JavaFX nodes from the scene group and animate an implosion
            jewel.implode(sceneNodes);

            // remove the event filter
            jewel.getNode().setOnMouseClicked(null);

            updateScore();

            // remove the Jewel from the Grid
            // TODO Make sure the Jewels are also removed from the spriteStore.
            // grid[jewel.getBoardX()][jewel.getBoardY()] = null;
        }

      //  doGravity();

      //  outOfMoves();
        
        return count;
    }
    
    
    /**
     * Checks if a pair of similar jewels can be extended by a valid move (done for a vertical pair
     * but can be used for horizontal if jewels matrix is transposed).
     * @param jewels The grid containing the Jewels
     * @param x x-position of the first jewel of the pair to be extended
     * @param y y-position of the first jewel of the pair to be extended
     * @return if valid move is possible list of jewels to swap, otherwise empty list
     */
    public ArrayList<Integer> validMove(Jewel[][] jewels, int x, int y) {
    	/**
    	 * Check completion at left end of the pair (i,j-1)
    	 */
    	if (y > 0) {
    		if (y > 1 && jewels[x][y - 2].getType() == jewels[x][y].getType()) {
    			return new ArrayList<Integer>(Arrays.asList(x, y - 1, x, y - 2));
    		}
    		if (x > 0 && jewels[x - 1][y - 1].getType() == jewels[x][y].getType()) {
    			return new ArrayList<Integer>(Arrays.asList(x, y - 1, x - 1, y - 1));
    		}
    		if (x < jewels.length - 1 && jewels[x + 1][y - 1].getType() == jewels[x][y].getType()) {
    			return new ArrayList<Integer>(Arrays.asList(x, y - 1, x + 1, y - 1));
    		}
    	}
    	
    	/**
    	 * Check completion at right end of the pair (i,j+2)
    	 */
    	if (y < jewels[0].length - 2) {
    		if (y < jewels[0].length - 3 && jewels[x][y + 3].getType() == jewels[x][y].getType()) {
    			return new ArrayList<Integer>(Arrays.asList(x, y + 2, x, y + 3));
    		}
    		if (x > 0 && jewels[x - 1][y + 2].getType() == jewels[x][y].getType()) {
    			return new ArrayList<Integer>(Arrays.asList(x, y + 2, x - 1, y + 2));
    		}
    		if (x < jewels.length - 1 && jewels[x + 1][y + 2].getType() == jewels[x][y].getType()) {
    			return new ArrayList<Integer>(Arrays.asList(x, y + 2, x + 1, y + 2));
    		}
    	}
    	
    	return new ArrayList<Integer>();
    }
    
    /**
     * Checks a vertical pair for a valid move.
     * @param x x-position of the first jewel of the vertical pair
     * @param y y-position of first jewel of the vertical pair
     */
    private boolean verticalRow(int x, int y) {
    	ArrayList<Integer> swap = validMove(grid, x, y);
    	System.out.println(swap);
    	if (swap.isEmpty()) {
    		return false;
    	}
    	System.out.print("Switch (" + swap.get(0) + "," + swap.get(1) + ") with (");
    	System.out.println(swap.get(2) + "," + swap.get(3) + ")");
    	return true;
    }
    
    /**
     * Checks a horizontal pair for a valid Move.
     * @param x x-position of the first jewel of the horizontal pair
     * @param y y-position of first jewel of the horizontal pair
     */
    private boolean horizontalRow(int x, int y) {
    	/**
    	 * Create transpose of matrix
    	 */
    	Jewel[][] transposed = new Jewel[grid[0].length][grid.length];
    	for (int i = 0; i < grid[0].length; i++) {
			for (int j = 0; j < grid.length; j++) {
				transposed[i][j] = grid[j][i];
			}
		}
    	
    	ArrayList<Integer> swap = validMove(transposed, y, x);
    	System.out.println(swap);
    	if (swap.isEmpty()) {
    		return false;
    	}
    	System.out.print("Switch (" + swap.get(1) + "," + swap.get(0) + ") with (");
    	System.out.println(swap.get(3) + "," + swap.get(2) + ")");
    	return true;
    }
    
    /**
     * Checks a possible horizontal row (xox) for a valid move.
     * @param x x-position of first jewel of possible row
     * @param y y-position of first jewel of possible row
     */
    private boolean horizontalRowPossible(int x, int y) {
    	if (y > 0 && grid[x + 1][y - 1].getType() == grid[x][y].getType()) {
    		System.out.print("Switch (" + (x + 1) + "," + y + ") with (");
        	System.out.println((x + 1) + "," + (y - 1) + ")");
        	return true;
    	}
    	if (y < grid[0].length - 1 && grid[x + 1][y + 1].getType() == grid[x][y].getType()) {
    		System.out.print("Switch (" + (x + 1) + "," + y + ") with (");
        	System.out.println((x + 1) + "," + (y + 1) + ")");
        	return true;
    	}
    	
    	return false;
    }
    
    /**
     * Checks a possible vertical row (xox) for a valid move.
     * @param x x-position of first jewel of possible row
     * @param y y-position of first jewel of possible row
     */
    private boolean verticalRowPossible(int x, int y) {
    	if (x > 0 && grid[x - 1][y + 1].getType() == grid[x][y].getType()) {
    		System.out.print("Switch (" + x + "," + (y + 1) + ") with (");
        	System.out.println((x - 1) + "," + (y + 1) + ")");
        	return true;
    	}
    	if (x < grid.length - 1 && grid[x + 1][y + 1].getType() == grid[x][y].getType()) {
    		System.out.print("Switch (" + x + "," + (y + 1) + ") with (");
        	System.out.println((x + 1) + "," + (y + 1) + ")");
        	return true;
    	}
    	return false;
    }
    
    /**
     * Function that checks if there are any moves possible.
     * @return true if any moves possible
     */
    public boolean outOfMoves() {
    	/**
    	 * Iterate through all gems and look for pairs of two,
    	 * which could be a possible row to complete
    	 */
    	for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (y < grid[0].length - 1 && grid[x][y].getType() == grid[x][y + 1].getType()) {
					System.out.println("vertical row found at " + x + ", " + y);
					if (verticalRow(x, y)) {
						return false;
					}
				}
				if (x < grid.length - 1 && grid[x][y].getType() == grid[x + 1][y].getType()) {
					System.out.println("horizontal row found at " + x + ", " + y);
					if (horizontalRow(x, y)) {
						return false;
					}
				}
				if (y < grid[0].length - 2 && grid[x][y].getType() == grid[x][y + 2].getType()) {
					System.out.println("vertical row possible at " + x + ", " + y);
					if (verticalRowPossible(x, y)) {
						return false;
					}
				}
				if (x < grid.length - 2 && grid[x][y].getType() == grid[x + 2][y].getType()) {
					System.out.println("horizontal row possible at " + x + ", " + y);
					if (horizontalRowPossible(x, y)) {
						return false;
					}
				}
			}
		}
    	
    	// if the flow arrives here then no move was found; inform the observers
    	for (BoardObserver observer : observers) {
			observer.boardOutOfMoves();
		}
		return true;
    	
    }
    
    /**
     * Function that runs one step of jewels falling down.
     * @return True if jewels are moving.
     */
    public Boolean doGravityStep() {
    	boolean falling;
		for (int j = 1; j < gridHeight; j++) {
			falling = false;
			for (int i = 0; i < gridWidth; i++) {		
					if (grid[i][j].isDead()) {
						System.out.println("Swap " + j + " and " + (j - 1));
						swapJewel(grid[i][j], grid[i][j - 1]);
						falling = true;
					}
				}
				if (falling) {
					return true;
				}
			}
		return false;
    }
    
    /**
     * Function that makes the jewels fall down.
     * @return True if jewels are moving.
     */
    public boolean doGravity() {
    	boolean changes = false;
    	while (doGravityStep()) {
    		changes = true;
    		fillEmptySpots();
    	}
    	return changes;
    }
    

    /**
     * This function adds a jewel of a random type to the grid at the specified position.
     * @param i Grid column
     * @param j Grid row
     */
    protected void addRandomJewel(int i, int j) {
    	  Jewel jewel = new Jewel(rand.nextInt(NUMBER_OF_JEWEL_TYPES) + 1, i, j);
          jewel.setxPos(i * spriteWidth);
          jewel.setyPos(j * spriteHeight);
          grid[i][j] = jewel;
          spriteStore.addSprites(jewel);
          sceneNodes.getChildren().add(0, jewel.getNode());
          setSpriteStore(spriteStore);
          grid[i][j].getNode().addEventFilter(MouseEvent.MOUSE_CLICKED,
                  new EventHandler<MouseEvent>() {
                      public void handle(MouseEvent event) {
                          System.out.println("Jewel[" + jewel.getBoardX() + "][" + jewel.getBoardY() + "] " + event.getEventType());
                          addSelection(jewel);
                          event.consume();
                      }
                  }
          );
    }

    /**
     * This function fills empty spots in the top row with new jewels.
     * (Combos being removed cause empty spots on the grid, 
     * the empty spots propagate to the to of the grid through gravity.)
     */
    public void fillEmptySpots() {
    	for (int i = 0; i < gridWidth; i++) {	
    		if (grid[i][0].isDead()) {
    			addRandomJewel(i, 0);
            }
    	}
    }
    
    /**
	 * Getter function for the current spriteStore.
	 * @return current spriteStore
	 */
	public Object getSpriteStore() {
		return spriteStore;
	}
    /**
	 * Setter function for the current spriteStore.
	 * @param spriteStore spriteStore to be set
	 */
	public void setSpriteStore(SpriteStore spriteStore) {
		this.spriteStore = spriteStore;
	}
	
    /**
	 * Update the board; check for combos, run gravity and fill empty spots .
	 * 	 */
	public void update() {
		if(toReverseMove) {
			tryToReverse();
		} else {
    		checkBoardCombos();
			while (doGravity()) {
	    		checkBoardCombos();
	    	}
			fillEmptySpots();
			outOfMoves();
		}
	}
	
	/**
	 * Getter function for the current jewel Grid (used for testing).
	 * @return current grid of jewels
	 */
	public Jewel[][] getGrid() {
		return grid;
	}

	/**
	 * Getter function for the current selection (used for testing).
	 * @return current selection
	 */
	public List<Jewel> getSelection() {
		return selection;
	}
	
	/**
	 * Getter function for the current selectionCursor (used for testing).
	 * @return current selectionCursor
	 */
	public SelectionCursor getSelectionCursor() {
		return selectionCursor;
	}
}
