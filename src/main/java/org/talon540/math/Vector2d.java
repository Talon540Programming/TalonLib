package org.talon540.math;

/**
 * Object for tracking linear and rotational velocities of a differential drivetrain
 */
public class Vector2d {
    /** Linear velocity of the drivetrain in the forward/backword direction */
    public double velY;
    /** Angular velocity of the drivetrain in {@code rad/s} */
    public double velRot;

    /**
     * @param linearSpeed speed of the robot
     * @param rotationalSpeed angular velocity of the robot in rad/s
     */
    public Vector2d(double linearSpeed, double rotationalSpeed) {
        this.velY = linearSpeed;
        this.velRot = rotationalSpeed;
    }
}
