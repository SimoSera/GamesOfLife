package com.simosera.gamesoflife;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * Hexagonal version of the GameController
 * It has methods specific for drawing hexagons
 * instead of squares, and it creates an HexGame instance
 * instead of a Game one
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class HexGameController extends GameController {

    /**
     * Initializes some data that depends on the {@link Shape}
     * of the game. This one is for Hexagonal version
     */
    public void initShapeData() {
        availableRules.put(Rule.getConwayHexRule().getName(), Rule.getConwayHexRule());
        cellHeight = matrixPane.getPrefHeight() * (4.0 / 3) / (double) numberOfRows;
        cellWidth = cellHeight * 0.866;
        cellsPerRow = (int) (matrixPane.getPrefWidth() / cellWidth);
    }

    /**
     * Initializes the game.
     * In this case it starts a {@link HexGame} that is a child
     * class of {@link Game}.
     */
    @FXML
    public void initGame(){
        game = new HexGame(numberOfRows, cellsPerRow, selectedRule);
    }

    /**
     * Returns the {@link Shape} to draw after instancing it
     * based on the parameters.
     * This is for the hexagonal game so the {@link Shape}
     * is upcast from a  {@link Polygon}.
     * @param i the y position of hte {@link Shape} to draw
     * @param j the x position of hte {@link Shape} to draw
     * @param width the width of the {@link Shape} to draw
     * @param height the height of the {@link Shape} to draw
     * @param color  the {@link Color} of the {@link Shape} to draw
     * @return Returns the {@link Shape} for the caller to add in the {@link Node}
     */
    public Shape getShape(int i, int j, double width, double height, Color color) {
        double r = height / 2; // the inner radius from hexagon center to outer corner
        double n = width / 2; // apothem of the hexagon
        Polygon hexagon;
        hexagon = new Polygon();
        double x = j*2 * n + (i % 2) * n ;
        double y = i*2 * r * 0.75;
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





}
