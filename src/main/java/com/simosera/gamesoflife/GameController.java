package com.simosera.gamesoflife;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.concurrent.ExecutorService;
import java.util.random.RandomGenerator;

import static java.lang.Math.min;

public class GameController {
    @FXML
    Label stepsPerSecondLbl;
    @FXML
     Pane matrixPane;

    @FXML
     ToggleButton playTButton;

    @FXML
     Button setBut;

    @FXML
     Slider speedSlider;

    @FXML
     Button randomGenerateButton;

    @FXML
     Slider densitySlider;

    @FXML
     Button clearButton;

     boolean started;
     Game game;
     int speedMs;
     double cellWidth;
     double cellHeight;
     int cellsPerRow;
     int numberOfRows;
     Color bgColor;
     Color cellColor;
     Cell rules;
     AnimationTimer timer;
     Color[] colors;
     long lastFrame;
     ExecutorService executorService;

    @FXML
    void playTButtonPressed() throws InterruptedException {
        if(started){
            stopGame();
        }else{
            startGame();
        }
    }

    public void initializeAll(){
        started=false;
        game=new Game(numberOfRows, cellsPerRow,rules);
        speedMs=(int)(1000/speedSlider.getValue());
        lastFrame=0;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime=now-lastFrame;
                if(elapsedTime/1000000>speedMs && (executorService==null || executorService.isTerminated())){
                    executorService=game.multiThreadNextStep();
                    updateMatrix();
                    lastFrame=now;
                    stepsPerSecondLbl.setText(String.valueOf(1000/(elapsedTime/1000000)));
                }
            }
        };

        cellWidth=1000/(double) cellsPerRow;
        cellHeight=500/(double) numberOfRows;
        updateMatrix();
    }

    @FXML
    public void clickAddCell(MouseEvent event) {
        int i,j;
        i=(int)(event.getY()/cellHeight);
        j=(int) (event.getX()/cellWidth);
        Cell c=rules.getDefault();
        c.setLive(!game.getCellFromIndex(i,j).isLive());
        game.setCellAtIndex(i, j, c);
        game.countNeighbours();
        updateMatrix();
    }
    @FXML
    public void speedSetter(){
        speedMs=(int)(1000/speedSlider.getValue());
    }

    @FXML
    public void randomCellsGenerator() {
        double density=densitySlider.getValue()/100;
        Cell c;
        RandomGenerator rnd=RandomGenerator.getDefault();
        for(int i = 0; i< numberOfRows; i++){
            for(int j = 0; j< cellsPerRow; j++){
                c=rules.getDefault();
                c.setLive(rnd.nextDouble() < density);
                game.setCellAtIndex(i,j,c);
            }
        }
        game.countNeighbours();
        updateMatrix();
    }

    @FXML
    public void resetGame(){
        stopGame();
        game=new Game(numberOfRows, cellsPerRow,rules.getDefault());
        updateMatrix();
    }

    public void updateMatrix(){
        matrixPane.getChildren().clear();
        for(int i=0;i< game.getHeight();i++){
            for(int j=0;j<game.getWidth();j++){
                Rectangle rect;
                if(game.getCellFromIndex(i,j).isLive()){
                    rect= new Rectangle(cellWidth,cellHeight,colors[min(3,game.getCellFromIndex(i,j).getNeighboursCount())]);

                }else{
                    rect= new Rectangle(cellWidth,cellHeight,Color.WHITE);
                }
                rect.setStroke(Color.GREY);
                rect.setX(j*cellWidth);
                rect.setY(i*cellHeight);
                matrixPane.getChildren().add(rect);
            }
        }
    }
    public void initData(int width, int height, Color cellColor, Color bgColor,int rules){
        this.cellsPerRow =width;
        this.numberOfRows =height;
        this.cellColor=cellColor;
        this.bgColor=bgColor;
        colors=new Color[5];
        colors[0]=Color.GREEN;
        colors[1]=Color.YELLOWGREEN;
        colors[2]=Color.ORANGE;
        colors[3]=Color.RED;
        this.rules=new ConwayCell();// to replace with actual rules int
        initializeAll();
    }


    public void startGame() {
        started=true;
        timer.start();
    }
    public void stopGame(){
        started=false;
        timer.stop();
    }

}
