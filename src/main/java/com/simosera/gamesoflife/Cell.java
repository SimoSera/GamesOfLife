package com.simosera.gamesoflife;

/**
 * Cell of the game of life, it applies the rules
 * defined in the {@link Rule} rule based on the live
 * state and the number of neighbours
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class Cell {

    Rule rule;

    int neighboursCount;

    /** live=true ---> cell is alive   live=false ---> cell is dead*/
    boolean live;


    /**
     * Initialize a new {@link Cell}
     * @param live true if cell is live, false if dead
     * @param rule {@link Rule} that this cell will follow
     * @see Rule
     */
    public Cell(boolean live, Rule rule) {
        this.live = live;
        neighboursCount = 0;
        this.rule = rule;
    }

    /**
     * Initialize a new {@link Cell} as dead
     * @param rule {@link Rule} that this cell follows
     * @see Rule
     */
    public Cell(Rule rule) {
        this.live = false;
        neighboursCount = 0;
        this.rule = rule;
    }

    /**
     * Initialize a new {@link Cell}
     * Copy constructor
     * @param cell {@link Cell} to copy values from
     */
    public Cell(Cell cell) {
        this.live = cell.live;
        this.neighboursCount = cell.neighboursCount;
        this.rule = cell.rule;
    }

    public int getNeighboursCount() {
        return neighboursCount;
    }

    public void setNeighboursCount(int neighboursCount) {
        this.neighboursCount = neighboursCount;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    /**
     * Apply the rules described in this.rule based on the number of neighbours (neighboursCount).
     * @see Rule
     */
    public void applyRules() {
        setLive( (this.live && neighboursCount<rule.overpopulation && neighboursCount>rule.underpopulation) || ( (!this.live) && neighboursCount>=rule.reliveMinimum && neighboursCount<=rule.reliveMaximum) );
    }
}
