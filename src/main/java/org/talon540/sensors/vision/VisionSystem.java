package org.talon540.sensors.vision;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.talon540.sensors.vision.VisionFlags.CAMMode;
import org.talon540.sensors.vision.VisionFlags.LEDStates;

public abstract class VisionSystem implements Sendable {
    protected VisionCameraMountConfig mountConfig;

    protected VisionSystem(VisionCameraMountConfig mountConfig) {
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
     * Set the mode of the LEDs of the vision system
     *
     * @param state target state of the camera
     */
    public abstract void setLEDMode(LEDStates state);

    /**
     * @return current mode of the LEDs
     */
    public abstract LEDStates getLEDMode();

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
                (index) -> setPipelineIndex(MathUtil.clamp(
                        (int) index,
                        0,
                        9
                ))
        );
        builder.addStringProperty(
                "LEDMode",
                () -> getLEDMode().toString(),
                null
        );
    }
}
