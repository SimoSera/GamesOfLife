package com.simosera.gamesoflife;

import java.util.Objects;

/**
 * Coordinate is used to describe the
 * relative position of the neighbouring cells
 * in the Rule class
 * @see Rule
 * @author Simone Serafini
 * @version 2023.06.07
 */
public class Coordinate {

    int x;

    int y;

    /**
     * Initialize a new Coordinate
     * @param x the x position (horizontal)
     * @param y the y position (vertical)
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Initialize a new Coordinate
     * Default constructor, x=0 and y=0
     */
    public Coordinate() {
        x = 0;
        y = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Equals method for comparing two Coordinates by the values.
     * @param o Coordinate object to compare with this
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
