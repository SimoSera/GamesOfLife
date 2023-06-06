package com.simosera.gamesoflife;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

@FunctionalInterface
public interface GetShapeFunction {
    Shape apply( int i, int j,double width,double height, Color color);
}
