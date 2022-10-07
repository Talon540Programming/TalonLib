package org.talon540.math;

/**
 * Object for tracking linear and rotational velocities of a differential
 * drivetrain
 */
public class Vector2d {
    /** Linear velocity of the drivetrain in the forward/backword direction */
    public double forwardVelocity;
    /** Angular velocity of the drivetrain in {@code rad/s} */
    public double velRot;

    /**
     * @param linearSpeed     speed of the robot
     * @param rotationalSpeed angular velocity of the robot in rad/s
     */
    public Vector2d(double linearSpeed, double rotationalSpeed) {
        this.forwardVelocity = linearSpeed;
        this.velRot = rotationalSpeed;
    }

    /**
     * Contruct a new vector by adding a vector to the current vector
     * 
     * @param vector
     */
    public Vector2d addVector(Vector2d vector) {
        return new Vector2d(
                forwardVelocity + vector.forwardVelocity,
                velRot + vector.velRot);
    }

    /**
     * Contruct a new vector by subtracting a vector to the current vector
     * 
     * @param vector
     */
    public Vector2d substractVector(Vector2d vector) {
        return new Vector2d(
                forwardVelocity - vector.forwardVelocity,
                velRot - vector.velRot);
    }

    /**
     * Contruct a new vector by multiplying the current vector by a scale value
     * 
     * @param scale
     */
    public Vector2d multiplyVectorByScale(double scale) {
        return new Vector2d(
                forwardVelocity * scale,
                velRot * scale);
    }

}
