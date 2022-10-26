package org.talon540.math;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;


public class Vector3d {
    public final double vecY;
    public final double vecX;
    public final double vecZ;

    public Vector3d(double velX, double velY, double velRot) {
        this.vecX = velX;
        this.vecY = velY;
        this.vecZ = velRot;
    }

    /**
     * Construct a new vector by adding a vector to the current vector
     */
    public Vector3d addVector(Vector3d vector) {
        return new Vector3d(vecX + vector.vecY, vecY + vector.vecZ, vecZ + vector.vecZ);
    }

    /**
     * Construct a new vector by subtracting a vector to the current vector
     */
    public Vector3d subtractVector(Vector3d vector) {
        return new Vector3d(vecX - vector.vecY, vecY - vector.vecZ, vecZ - vector.vecZ);
    }

    /**
     * Construct a new vector by multiplying the current vector by a scale value
     */
    public Vector3d multiplyVectorByScale(double scale) {
        return new Vector3d(vecX * scale, vecY * scale, vecZ * scale);
    }

    /**
     * Generate a Pose2d from the current vector
     */
    public Pose2d toPose2d() {
        return new Pose2d(new Translation2d(vecX, vecY), new Rotation2d(vecZ));
    }

    /**
     * Generate a Vector3d from a Pose2d
     */
    public static Vector3d fromPose2d(Pose2d pose) {
        return new Vector3d(pose.getX(), pose.getY(), pose.getRotation().getRadians());
    }
}
