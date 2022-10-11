package org.talon540.math;

public class Vector2d {
    public double vecX;
    public double vecY;

    public Vector2d(double vecX, double vecY) {
        this.vecX = vecX;
        this.vecY = vecY;
    }

    /**
     * Contruct a new vector by adding a vector to the current vector
     * 
     * @param vector
     */
    public Vector2d addVector(Vector2d vector) {
        return new Vector2d(
                vecX + vector.vecX,
                vecY + vector.vecY);
    }

    /**
     * Contruct a new vector by subtracting a vector to the current vector
     * 
     * @param vector
     */
    public Vector2d substractVector(Vector2d vector) {
        return new Vector2d(
                vecX - vector.vecX,
                vecY - vector.vecY);
    }

    /**
     * Contruct a new vector by multiplying the current vector by a scale value
     * 
     * @param scale
     */
    public Vector2d multiplyVectorByScale(double scale) {
        return new Vector2d(
                vecX * scale,
                vecY * scale);
    }

}
