package com.simosera.gamesoflife;

/**
 * Cell of the Game Of Life it applies the rules
 * defined in the rule object based on the live
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
     * Initialize a new Cell
     * @param live true if cell is live, false if dead
     * @param rule rule that this cell follows
     * @see Rule
     */
    public Cell(boolean live, Rule rule) {
        this.live = live;
        neighboursCount = 0;
        this.rule = rule;
    }

    /**
     * Initialize a new Cell as dead
     * @param rule rule that this cell follows
     * @see Rule
     */
    public Cell(Rule rule) {
        this.live = false;
        neighboursCount = 0;
        this.rule = rule;
    }

    /**
     * Initialize a new Cell
     * Copy constructor
     * @param cell cell to copy values from
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
     * Apply the rules described in rule based on the number of neighbours (neighboursCount).
     * @see Rule
     */
    public void applyRules() {
        setLive( (this.live && neighboursCount<rule.overpopulation && neighboursCount>rule.underpopulation) || ( (!this.live) && neighboursCount>=rule.reliveMinimum && neighboursCount<=rule.reliveMaximum) );
    }
}
