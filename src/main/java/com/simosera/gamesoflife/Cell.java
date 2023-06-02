package com.simosera.gamesoflife;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    int neighboursCount;
    boolean live;

    public Cell(boolean live) {
        this.live = live;
        neighboursCount=0;
    }

    public Cell() {
        this.live=true;
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

    public ArrayList<Coordinate> neighboursRelativeCordsToCount(){
        return new ArrayList<Coordinate>(List.of(new Coordinate(-1,-1),
                new Coordinate(-1,0),new Coordinate(-1,1),
                new Coordinate(0,-1),new Coordinate(0,1),
                new Coordinate(1,-1),new Coordinate(1,0),
                new Coordinate(1,1)));
    }

    public void applyRules(){
        setLive(getNeighboursCount()==3 || (getNeighboursCount()==2 && isLive()));
    }

}
