package nl.tudelft.bejeweled.board;

import nl.tudelft.bejeweled.jewel.Jewel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jeroen on 20-10-2015.
 * Brute force strategy impementation that loops over all jewels
 * and sees if there is a move possible.
 */
public class BruteForceStrategy implements BoardMoveStrategy {

    private Jewel[][] grid;

    /**
     * Constructor for BruteForceStrategy.
     * @param grid The grid of Jewels to search.
     */
    public BruteForceStrategy(Jewel[][] grid) {
        this.grid = grid;
    }

    @Override
    public List<Jewel> getValidMovePair() {
        List<Jewel> swap = new ArrayList<>();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                swap = checkForPair(x, y);
                if (!swap.isEmpty()) {
                    return swap;
                }
            }
        }

        return swap;
    }

    /**
     * Checks grid for valid move pair.
     * @param x Starting x position.
     * @param y Starting y position.
     * @return Pair of Jewels if move is found, empty list otherwise.
     */
    public List<Jewel> checkForPair(int x, int y) {
        int type = grid[x][y].getType();
        List<Jewel> swap;
        if (y < grid[0].length - 1 && type == grid[x][y + 1].getType()) {
            swap = verticalRow(x, y);
            if (!swap.isEmpty()) {
                return swap;
            }
        }
        if (x < grid.length - 1 && type == grid[x + 1][y].getType()) {
            swap = horizontalRow(x, y);
            if (!swap.isEmpty()) {
                return swap;
            }
        }
        if (y < grid[0].length - 2 && type == grid[x][y + 2].getType()) {
            swap = verticalRowPossible(x, y);
            if (!swap.isEmpty()) {
                return swap;
            }
        }
        if (x < grid.length - 2 && type == grid[x + 2][y].getType()) {
            swap = horizontalRowPossible(x, y);
            if (!swap.isEmpty()) {
                return swap;
            }
        }
        return new ArrayList<Jewel>();
    }

    /**
     * Checks a vertical pair for a valid move.
     * @param x x-position of the first jewel of the vertical pair
     * @param y y-position of first jewel of the vertical pair
     * @return a list of the two Jewels to be swapped for a valid move
     */
    private List<Jewel> verticalRow(int x, int y) {
        final int three = 3;
        List<Integer> indices = validMove(grid, x, y);

        if (!indices.isEmpty()) {
            return Arrays.asList(grid[indices.get(0)][indices.get(1)],
                    grid[indices.get(2)][indices.get(three)]);
        }
        return new ArrayList<Jewel>();
    }

    /**
     * Checks the left most completion option for a valid move.
     *
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkLeft(Jewel[][] jewels, int x, int y) {
        if (y > 0) {
            if (y > 1 && jewels[x][y - 2].getType() == jewels[x][y].getType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks the left-top completion option for a valid move.
     *
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkLeftTop(Jewel[][] jewels, int x, int y) {
        if (y > 0) {
            if (x > 0 && jewels[x - 1][y - 1].getType() == jewels[x][y].getType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks the left-bottom completion option for a valid move.
     *
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkLeftBottom(Jewel[][] jewels, int x, int y) {
        if (y > 0) {
            if (x < jewels.length - 1 && jewels[x + 1][y - 1].getType()
                    == jewels[x][y].getType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks the right most completion option for a valid move.
     *
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkRight(Jewel[][] jewels, int x, int y) {
        final int three = 3;

        if (y < jewels[0].length - 2) {
            if (y < jewels[0].length - three && jewels[x][y + three].getType()
                    == jewels[x][y].getType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks the right-top completion option for a valid move.
     *
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkRightTop(Jewel[][] jewels, int x, int y) {
        if (y < jewels[0].length - 2) {

            if (x > 0 && jewels[x - 1][y + 2].getType() == jewels[x][y].getType()) {
                return true;
            }

        }

        return false;
    }

    /**
     * Checks the right-top completion option for a valid move.
     *
     * @param jewels Grid of jewels
     * @param x x-position of the start of the row to be completed
     * @param y y-position of the start of the row to be completed
     * @return true if the option is possible, otherwise false
     */
    private boolean checkRightBottom(Jewel[][] jewels, int x, int y) {
        if (y < jewels[0].length - 2) {
            if (x < jewels.length - 1 && jewels[x + 1][y + 2].getType()
                    == jewels[x][y].getType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a pair of similar jewels can be extended by a valid move (done for a vertical pair
     * but can be used for horizontal if jewels matrix is transposed).
     * @param jewels The grid containing the Jewels
     * @param x x-position of the first jewel of the pair to be extended
     * @param y y-position of the first jewel of the pair to be extended
     * @return a list of the coordinates of the Jewels (x1,y1,x2,y2)
     */
    private List<Integer> validMove(Jewel[][] jewels, int x, int y) {
        final int three = 3;
        if (checkLeft(jewels, x, y)) {
            return Arrays.asList(x, y - 1, x, y - 2);
        }
        if (checkLeftTop(jewels, x, y)) {
            return Arrays.asList(x, y - 1, x - 1, y - 1);
        }
        if (checkLeftBottom(jewels, x, y)) {
            return Arrays.asList(x, y - 1, x + 1, y - 1);
        }
        if (checkRight(jewels, x, y)) {
            return Arrays.asList(x, y + 2, x, y + three);
        }
        if (checkRightTop(jewels, x, y)) {
            return Arrays.asList(x, y + 2, x - 1, y + 2);
        }
        if (checkRightBottom(jewels, x, y)) {
            return Arrays.asList(x, y + 2, x + 1, y + 2);
        }
        return new ArrayList<Integer>();
    }

    /**
     * Checks a horizontal pair for a valid Move.
     * @param x x-position of the first jewel of the horizontal pair
     * @param y y-position of first jewel of the horizontal pair
     * @return a list of the two Jewels to be swapped for a valid move
     */
    private List<Jewel> horizontalRow(int x, int y) {
        /**
         * Create transpose of matrix
         */
        Jewel[][] transposed = new Jewel[grid[0].length][grid.length];
        for (int i = 0; i < grid[0].length; i++) {
            for (int j = 0; j < grid.length; j++) {
                transposed[i][j] = grid[j][i];
            }
        }

        final int three = 3;
        List<Integer> indices = validMove(transposed, y, x);
        if (!indices.isEmpty()) {
            return Arrays.asList(grid[indices.get(1)][indices.get(0)],
                    grid[indices.get(three)][indices.get(2)]);
        }
        return new ArrayList<Jewel>();
    }

    /**
     * Checks a possible horizontal row (xox) for a valid move.
     * @param x x-position of first jewel of possible row
     * @param y y-position of first jewel of possible row
     */
    private List<Jewel> horizontalRowPossible(int x, int y) {
        if (y > 0 && grid[x + 1][y - 1].getType() == grid[x][y].getType()) {

            return Arrays.asList(grid[x + 1][y], grid[x + 1][y - 1]);
        }
        if (y < grid[0].length - 1 && grid[x + 1][y + 1].getType() == grid[x][y].getType()) {
            return Arrays.asList(grid[x + 1][y], grid[x + 1][y + 1]);
        }

        return new ArrayList<Jewel>();
    }

    /**
     * Checks a possible vertical row (xox) for a valid move.
     * @param x x-position of first jewel of possible row
     * @param y y-position of first jewel of possible row
     */
    private List<Jewel> verticalRowPossible(int x, int y) {
        if (x > 0 && grid[x - 1][y + 1].getType() == grid[x][y].getType()) {
            return Arrays.asList(grid[x][y + 1], grid[x - 1][y + 1]);
        }
        if (x < grid.length - 1 && grid[x + 1][y + 1].getType() == grid[x][y].getType()) {
            return Arrays.asList(grid[x][y + 1], grid[x + 1][y + 1]);
        }
        return new ArrayList<Jewel>();
    }
}
