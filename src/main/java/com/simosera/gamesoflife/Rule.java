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
    public Rule(Rule rule) {
        name=rule.name;
        overpopulation=rule.overpopulation;
        underpopulation=rule.underpopulation;
        reliveMaximum=rule.reliveMaximum;
        reliveMinimum=rule.reliveMinimum;
        neighbours=List.copyOf(rule.neighbours);
    }
    public boolean dataCheck(){
        return reliveMinimum <= reliveMaximum &&
                overpopulation >= underpopulation &&
                overpopulation <= neighbours.size();
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
    public void removeNeighbour(Coordinate coordinate){
        neighbours.removeIf(coordinate::equals);
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

    public static Rule getNeumannRule(){
        return new Rule("von Neumann",4,1,2,2,
                List.of(new Coordinate(0,-1),
                        new Coordinate(0,1),new Coordinate(-1,0),
                        new Coordinate(1,0)));
    }

    public static Rule getConwayHexRule(){
        return new Rule("Conway Hexagonal",3,1,2,2,
                List.of(new Coordinate(1,-1),
                new Coordinate(0,-1),new Coordinate(-1,0),
                new Coordinate(1,0),new Coordinate(1,1),
                new Coordinate(0,1)));
    }



    public  Rule getHexOddRuleFromEven(){
         Rule rule= new Rule(this);
         rule.setNeighbours(new ArrayList<>());
         this.neighbours.forEach(c->{
             if(c.y%2!=0)
                 rule.addNeighbour(new Coordinate(c.x+1,c.y));
             else
                 rule.addNeighbour(new Coordinate(c.x,c.y));
         });
         return rule;
    }
    public  Rule getHexEvenRuleFromOdd(){
        Rule rule= new Rule(this);
        rule.setNeighbours(new ArrayList<>());
        this.neighbours.forEach(c->{
            if(c.y%2!=0)
                rule.addNeighbour(new Coordinate(c.x-1,c.y));
            else
                rule.addNeighbour(new Coordinate(c.x,c.y));
        });
        return rule;
    }
    public boolean isNeighbour(Coordinate coordinate){
        return neighbours.stream().anyMatch(coordinate::equals);
    }


}
