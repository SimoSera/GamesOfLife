package com.simosera.gamesoflife;

import java.util.List;

public class ConwayCell extends Cell{
    public ConwayCell(boolean live) {
        super(live);
    }

    public ConwayCell() {
    }

    public ConwayCell(Cell c) {
        super(c);
    }

    @Override
    public List<Coordinate> neighboursRelativeCordsToCount() {
        return List.of(new Coordinate(-1,-1),
                new Coordinate(-1,0),new Coordinate(-1,1),
                new Coordinate(0,-1),new Coordinate(0,1),
                new Coordinate(1,-1),new Coordinate(1,0),
                new Coordinate(1,1));
    }

    @Override
    public void applyRules() {
        setLive(getNeighboursCount()==3 || (getNeighboursCount()==2 && isLive()));
    }

    @Override
    public Cell getDefault() {
        return new ConwayCell();
    }
}
