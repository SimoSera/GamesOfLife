package com.simosera.gamesoflife;

import javafx.util.Pair;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Abstract class that defines the basics of game of life
 * This class doesn't implement any version of game of life
 */
public class Game {
    int width;
    int height;
    Cell[][] cells;

    /**
     *  Constructor for the Game class
     * @param height number of cells in the y-axis
     * @param width  number of cells in the x-axis
     * @param cells cells bi dimensional array (2D array)
     */
    public Game(int height, int width, Cell[][] cells){
        this.height=height;
        this.width=width;
        this.cells=new Cell[height][width];
        for(int i=0;i<height;i++) {
            for (int j = 0; j < width; j++) {
                this.cells[i][j] = new Cell(cells[i][j]);
            }
        }
    }

    public Game(int height, int width){
        this.height=height;
        this.width=width;
        this.cells=new Cell[height][width];
        for(int i=0;i<height;i++) {
            for (int j = 0; j < width; j++) {
                this.cells[i][j] = new Cell(false);
            }
        }
    }

    /**
     *    Method that computes the next step in the game of life using the ruleConsumer
     */
    public void nextStep(){
        countNeighbours();
        updateCellsLiveState();
    }

    private void updateCellsLiveState(){
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                cells[i][j].applyRules();
    }
    /**
     * Method that counts the neighbours of each cell
     */
    private void countNeighbours(){
        int sum;
        for(int i=0;i<cells.length;i++){
            for(int j=0;j<cells[i].length;j++){
                sum=0;
                for(Coordinate e : cells[i][j].neighboursRelativeCoordsToCount()){
                    sum+=cells[i+e.x][j+e.y].isLive() ? 1 : 0 ;
                }
                cells[i][j].setNeighboursCount(sum);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Cell getCellFromIndex(int i,int j){
        return cells[i][j];
    }

}
