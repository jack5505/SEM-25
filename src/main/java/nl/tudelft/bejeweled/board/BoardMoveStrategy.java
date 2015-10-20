package nl.tudelft.bejeweled.board;

import nl.tudelft.bejeweled.sprite.Jewel;

import java.util.List;

/**
 * Created by Jeroen on 20-10-2015.
 * Interface for Stategy Pattern.
 */
public interface BoardMoveStrategy {

    /**
     * Function that checks if there are any moves possible.
     *
     * <p>Iterates through all gems and looks for pairs or two or
     * constructions like "xox" where another x could fill in.
     * For each case a different function is called which checks for
     * a valid move.</p>
     *
     * @return returns two jewels in a list to swap if move is possible.
     */
    List<Jewel> getValidMovePair();
}
