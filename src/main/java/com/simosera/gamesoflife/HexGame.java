package com.simosera.gamesoflife;

/**
 * Hexagonal version of {@link Game}, the main
 * difference is that hexagonal cells have
 * different neighbours when they are on
 * odd and even rows of the matrix
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class HexGame extends Game {

    /**
     *
     * Initialize a new {@link HexGame}
     * @param height number of rows
     * @param width number of cells per row
     * @param evenRule {@link Rule} that all the cells in an even row will have,
     *                 the cells in an odd row will have the odd version of this rule
     */
    public HexGame(int height, int width, Rule evenRule) {
        super(height, width, evenRule);
        Rule oddRule = evenRule.getHexOddRuleFromEven();
        for(int row = 1; row < height; row += 2){
            for(int column = 0; column < width; column++){
                cells[row][column].setRule(oddRule);
            }
        }
    }

    /**
     * Sets the {@link Cell} at in position row, column
     * to the {@link Cell} passed by parameter.
     * The difference between this and the super method is that
     * this sets a different {@link Rule} based on if the row is even or odd
     * @param row Row where the {@link Cell} will be set
     * @param column    Column where the {@link Cell} will be set
     * @param cell  {@link Cell} that will replace the current {@link Cell}
     */
    public void setCellAtIndex(int row, int column, Cell cell) {
        cells[row][column] = cell;
        if(row % 2 == 1)
            cells[row][column].setRule(cell.getRule().getHexOddRuleFromEven());

    }
}
