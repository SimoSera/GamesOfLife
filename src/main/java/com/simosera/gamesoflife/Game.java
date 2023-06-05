package com.simosera.gamesoflife;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that defines the basics of game of life
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
                this.cells[i][j] =cells[i][j];
            }
        }
        countNeighbours();
    }

    public Game(int height, int width, Rule rule){
        this.height=height;
        this.width=width;
        this.cells=new Cell[height][width];
        for(int i=0;i<height;i++) {
            for (int j = 0; j < width; j++) {
                this.cells[i][j] = new Cell(rule);
            }
        }
        countNeighbours();
    }

    /**
     *    Method that computes the next step in the game of life using the ruleConsumer
     */
    public void nextStep(){
        updateCellsLiveState();
        countNeighbours();
    }

    private void updateCellsLiveState(){
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                cells[i][j].applyRules();
    }
    /**
     * Method that counts the neighbours of each cell
     */
    public void countNeighbours(){
        int sum;
        int y;
        int x;
        for(int i=0;i<cells.length;i++){
            for(int j=0;j<cells[i].length;j++){
                sum=0;
                for(Coordinate e : cells[i][j].getRule().getNeighbours()){
                    y=(i+e.y+height)%height;
                    x=(j+e.x+width)%width;
                    sum+=cells[y][x].isLive() ? 1 : 0 ;
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

    public Cell getCellFromIndex(int i, int j){
        return cells[i][j];
    }

    public void setCellAtIndex(int i, int j, Cell c){
        cells[i][j]=c;
    }

    public ExecutorService multiThreadNextStep(){
        updateCellsLiveState();
        return startExecutorsCountNeighbours();
    }
    /**
     * Method that counts the neighbours of each cell
     */
    private void countNeighboursRow(int i){
        int sum;
        int x;
        int y;
        for(int j=0;j<cells[i].length;j++){
            sum=0;
            for(Coordinate e : cells[i][j].getRule().getNeighbours()){
                y=(i+e.y+height)%height;
                x=(j+e.x+width)%width;
                sum+=cells[y][x].isLive() ? 1 : 0 ;
            }
            cells[i][j].setNeighboursCount(sum);
        }

    }

    public ExecutorService startExecutorsCountNeighbours(){
        int poolSize = Runtime.getRuntime().availableProcessors()*2;
        ExecutorService execs= Executors.newFixedThreadPool(poolSize);
        int rowsPerTask=height/poolSize;
        for (int i = 0; i < poolSize; i++) {
            int taskNumber = i;
            execs.submit(() -> {
                for(int j=0;j<rowsPerTask && (taskNumber*rowsPerTask+j)<height;j++){
                    countNeighboursRow(taskNumber*rowsPerTask+j);
                }
            });
        }
        execs.submit(() -> {
            for(int j=1;j<height%poolSize+1;j++){
                countNeighboursRow(height-j);
            }
        });
        execs.shutdown();
        return execs;
    }
}
