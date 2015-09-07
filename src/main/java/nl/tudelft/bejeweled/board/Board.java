package nl.tudelft.bejeweled.board;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import nl.tudelft.bejeweled.sprite.Jewel;
import nl.tudelft.bejeweled.sprite.SpriteStore;

import java.util.*;


/**
 * Created by Jeroen on 4-9-2015.
 * Class that encapsulates the board.
 */
public class Board {

    private final SpriteStore spriteStore;

    /** The sprite store providing the sprites for the game. */
    private List<Jewel> selection = new ArrayList<Jewel>();

    private double width = 0, height = 0;

    private Jewel[][] grid;

    /** The JavaFX group containing all the jewels */
    private Group sceneNodes;

    /**
     * Constructor for the board class
     * @param grid Two-dimensional grid holding all Jewel sprites.
     * @param width Width of the board scene in pixels.
     * @param height Height of the board scene in pixels.
     */
    public Board(Jewel[][] grid, Group sceneNodes, double width, double height, final SpriteStore spriteStore) {
        this.grid = grid;
        this.width = width;
        this.height = height;
        this.sceneNodes = sceneNodes;
        this.spriteStore = spriteStore;
    }

    /**
     * Add Jewel to the current selection.
     * After 2 Jewels are selected they are swapped.
     * @param jewel The Jewel to be added to the current selection.
     */
    public void addSelection(Jewel jewel) {
        selection.add(jewel);

        // 2 gems are selected, see if any combo's are made
        if(selection.size() == 2) {

            if(moveWithinDomain(selection.get(0), selection.get(1))) {
                System.out.println("Swapping jewels");
                swapJewel(selection.get(0), selection.get(1));

                int comboCount = checkBoardCombos();
                System.out.println("Combo Jewels on board: " + comboCount);
            }
            else
                System.out.println("No swap made");

            // reset the selection
            selection.clear();
        }
    }

    /**
     * Generates new Jewels if any have imploded by user input.
     */
    public void updateBoard() {
        generateJewels();
    }

    /**
     * Checks if two Jewels are exactly one apart either horizontally
     * or vertically.
     * @param j1 The first Jewel.
     * @param j2 The second Jewel.
     * @return True if the Jewels can be swapped.
     */
    public Boolean moveWithinDomain(Jewel j1, Jewel j2) {
        if(Math.abs(j1.getBoardX() - j2.getBoardX()) +
                Math.abs(j1.getBoardY() - j2.getBoardY()) == 1) {
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
        double tmp2;
        // find out what direction the change is
        // horizontal swap
        if(Math.abs(j1.getBoardX() - j2.getBoardX()) == 1) {
            tmp = j1.getBoardX();
            j1.setBoardX(j2.getBoardX());
            j2.setBoardX(tmp);

            tmp2 = j1.xPos;
            j1.xPos = j2.xPos;
            j2.xPos = tmp2;
        }
        else if (Math.abs(j1.getBoardY() - j2.getBoardY()) == 1) {
            // vertical swap
            tmp = j1.getBoardY();
            j1.setBoardY(j2.getBoardY());
            j2.setBoardY(tmp);

            tmp2 = j1.yPos;
            j1.yPos = j2.yPos;
            j2.yPos = tmp2;
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

        //TODO Find nicer implementation to find combos, maybe with recursion.
        int count;
        int matches;
        int type;

        // first check columns for combos
        for(int col = 0; col < grid.length; col++){
            matches = 0;
            type = 0; //Reserve 0 for the empty state. If we make it a normal gem type, then only 2 are needed to match for the start.
            for(int i = 0; i < grid[0].length; i++){
                if(grid[col][i].getType() == type){
                    matches++;
                    current.push(grid[col][i]);
                }
                if(grid[col][i].getType() != type || i == grid[0].length - 1){ //subtract 1 because arrays start at 0
                    if(matches >= 3){
                        while(!current.empty())
                            comboList.add(current.pop());
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
        for(int row = 0; row < grid.length; row++){
            matches = 0;
            type = 0; //Reserve 0 for the empty state. If we make it a normal gem type, then only 2 are needed to match for the start.
            for(int i = 0; i < grid[0].length; i++){
                if(grid[i][row].getType() == type){
                    matches++;
                    current.push(grid[i][row]);
                }
                if(grid[i][row].getType() != type || i == grid[0].length - 1){ //subtract 1 because arrays start at 0
                    if(matches >= 3){
                        while(!current.empty())
                            comboList.add(current.pop());
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

        // remove the combo Jewels from the board
        for (Jewel jewel : comboList) removeJewel(jewel);

        return count;
    }

    /**
     * Generates new Jewels in the grid.
     * References to a null object are replaced by new Jewels.
     */
    public void generateJewels() {
        // assumptions: either 3+ horizontal or 3+ vertical Jewels are missing.
        Random rand = new Random();
/*
        int k = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 6; j >= 0; j--) {
                if(grid[i][j] == null)
                    continue;

                k = j + 1;
                while(grid[i][k] == null) {
                    grid[i][k] = grid[i][k-1];
                    grid[i][k].setBoardY(grid[i][k].getBoardY() + 1);
                    grid[i][k].yPos += height / 8.0;
                    k++;
                }
            }
        }
        /*
        // check the grid for null jewels
        for(int j = 7; j >= 0; j--) {
            for(int i = 7; i >= 0; i--) {
                if(grid[i][j] == null) {
                    // loop through all vertical nodes above this Jewel
                    // first one it sees replaces this empty spot and creates a new empty spot
                    for(int k = j - 1; k >= 0; k--) {
                        if(grid[i][k] != null) {
                            // update grid[i][k] jewel with new position information for animation
                            grid[i][k].setBoardY(j);
                            grid[i][k].yPos += height / 8.0;

                            // finally swap them
                            grid[i][j] = grid[i][k];
                            grid[i][k] = null;
                        }
                        else if(grid[i][k] == null && j == 0) {
                            // we found no jewels and are at the top so start filling
                            // the vertical column
                            for(int l = 7; l >= 0; l--) {
                                if(grid[i][l] == null) {
                                    // generate a new jewel at this place
                                    Jewel jewel = new Jewel(rand.nextInt((7 - 1) + 1) + 1, i, l);
                                    jewel.xPos = i * (width / 8.0);
                                    jewel.yPos = l * (height / 8.0);
                                    grid[i][l] = jewel;

                                    // add to actors in play (sprite objects)
                                    spriteStore.addSprites(jewel);

                                    // add sprites
                                    sceneNodes.getChildren().add(0, jewel.node);
                                }
                            }
                        }
                    }
                }
            }
        }
        */
    }

    /**
     * Remove the Jewel from the game.
     * @param jewel The jewel to be removed.
     */
    public void removeJewel(Jewel jewel) {
        // remove the JavaFX nodes from the scene group and animate an implosion
        jewel.implode(sceneNodes);

        // remove the event filter
        jewel.node.setOnMouseClicked(null);

        // remove the jewel from the sprite store
        spriteStore.removeSprite(jewel);

        // remove the Jewel from the Grid
     //   grid[jewel.getBoardX()][jewel.getBoardY()] = null;
    }

    /**
     * Function that checks if jewels are falling down.
     * @return True if jewels are moving.
     */
    public Boolean doGravity() {
        return true;
    }

    public void fillEmptySpots() {

    }
}
