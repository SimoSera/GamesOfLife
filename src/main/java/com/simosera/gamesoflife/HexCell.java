package com.simosera.gamesoflife;

import java.util.ArrayList;
import java.util.List;

public class HexCell extends Cell{
    public HexCell(boolean live) {
        super(live);
    }

    public HexCell() {
        super();
    }

    public HexCell(HexCell c) {
        super(c);
    }

    public ArrayList<Coordinate> neighboursRelativeCordsToCount(){
        return new ArrayList<Coordinate>(List.of(new Coordinate(-1,-1),
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


}
