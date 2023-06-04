package com.simosera.gamesoflife;

import java.util.ArrayList;
import java.util.List;

public class HexCell extends Cell{
    boolean rowIsEven; //true=the row it's in is even false=the row it's on is odd
    public HexCell(boolean live,boolean rowIsEven) {
        super(live);
        this.rowIsEven=rowIsEven;
    }

    public HexCell() {
        super();
    }

    public HexCell(HexCell c) {
        super(c);
        this.rowIsEven=c.rowIsEven;
    }

    public ArrayList<Coordinate> neighboursRelativeCordsToCount(){
        return rowIsEven ? new ArrayList<Coordinate>(List.of(new Coordinate(1,-1),
                new Coordinate(0,-1),new Coordinate(-1,0),
                new Coordinate(1,0),new Coordinate(-1,1),
                new Coordinate(0,1)))
                :
                new ArrayList<Coordinate>(List.of(new Coordinate(1,-1),
                        new Coordinate(0,-1),new Coordinate(-1,0),
                        new Coordinate(1,0),new Coordinate(-1,1),
                        new Coordinate(0,1)));
    }

    @Override
    public void applyRules() {
        setLive(getNeighboursCount()==3 || (getNeighboursCount()==2 && isLive()));
    }

    @Override
    public Cell getDefault() {
        return new HexCell();
    }

    public boolean isRowIsEven() {
        return rowIsEven;
    }

    public void setRowIsEven(boolean rowIsEven) {
        this.rowIsEven = rowIsEven;
    }
}
