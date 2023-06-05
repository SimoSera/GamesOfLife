package com.simosera.gamesoflife;

import java.util.ArrayList;
import java.util.List;

public class Cell extends AbstractCell{


    public Cell(boolean live, Rule rule) {
        super(live, rule);
    }

    public Cell(Rule rule) {
        super(rule);
    }

    public Cell(AbstractCell c) {
        super(c);
    }

    public void applyRules(){
        setLive((neighboursCount<rule.overpopulation && neighboursCount>rule.underpopulation) || neighboursCount>=rule.reliveMinimum && neighboursCount<=rule.reliveMaximum);
    }
}
