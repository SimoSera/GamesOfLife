package com.simosera.gamesoflife;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        countNeighbours();
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
    private void countNeighbours(){
        int sum;
        for(int i=0;i<cells.length;i++){
            for(int j=0;j<cells[i].length;j++){
                sum=0;
                for(Coordinate e : cells[i][j].neighboursRelativeCoordsToCount()){
                    if(e.y+i>=0 && e.y+i<height && e.x+j>=0 && e.x+j<width)
                        sum+=cells[i+e.y][j+e.x].isLive() ? 1 : 0 ;
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

    public void setCellAtIndex(int i,int j,Cell c){
        cells[i][j]=new Cell(c);
        countNeighbours();
    }

    public static void main(String[] args) {
        Cell c=new Cell(true);
        long startTime=System.nanoTime();
        Game game=new Game(100,100);


        game.setCellAtIndex(11,11,c);
        game.setCellAtIndex(11,10,c);
        game.setCellAtIndex(11,9,c);
        game.nextStep();
        game.nextStep();
        game.nextStep();
        long elapsedTime=System.nanoTime()-startTime;
        System.out.println("not threaded: "+elapsedTime/1000000+"ms");
        startTime=System.nanoTime();
        game.multiThreadNextStep();
        game.multiThreadNextStep();
        game.multiThreadNextStep();
        elapsedTime=System.nanoTime()-startTime;
        System.out.println("threaded: "+elapsedTime/1000000+"ms");
    }



    public void multiThreadNextStep(){
        int poolSize = Runtime.getRuntime().availableProcessors()*2;
        updateCellsLiveState();
        try{
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
            while(!execs.awaitTermination(10, TimeUnit.MILLISECONDS)){
            }
        }catch (InterruptedException e){
            System.out.println("DEBUG::: ERROR");
        }

    }
    /**
     * Method that counts the neighbours of each cell
     */
    private void countNeighboursRow(int i){
        int sum;
        for(int j=0;j<cells[i].length;j++){
            sum=0;
            for(Coordinate e : cells[i][j].neighboursRelativeCoordsToCount()){
                if(e.y+i>=0 && e.y+i<height && e.x+j>=0 && e.x+j<width)
                    sum+=cells[i+e.y][j+e.x].isLive() ? 1 : 0 ;
            }
            cells[i][j].setNeighboursCount(sum);
        }

    }
}
