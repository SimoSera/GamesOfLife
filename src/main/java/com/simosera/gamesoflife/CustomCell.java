package com.simosera.gamesoflife;

import java.util.ArrayList;
import java.util.List;

public class CustomCell extends Cell{
    List<Coordinate> neighbours;
    int overpopulation;
    int underpopulation;
    int rebirth;


    public CustomCell(boolean live, List<Coordinate> neighbours, int overpopulation, int underpopulation, int rebirth) {
        super(live);
        this.neighbours=new ArrayList<>(neighbours);
        this.overpopulation = overpopulation;
        this.underpopulation = underpopulation;
        this.rebirth = rebirth;
    }

    public CustomCell(CustomCell c) {
        super(c);
        this.neighbours=new ArrayList<>(c.neighbours);
        this.overpopulation = c.overpopulation;
        this.underpopulation = c.underpopulation;
        this.rebirth = c.rebirth;
    }


    public List<Coordinate> neighboursRelativeCordsToCount(){
        return neighbours;
    }

    public void applyRules(){
        setLive((neighboursCount<overpopulation && neighboursCount>underpopulation) || neighboursCount==rebirth);
    }

    @Override
    public Cell getDefault() {
        return new ConwayCell();
    }
}
