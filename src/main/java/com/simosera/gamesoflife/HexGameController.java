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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.ExecutorService;
import java.util.random.RandomGenerator;

import static java.lang.Math.min;

public class HexGameController extends GameController{



    public void initializeAll(){
        this.rules=new HexCell();  //to replace in the future maybe
        started=false;
        game=new Game(height,width,rules);
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

        cellWidth=1000/(double)width;
        cellHeight=500/(double)height;
        updateMatrix();
    }

    @FXML
    public void clickAddCell(MouseEvent event) {
        int i,j;
        i=(int)(event.getY()/cellHeight);
        j=(int) ((event.getX()/cellWidth)-i%2*0.5); //to modify
        Cell c=rules.getDefault();
        c.setLive(!game.getCellFromIndex(i,j).isLive());
        game.setCellAtIndex(i, j, c);
        game.countNeighbours();
        updateMatrix();
    }

    public void updateMatrix(){
        matrixPane.getChildren().clear();
        double r = cellHeight/2* 1.28; // the inner radius from hexagon center to outer corner
        double n = r; // the inner radius from hexagon center to middle of the axis
        for(int i=0;i< game.getHeight();i++){
            for(int j=0;j<game.getWidth();j++){
                Polygon poly;
                poly=new Polygon();
                double x =j* 2 * n + (i % 2) * n ;
                double y=i * 2 * r * 0.75;
                poly.getPoints().addAll(x, y,
                        x, y + r,
                        x + n, y + r * 1.5,
                        x + 2 * n, y + r,
                        x + 2 * r, y,
                        x + n, y - r * 0.5);
                if(game.getCellFromIndex(i,j).isLive()){
                    poly.setFill(colors[min(3,game.getCellFromIndex(i,j).getNeighboursCount())]);
                }else{
                    poly.setFill(Color.WHITE);
                }
                poly.setStroke(Color.GREY);
                matrixPane.getChildren().add(poly);
            }
        }
    }





}
