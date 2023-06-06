package com.simosera.gamesoflife;

public class Cell {
    int neighboursCount;
    boolean live;
    Rule rule;

    public Cell(boolean live, Rule rule) {
        this.live = live;
        neighboursCount=0;
        this.rule=rule;
    }

    public Cell(Rule rule) {
        this.live=false;
        neighboursCount=0;
        this.rule=rule;
    }

    public Cell(Cell c) {
        this.live=c.live;
        this.neighboursCount=c.neighboursCount;
        this.rule=c.rule;
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
    public void applyRules(){
        setLive((this.live && neighboursCount<rule.overpopulation && neighboursCount>rule.underpopulation) || ((!this.live)&& neighboursCount>=rule.reliveMinimum && neighboursCount<=rule.reliveMaximum));
    }
}
