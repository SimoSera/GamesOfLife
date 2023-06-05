package com.simosera.gamesoflife;

import java.util.List;

public abstract class AbstractCell {
    int neighboursCount;
    boolean live;
    Rule rule;


    public AbstractCell(boolean live,Rule rule) {
        this.live = live;
        neighboursCount=0;
        this.rule=rule;
    }


    public AbstractCell(Rule rule) {
        this.live=false;
        neighboursCount=0;
        rule=null;
    }

    public AbstractCell(AbstractCell c) {
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

    public abstract void applyRules();

}
