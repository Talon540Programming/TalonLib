package org.talon540.math;

/**
 * Type of {@link Vector2d} class for swerve
 */
public class Vector3d {
    /** Linear velocity of the drivetrain in the side to side direction */
    public double velY;
    /** Linear velocity of the drivetrain in the forward/backword direction */
    public double velX;
    /** Angular velocity of the drivetrain in {@code rad/s} */
    public double velRot;

    /**
     * @param velX   speed of the robot in the x-axis
     * @param velY   speed of the robot in the y-axis
     * @param velRot angular speed of the robot in rad/s
     */
    public Vector3d(double velX, double velY, double velRot) {
        this.velX = velX;
        this.velY = velY;
        this.velRot = velRot;
    }

    /**
     * Contruct a new vector by adding a vector to the current vector
     * @param vector
     */
    public Vector3d addVector(Vector3d vector) {
        return new Vector3d(
            velX + vector.velY,
            velY + vector.velRot,
            velRot + vector.velRot
        );
    }

    /**
     * Contruct a new vector by subtracting a vector to the current vector
     * @param vector
     */
    public Vector3d substractVector(Vector3d vector) {
        return new Vector3d(
            velX - vector.velY,
            velY - vector.velRot,
            velRot - vector.velRot
        );
    }

    /**
     * Contruct a new vector by multiplying the current vector by a scale value
     * @param scale
     */
    public Vector3d multiplyVectorByScale(double scale) {
        return new Vector3d(
            velX * scale,
            velY * scale,
            velRot * scale
        );
    }

}
