package com.simosera.gamesoflife;


import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


import static java.lang.Math.min;

public class HexGameController extends GameController {
    public void initShapeData(){
        availableRules.put(Rule.getConwayHexRule().getName(), Rule.getConwayHexRule());
        cellHeight=matrixPane.getPrefHeight()*(4.0/3)/(double) numberOfRows;
        cellWidth = cellHeight*0.866;
        cellsPerRow=(int)(matrixPane.getPrefWidth()/cellWidth);
    }
    @FXML
    public void initGame(){
        game=new HexGame(numberOfRows, cellsPerRow,selectedRule);
    }
    public static Shape staticGetShape(int i, int j,double width,double height, Color color){
        double r = height/2; // the inner radius from hexagon center to outer corner
        double n = width/2; // the inner radius from hexagon center to middle of the axis
        Polygon hexagon;
        hexagon=new Polygon();
        double x =j* 2 * n + (i % 2) * n ;
        double y=i * 2 * r * 0.75;
        hexagon.getPoints().addAll(x, y,
                x, y + r,
                x + n, y + r * 1.5,
                x + 2 * n, y + r,
                x + 2 * n, y,
                x + n, y - r * 0.5);
        hexagon.setFill(color);
        hexagon.setStroke(Color.GREY);
        return hexagon;
    }

    public Shape getShape(int i, int j,double width,double height, Color color){
        return staticGetShape(i,j,width,height,color);
    }





}
