package com.simosera.gamesoflife;

import java.util.ArrayList;
import java.util.List;

public abstract class Cell {
    int neighboursCount;
    boolean live;

    public Cell(boolean live) {
        this.live = live;
        neighboursCount=0;
    }

    public Cell() {
        this.live=false;
        neighboursCount=0;
    }

    public Cell(Cell c) {
        this.live=c.live;
        neighboursCount=c.neighboursCount;
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

    public abstract List<Coordinate> neighboursRelativeCordsToCount();

    public abstract void applyRules();

    public abstract Cell getDefault();

}
