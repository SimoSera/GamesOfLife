package com.simosera.gamesoflife;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

import java.util.Arrays;

public class GameController {

    @FXML
    private Pane matrixPane;

    @FXML
    private ToggleButton playTButton;

    @FXML
    private Button setBut;

    @FXML
    private Slider speedSlider;

    private boolean started;
    private Game game;
    private int speedMs;
    private double cellWidth;
    private double cellHeight;
    private int width;
    private int height;
    private String cellShape;
    private String rules;
    private Color bgColor;
    private Color cellColor;
    private AnimationTimer timer;

    @FXML
    void playTButtonPressed(ActionEvent event) throws InterruptedException {
        if(started){
            stopGame();
            started=false;
        }else{
            startGame();
            started=true;
        }
    }


    public void initializeAll(){
        started=false;

        Cell c=new Cell(true);
        game=new Game(height,width);
        game.setCellAtIndex(11,11,c);
        game.setCellAtIndex(11,10,c);
        game.setCellAtIndex(11,9,c);
        speedMs=(int)(speedSlider.getValue()/speedSlider.getMax()*10);
        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                game.multiThreadNextStep();updateMatrix();
            }
        };
        cellWidth=1000/(double)width;
        cellHeight=500/(double)height;
        System.out.println(cellWidth+ "cell"+matrixPane.getWidth());
        initializeMatrix();
        updateMatrix();
    }

    public void initializeMatrix(){
        matrixPane.getChildren().clear();
        for(int i=0;i< game.getHeight();i++){
            for(int j=0;j<game.getWidth();j++){
                Rectangle rect= new Rectangle(cellWidth,cellHeight,Color.WHITE);
               // rect.setStrokeType(StrokeType.OUTSIDE);
                rect.setStroke(Color.BLACK);
                rect.setX(j*cellWidth);
                rect.setY(i*cellHeight);
                matrixPane.getChildren().add(rect);
            }
        }
    }

    public void speedSetter(ActionEvent e){
        speedMs=(int)(speedSlider.getValue()/speedSlider.getMax()*10);
    }


    public void updateMatrix(){
        matrixPane.getChildren().clear();
        for(int i=0;i< game.getHeight();i++){
            for(int j=0;j<game.getWidth();j++){
                Rectangle rect;
                if(game.getCellFromIndex(i,j).isLive()){
                    rect= new Rectangle(cellWidth,cellHeight,Color.BLUE);

                }else{
                    rect= new Rectangle(cellWidth,cellHeight,Color.WHITE);
                }
                rect.setStroke(Color.BLACK);
                rect.setX(j*cellWidth);
                rect.setY(i*cellHeight);
                matrixPane.getChildren().add(rect);
            }
        }
    }

    public void initData(int width, int height, Color cellColor, Color bgColor,String cellShape,String rules){
        this.width=width;
        System.out.println(width);
        this.height=height;
        this.cellColor=cellColor;
        this.bgColor=bgColor;
        this.rules=rules;
        this.cellShape=cellShape;
        initializeAll();
    }


    public void startGame() throws InterruptedException {
        timer.start();
    }
    public void stopGame(){
        timer.stop();
    }


}
