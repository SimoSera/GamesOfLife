package com.simosera.gamesoflife;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * Functional Interface for the getShape function of the
 * {@link GameController} and {@link HexGameController}.
 * Used to pass the method to the {@link CustomRuleController}
 */
@FunctionalInterface
public interface GetShapeFunction {

    /**
     * Apply method required by the {@link FunctionalInterface} Interface
     * @param y parameter of the getShape method in {@link GameController}
     * @param x parameter of the getShape method in {@link GameController}
     * @param width parameter of the getShape method in {@link GameController}
     * @param height parameter of the getShape method in {@link GameController}
     * @param color parameter of the getShape method in {@link GameController}
     * @return value of the getShape method in {@link GameController}
     * @see GameController, {@link HexGameController}
     */
    Shape apply( int y, int x,double width,double height, Color color);
}
