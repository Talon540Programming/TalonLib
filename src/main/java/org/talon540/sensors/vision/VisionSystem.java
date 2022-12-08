package org.talon540.sensors.vision;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.jetbrains.annotations.NotNull;
import org.talon540.math.Vector2d;
import org.talon540.sensors.vision.VisionFlags.CAMMode;
import org.talon540.sensors.vision.VisionFlags.LEDStates;

public abstract class VisionSystem implements Sendable {
    protected final VisionCameraMountConfig mountConfig;

    protected VisionSystem(@NotNull VisionCameraMountConfig mountConfig) {
        this.mountConfig = mountConfig;
    }

    /**
     * Get the current index of the running vision system pipeline
     *
     * @return current index
     */
    public abstract int getPipelineIndex();

    /**
     * Set the index of the vision system's pipeline
     */
    public abstract void setPipelineIndex(int index);

    /**
     * @return current mode of the LEDs
     */
    public abstract LEDStates getLEDMode();

    /**
     * Set the mode of the LEDs of the vision system
     *
     * @param state target state of the camera
     */
    public abstract void setLEDMode(LEDStates state);

    /**
     * Enables vision system LEDs
     */
    public void enableLEDS() {
        setLEDMode(LEDStates.ON);
    }

    /**
     * Disable vision system LEDs
     */
    public void disableLEDS() {
        setLEDMode(LEDStates.OFF);
    }

    /**
     * Blinks vision system LEDs
     */
    public void blinkLEDS() {
        setLEDMode(LEDStates.BLINK);
    }

    /**
     * Get the current camera mode of the vision system
     *
     * @return current camera mode
     */
    public abstract CAMMode getCamMode();

    /**
     * Set the camera mode of the vision system
     */
    public abstract void setCamMode(CAMMode targetMode);

    /**
     * Whether the vision system target is currently viewed
     *
     * @return view status of target
     */
    public abstract boolean targetViewed();

    /**
     * Get the current vision state data. Returns {@code null} if it doesn't exist because the target isn't found or is
     * unrealistic
     */
    public abstract VisionState getVisionState();

    // Calculations

    /**
     * Get distance from a specified target (Hypotenuse). Follows
     * <a href="https://docs.limelightvision.io/en/latest/cs_estimating_distance.html">...</a>
     *
     * @param targetHeightMeters height of the retro reflector in meters. Already offsets for mount height
     * @return distance from the target in {@code meters}. Returns {@code null} if target is not found or value is
     * unrealistic
     */
    public Double getDistanceFromTarget(double targetHeightMeters) {
        if (!targetViewed())
            return null;
        double deltaAngle = Math.toRadians(this.mountConfig.getMountAngleDegrees() + this.getVisionState().getPitch());
        return (targetHeightMeters - this.mountConfig.getMountHeightMeters()) / Math.sin(deltaAngle);
    }

    /**
     * Get distance from a specified target's base. Follows
     * <a href="https://docs.limelightvision.io/en/latest/cs_estimating_distance.html">...</a>
     *
     * @param targetHeightMeters height of the retro reflector in meters. Already offsets for mount height
     * @return distance from the base of the target in {@code meters}. Returns {@code null} if target is not found or
     * value is unrealistic
     */
    public Double getDistanceFromTargetBase(double targetHeightMeters) {
        if (!targetViewed())
            return null;
        double deltaAngle = Math.toRadians(this.mountConfig.getMountAngleDegrees() + this.getVisionState().getPitch());
        return (targetHeightMeters - this.mountConfig.getMountHeightMeters()) / Math.tan(deltaAngle);
    }

    /**
     * Get the distance from the center of the robot to the base of a target
     *
     * @param targetHeightMeters height of the retro reflector in meters. Already offsets for mount height
     * @return distance from the base of the target in {@code meters} from the center of the robot. Returns {@code null}
     * if target is not found or value is unrealistic
     */
    public Double getDistanceToTargetBaseFromRobotCenter(double targetHeightMeters) {
        if (!targetViewed())
            return null;

        Vector2d cameraPosition = mountConfig.getRobotRelativePosition();

        double deltaX = cameraPosition.getX();
        double deltaY = cameraPosition.getY();

        double targetCameraOffsetRadians = Math.toRadians(getVisionState().getYaw());
        double distanceFromTargetMeters = getDistanceFromTargetBase(targetHeightMeters);

        // Included angle between the robot's center and the target
        double theta = Math.signum(deltaX) * targetCameraOffsetRadians;

        // @formatter:off

        if (deltaX == 0) {
            return distanceFromTargetMeters + deltaY;
        } else if (deltaY == 0) {
            theta += (Math.PI / 2.0);
            return Math.sqrt(Math.pow(distanceFromTargetMeters, 2) + Math.pow(deltaX, 2) - (2 * distanceFromTargetMeters * Math.abs(deltaX) * Math.cos(theta)));
        }

        theta += deltaY < 0 ? (Math.PI / 2.0) - Math.atan(Math.abs(deltaY) / Math.abs(deltaX)) : Math.PI - Math.atan(Math.abs(deltaX) / Math.abs(deltaY));

        double includedSideLength = Math.hypot(deltaX, deltaY);

        return Math.sqrt(Math.pow(distanceFromTargetMeters, 2) + Math.pow(includedSideLength, 2) - (2 * distanceFromTargetMeters * includedSideLength * Math.cos(theta)));
        // @formatter:on
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addBooleanProperty(
                "viewed",
                this::targetViewed,
                null
        );
        builder.addDoubleProperty(
                "yaw",
                () -> targetViewed() ? getVisionState().getYaw() : 0,
                null
        );
        builder.addDoubleProperty(
                "pitch",
                () -> targetViewed() ? getVisionState().getPitch() : 0,
                null
        );
        builder.addDoubleProperty(
                "skew",
                () -> targetViewed() ? getVisionState().getSkew() : 0,
                null
        );
        builder.addDoubleProperty(
                "area",
                () -> targetViewed() ? getVisionState().getArea() : 0,
                null
        );
        builder.addDoubleProperty(
                "error",
                () -> targetViewed() ? getVisionState().getError() : 0,
                null
        );
        builder.addDoubleProperty(
                "latency",
                () -> targetViewed() ? getVisionState().getPipelineLatency() : 0,
                null
        );
        builder.addDoubleProperty(
                "timestamp",
                () -> targetViewed() ? getVisionState().getStateTimestamp() : 0,
                null
        );
        builder.addDoubleProperty(
                "pipeline",
                this::getPipelineIndex,
                null
        );
        builder.addStringProperty(
                "LEDMode",
                () -> getLEDMode().toString(),
                null
        );
    }
}
