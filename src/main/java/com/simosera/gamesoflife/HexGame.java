package com.simosera.gamesoflife;

/**
 * Hexagonal version of Game, the main
 * difference is that hexagonal cells have
 * different neighbours when they are on
 * odd and even rows of the matrix
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class HexGame extends Game {

    /**
     *
     * Initialize a new Game
     * @param height number of rows
     * @param width number of cells per row
     * @param evenRule rule that all the cells in an even row will have,
     *                 the cells in an odd row will have the odd version of this rule
     */
    public HexGame(int height, int width, Rule evenRule) {
        super(height, width, evenRule);
        Rule oddRule = evenRule.getHexOddRuleFromEven();
        for(int i = 1; i < height; i += 2){
            for(int j = 0; j < width; j++){
                cells[i][j].setRule(oddRule);
            }
        }
    }

    public void setCellAtIndex(int i, int j, Cell c) {
        cells[i][j] = c;
        if(i % 2 == 1)
            cells[i][j].setRule(c.getRule().getHexOddRuleFromEven());

    }
}
