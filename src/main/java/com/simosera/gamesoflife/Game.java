package com.simosera.gamesoflife;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Defines the general concept of
 * game of life: a matrix of {@link Cell}.
 * It counts the neighbours of each live and dead
 * {@link Cell} and applies the rules of each cell
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class Game {

    int width;

    int height;

    Cell[][] cells;

    /**
     * Initialize a new {@link Game}
     * @param height number of rows
     * @param width number of cells per row
     * @param defaultRule {@link Rule that all the cells will have
     */
    public Game(int height, int width, Rule defaultRule) {
        this.height = height;
        this.width = width;
        this.cells = new Cell[height][width];
        for(int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                this.cells[row][column] = new Cell(defaultRule);
            }
        }
        countNeighbours();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCellFromIndex(int row, int column){
        return cells[row][column];
    }

    public void setCellAtIndex(int row, int column, Cell c){
        cells[row][column]=c;
    }


    /**
     * Updates all the cells live state by applying the rules for each cell.
     * This method should be called after having called countNeighbours().
     */
    private void updateCellsLiveState() {
        for(int row = 0; row < height; row++)
            for(int column = 0; column < width; column++)
                cells[row][column].applyRules();
    }

    /**
     * Counts the live neighbours of each cell using the coordinates defined in the rule of each cell.
     */
    public void countNeighbours() {
        int sum;
        int y;
        int x;
        for(int row = 0; row < height; row++){
            for(int column = 0; column < width; column++){
                sum = 0;
                for(Coordinate e : cells[row][column].getRule().getNeighbours()){
                    y = (row + e.y + height) % height;
                    x = (column + e.x + width) % width;
                    sum += cells[y][x].isLive() ? 1 : 0 ;
                }
                cells[row][column].setNeighboursCount(sum);
            }
        }
    }

    /**
     * Updates the live state and counts the neighbours using a multi thread method
     * to have better performance.
     * @return  ExecutorService of the multi thread method so that whatever calls
     *          this method can check when the tasks have finished
     * @see ExecutorService
     */
    public ExecutorService multiThreadNextStep() {
        updateCellsLiveState();
        return startExecutorsCountNeighbours();
    }

    /**
     * Creates an ExecutorService and submits some tasks based on the
     * capability of the device, each task will count the neighbours
     * of one or more rows of cells.
     * @return ExecutorService so that whatever calls
     *          this method can check when the tasks have finished
     * @see ExecutorService
     */
    public ExecutorService startExecutorsCountNeighbours() {
        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService execs = Executors.newFixedThreadPool(poolSize);
        int rowsPerTask = height / poolSize;
        for (int i = 0; i < poolSize; i++) {
            int taskNumber = i;
            execs.submit(() -> {
                for(int j = 0; j < rowsPerTask && (taskNumber * rowsPerTask + j) < height; j++){
                    countNeighboursRow(taskNumber * rowsPerTask + j);
                }
            });
        }
        execs.submit(() -> {
            for(int j = 1; j < height % poolSize + 1; j++){
                countNeighboursRow(height - j);
            }
        });
        execs.shutdown();
        return execs;
    }

    /**
     * Counts the number of live neighbours for each cell in one row.
     */
    private void countNeighboursRow(int row) {
        int sum;
        int x;
        int y;
        for(int column = 0; column < width; column++){
            sum = 0;
            for(Coordinate e : cells[row][column].getRule().getNeighbours()){
                y = (row + e.y + height) % height;
                x = (column + e.x + width) % width;
                sum += cells[y][x].isLive() ? 1 : 0 ;
            }
            cells[row][column].setNeighboursCount(sum);
        }

    }
}
