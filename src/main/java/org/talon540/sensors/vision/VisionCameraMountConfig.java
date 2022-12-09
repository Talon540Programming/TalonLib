package org.talon540.sensors.vision;

import org.jetbrains.annotations.NotNull;
import org.talon540.math.Vector2d;

public class VisionCameraMountConfig {
    private final double mountHeightMeters;
    private final double mountAngleDegrees, mountAngleRadians;
    private final Vector2d robotRelativePosition;

    /**
     * Create an object used to tell the position of the camera on the robot
     *
     * @param mountHeightMeters height of the camera off the floor in meters
     * @param mountAngleDegrees pitch of the camera from the horizontal axis (positive values mean up)
     * @param robotRelativePosition camera's position relative to the center of the robot. (Pos X = right, Pos y =
     * forward)
     */
    private VisionCameraMountConfig(
            double mountHeightMeters, double mountAngleDegrees, @NotNull Vector2d robotRelativePosition
    ) {
        this.mountHeightMeters = mountHeightMeters;
        this.mountAngleDegrees = mountAngleDegrees;
        this.mountAngleRadians = Math.toRadians(mountAngleDegrees);
        this.robotRelativePosition = robotRelativePosition;
    }

    /**
     * Create an object used to tell the position of the camera on the robot.
     *
     * @param mountHeightMeters height of the camera off the floor in meters
     * @param mountAngleDegrees pitch of the camera from the horizontal axis (positive values mean up)
     * @param robotPositionX side to side offset (x-axis); center of the robot is (0,0) right is positive
     * @param robotPositionY forward or reverse offset (y-axis); center of the robot is (0,0), forward is positive
     */
    public VisionCameraMountConfig(
            double mountHeightMeters, double mountAngleDegrees, double robotPositionX, double robotPositionY
    ) {
        this(
                mountHeightMeters,
                mountAngleDegrees,
                new Vector2d(
                        robotPositionX,
                        robotPositionY
                )
        );
    }

    /**
     * Create a camera position with no defined camera position. Assumes the camera is mounted in the center of the
     * robot (0, 0)
     *
     * @param mountHeightMeters height of the camera off the floor in meters
     * @param mountAngleDegrees pitch of the camera from the horizontal axis (positive values mean up)
     */
    public VisionCameraMountConfig(double mountHeightMeters, double mountAngleDegrees) {
        this(
                mountHeightMeters,
                mountAngleDegrees,
                new Vector2d(
                        0,
                        0
                )
        );
    }

    /**
     * Get the height of the camera from the ground in meters
     *
     * @return height of the camera
     */
    public double getMountHeightMeters() {
        return mountHeightMeters;
    }

    /**
     * Get the mount angle of the camera in degrees
     *
     * @return mount angle in degrees
     */
    public double getMountAngleDegrees() {
        return mountAngleDegrees;
    }

    /**
     * Get the mount angle of the camera in radians
     *
     * @return mount angle in radians
     */
    public double getMountAngleRadians() {
        return mountAngleRadians;
    }

    /**
     * Get the position of the camera relative to the center (0, 0) of the robot
     *
     * @return relative position of the camera
     */
    public Vector2d getRobotRelativePosition() {
        return robotRelativePosition;
    }

    /**
     * Get the camera's side to side distance from the center of the robot
     *
     * @return side to side offset (x-axis)
     */
    public double getOffsetX() {
        return robotRelativePosition.getX();
    }

    /**
     * Get the camera's forward or reverse distance from the center of the robot
     *
     * @return forward or reverse offset (y-axis)
     */
    public double getOffsetY() {
        return robotRelativePosition.getY();
    }
}
