package org.talon540.math;

/**
 * Extends the {@link Vector2d} class and adds a horizontal velocity component for swerve
 */
public class Vector3d extends Vector2d {
    /** Linear velocity of the drivetrain in the side to side direction */
    public double velY;

    /**
     * @param velX speed of the robot in the x-axis
     * @param velY speed of the robot in the y-axis
     * @param velRot angular speed of the robot in rad/s
     */
    public Vector3d(double velX, double velY, double velRot) {
        super(velX, velRot);
        this.velY = velY;
    }

}
