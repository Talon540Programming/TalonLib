package org.talon540.math;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * Type of {@link Vector2d} class for swerve
 */
public class Vector3d {
    /** Linear velocity of the drivetrain in the side to side direction */
    public double vecY;
    /** Linear velocity of the drivetrain in the forward/backword direction */
    public double vecX;
    /** Angular velocity of the drivetrain in {@code rad/s} */
    public double vecRot;

    /**
     * @param velX   speed of the robot in the x-axis
     * @param velY   speed of the robot in the y-axis
     * @param velRot angular speed of the robot in rad/s
     */
    public Vector3d(double velX, double velY, double velRot) {
        this.vecX = velX;
        this.vecY = velY;
        this.vecRot = velRot;
    }

    /**
     * Contruct a new vector by adding a vector to the current vector
     * 
     * @param vector
     */
    public Vector3d addVector(Vector3d vector) {
        return new Vector3d(
                vecX + vector.vecY,
                vecY + vector.vecRot,
                vecRot + vector.vecRot);
    }

    /**
     * Contruct a new vector by subtracting a vector to the current vector
     * 
     * @param vector
     */
    public Vector3d substractVector(Vector3d vector) {
        return new Vector3d(
                vecX - vector.vecY,
                vecY - vector.vecRot,
                vecRot - vector.vecRot);
    }

    /**
     * Contruct a new vector by multiplying the current vector by a scale value
     * 
     * @param scale
     */
    public Vector3d multiplyVectorByScale(double scale) {
        return new Vector3d(
                vecX * scale,
                vecY * scale,
                vecRot * scale);
    }

    /**
     * Generate a Pose2d from the current vector
     * 
     * @return
     */
    public Pose2d toPose2d() {
        return new Pose2d(new Translation2d(vecX, vecY), new Rotation2d(vecRot));
    }

    /**
     * Generate a Vector3d from a Pose2d
     * 
     * @param pose
     * @return
     */
    public static Vector3d fromPose2d(Pose2d pose) {
        return new Vector3d(pose.getX(), pose.getY(), pose.getRotation().getRadians());
    }
}
