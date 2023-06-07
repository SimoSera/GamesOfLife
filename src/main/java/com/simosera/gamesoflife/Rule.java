package com.simosera.gamesoflife;

import java.util.ArrayList;
import java.util.List;


/**
 * Rule for how the Cell has to count the neighbours
 * and also the parameters for the applyRules method
 * in Cell
 * It also defines some standard rules as static methods
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class Rule {

    List<Coordinate> neighbours;

    String name;

    int overpopulation;

    int underpopulation;

    /** For relive the cell have a range, reliveMinimum and ReliveMaximum
     * are respectively the lower value of the range and the higher one
     * so to relive the cell must have reliveMinimum<=neighboursCount<=reliveMaximum*/
    int reliveMinimum;

    int reliveMaximum;

    /**
     * Initialize a new Rule
     * @param name  name of the rule
     * @param overpopulation   maximum number of neighbours, when the cell will have
     *                         this number or more of neighbours the cell will die
     * @param underpopulation   minimum number of neighbours, when the cell will have
     *                          this number or less of neighbours the cell will die
     * @param reliveMinimum     minimum number of neighbour to let the cell come back to life
     * @param reliveMaximum     maximum number of neighbour to let the cell come back to life
     * @param neighbours   List of relative Coordinates of neighbour cells to count if live
     */
    public Rule(String name, int overpopulation, int underpopulation, int reliveMinimum, int reliveMaximum, List<Coordinate> neighbours) {
        this.name = name;
        this.overpopulation = overpopulation;
        this.underpopulation = underpopulation;
        this.reliveMinimum = reliveMinimum;
        this.reliveMaximum = reliveMaximum;
        this.neighbours = new ArrayList<>(neighbours);
    }

    /**
     * Initialize a new Rule
     * Default constructor
     * name = "";
     * overpopulation = 0;
     * underpopulation = 0;
     * reliveMaximum = 0;
     * reliveMinimum = 0;
     * neighbours = new ArrayList<>();
     */
    public Rule() {
        name = "";
        overpopulation = 0;
        underpopulation = 0;
        reliveMaximum = 0;
        reliveMinimum = 0;
        neighbours = new ArrayList<>();
    }

    /** Initialize a new Rule
     *  Copy constructor
     * @param rule Rule to copy values from
     */
    public Rule(Rule rule) {
        name=rule.name;
        overpopulation=rule.overpopulation;
        underpopulation=rule.underpopulation;
        reliveMaximum=rule.reliveMaximum;
        reliveMinimum=rule.reliveMinimum;
        neighbours=List.copyOf(rule.neighbours);
    }

    public void setName(String s) {
        name = s;
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
    public void removeNeighbour(Coordinate coordinate) {
        neighbours.removeIf(coordinate::equals);
    }
    public void setNeighbours(List<Coordinate> neighbours) {
        this.neighbours = neighbours;
    }

    public static Rule getConwayRule() {
        return new Rule("Conway", 4, 1, 3, 3,
                List.of(new Coordinate(-1, -1),
                new Coordinate(-1, 0), new Coordinate(-1, 1),
                new Coordinate(0,-1), new Coordinate(0, 1),
                new Coordinate(1, -1), new Coordinate(1, 0),
                new Coordinate(1, 1)));
    }

    public static Rule getNeumannRule() {
        return new Rule("von Neumann", 4, 1, 2, 2,
                List.of(new Coordinate(0, -1),
                        new Coordinate(0, 1), new Coordinate(-1, 0),
                        new Coordinate(1, 0)));
    }

    public static Rule getConwayHexRule() {
        return new Rule("Conway Hexagonal", 3, 1, 2, 2,
                List.of(new Coordinate(-1, -1),
                new Coordinate(0, -1), new Coordinate(-1, 0),
                new Coordinate(1, 0), new Coordinate(-1, 1),
                new Coordinate(0, 1)));
    }



    public  Rule getHexOddRuleFromEven() {
         Rule rule = new Rule(this);
         rule.setNeighbours(new ArrayList<>());
         this.neighbours.forEach(c->{
             if(c.y % 2 != 0)
                 rule.addNeighbour(new Coordinate(c.x  + 1, c.y));
             else
                 rule.addNeighbour(new Coordinate(c.x, c.y));
         });
         return rule;
    }
    public  Rule getHexEvenRuleFromOdd() {
        Rule rule= new Rule(this);
        rule.setNeighbours(new ArrayList<>());
        this.neighbours.forEach(c->{
            if(c.y % 2 != 0)
                rule.addNeighbour(new Coordinate(c.x - 1, c.y));
            else
                rule.addNeighbour(new Coordinate(c.x, c.y));
        });
        return rule;
    }
    public boolean isNeighbour(Coordinate coordinate) {
        return neighbours.stream().anyMatch(coordinate::equals);
    }


}
