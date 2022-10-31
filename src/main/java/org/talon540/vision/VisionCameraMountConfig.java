package org.talon540.vision;

import edu.wpi.first.math.geometry.Translation2d;


public class VisionCameraMountConfig {
    private final double mountHeightMeters;
    private final double mountAngleDegrees, mountAngleRadians;
    private final Translation2d robotRelativePosition;

    /**
     * Create an object used to tell the position of the camera on the robot
     *
     * @param mountHeightMeters height of the camera off the floor in meters
     * @param mountAngleDegrees pitch of the camera from the horizontal axis (positive values mean up)
     * @param robotRelativePosition camera's position relative to the center of the robot. See {@link Translation2d} for
     * specifics
     */
    public VisionCameraMountConfig(
            double mountHeightMeters, double mountAngleDegrees, Translation2d robotRelativePosition
    ) {
        this.mountHeightMeters = mountHeightMeters;
        this.mountAngleDegrees = mountAngleDegrees;
        this.mountAngleRadians = Math.toRadians(mountAngleDegrees);
        this.robotRelativePosition = robotRelativePosition;
    }

    /**
     * Create an object used to tell the position of the camera on the robot. Follows conventional mathematical axis,
     * see {@link Translation2d}
     *
     * @param mountHeightMeters height of the camera off the floor in meters
     * @param mountAngleDegrees pitch of the camera from the horizontal axis (positive values mean up)
     * @param robotPositionX double robotPositionX, side to side offset (x-axis); center of the robot is (0,0) right is
     * positive
     * @param robotPositionY forward or reverse offset (y-axis); center of the robot is (0,0), forward is positive
     */
    public VisionCameraMountConfig(
            double mountHeightMeters, double mountAngleDegrees, Double robotPositionX, Double robotPositionY
    ) {
        this(mountHeightMeters,
                mountAngleDegrees,
                robotPositionX == null || robotPositionY == null ? null : new Translation2d(robotPositionY,
                        -robotPositionX
                )
        );
    }

    /**
     * Create a camera position with no defined camera position.
     *
     * @param mountHeightMeters height of the camera off the floor in meters
     * @param mountAngleDegrees pitch of the camera from the horizontal axis (positive values mean up)
     * @implNote This will cause methods dealing with positioning to return {@code null} because the position of the
     * camera is undefined
     */
    public VisionCameraMountConfig(double mountHeightMeters, double mountAngleDegrees) {
        this(mountHeightMeters, mountAngleDegrees, null);
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
    public Translation2d getRobotRelativePosition() {
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
