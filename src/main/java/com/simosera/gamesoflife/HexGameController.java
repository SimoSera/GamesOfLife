package com.simosera.gamesoflife;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.random.RandomGenerator;

import static java.lang.Math.min;

public class HexGameController extends GameController{


    public void initializeAll(){
        this.rules=new HexCell();  //to replace in the future maybe
        started=false;
        game=new HexGame(numberOfRows, cellsPerRow,rules);
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
        cellHeight=matrixPane.getPrefHeight()*(4.0/3)/(double) numberOfRows;
        cellWidth = cellHeight*0.866;
        updateMatrix();
    }

    @FXML
    public void clickAddCell(MouseEvent event) {
        int i,j;
        i=(int)(event.getY()/matrixPane.getPrefHeight()*numberOfRows);
        j=(int) ((event.getX()/(matrixPane.getPrefWidth()-cellWidth/2)*cellsPerRow)-(i%2*0.5)); //to modify
        HexCell c=(HexCell) rules.getDefault();
        c.setRowIsEven(i%2==0);
        c.setLive(!game.getCellFromIndex(i,j).isLive());
        game.setCellAtIndex(i, j, c);
        game.countNeighbours();
        updateMatrix();
    }

    public void updateMatrix(){
        matrixPane.getChildren().clear();
        double r = cellHeight/2; // the inner radius from hexagon center to outer corner
        double n = cellWidth/2; // the inner radius from hexagon center to middle of the axis
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
                        x + 2 * n, y,
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

    @FXML
    public void randomCellsGenerator() {
        double density=densitySlider.getValue()/100;
        HexCell c;
        RandomGenerator rnd=RandomGenerator.getDefault();
        for(int i = 0; i< numberOfRows; i++){
            for(int j = 0; j< cellsPerRow; j++){
                c=(HexCell) rules.getDefault();
                c.setRowIsEven(i%2==0);
                c.setLive(rnd.nextDouble() < density);
                game.setCellAtIndex(i,j,c);
            }
        }
        game.countNeighbours();
        updateMatrix();
    }



}
