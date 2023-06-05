package com.simosera.gamesoflife;

import java.util.ArrayList;
import java.util.List;


public class Rule {
    String name;
    int overpopulation;
    int underpopulation;
    int reliveMinimum;
    int reliveMaximum;
    List<Coordinate> neighbours;

    public Rule(String name, int overpopulation, int underpopulation, int reliveMinimum, int reliveMaximum, List<Coordinate> neighbours) {
        this.name = name;
        this.overpopulation = overpopulation;
        this.underpopulation = underpopulation;
        this.reliveMinimum = reliveMinimum;
        this.reliveMaximum = reliveMaximum;
        this.neighbours = new ArrayList<>(neighbours);
    }

    public Rule() {
        name="";
        overpopulation=0;
        underpopulation=0;
        reliveMaximum=0;
        reliveMinimum=0;
        neighbours=new ArrayList<>();
    }

    public boolean dataCheck(){
        if(reliveMinimum>reliveMaximum ||
                overpopulation<underpopulation ||
                overpopulation>neighbours.size())
            return false;
        return true;
    }

    public void setName(String s){
        name=s;
    }

    public String getName() {
        return name;
    }

    public int getOverpopulation() {
        return overpopulation;
    }

    public void setOverpopulation(int overpopulation) {
        this.overpopulation = overpopulation;
    }

    public int getUnderpopulation() {
        return underpopulation;
    }

    public void setUnderpopulation(int underpopulation) {
        this.underpopulation = underpopulation;
    }

    public int getReliveMinimum() {
        return reliveMinimum;
    }

    public void setReliveMinimum(int reliveMinimum) {
        this.reliveMinimum = reliveMinimum;
    }

    public int getReliveMaximum() {
        return reliveMaximum;
    }

    public void setReliveMaximum(int reliveMaximum) {
        this.reliveMaximum = reliveMaximum;
    }

    public List<Coordinate> getNeighbours() {
        return neighbours;
    }
    public void addNeighbour(Coordinate n) {
        neighbours.add(n);
    }
    public void setNeighbours(List<Coordinate> neighbours) {
        this.neighbours = neighbours;
    }

    public static Rule getConwayRule(){
        return new Rule("Conway",4,1,3,3,
                List.of(new Coordinate(-1,-1),
                new Coordinate(-1,0),new Coordinate(-1,1),
                new Coordinate(0,-1),new Coordinate(0,1),
                new Coordinate(1,-1),new Coordinate(1,0),
                new Coordinate(1,1)));
    }

}
