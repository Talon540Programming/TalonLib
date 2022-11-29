package org.talon540.vision;

import edu.wpi.first.util.sendable.Sendable;
import org.jetbrains.annotations.NotNull;
import org.talon540.vision.VisionFlags.CAMMode;
import org.talon540.vision.VisionFlags.LEDStates;


/**
 * Interface representing a vision system capable of tracking targets using computer vision to produce a VisionState
 */
public interface TalonVisionSystem extends Sendable {
    /**
     * @return current mode of the LEDs
     */
    LEDStates getLEDMode();

    /**
     * Set the mode of the LEDs of the vision system
     *
     * @param state target state of the camera
     */
    void setLEDMode(LEDStates state);

    default void setLEDMode(@NotNull String mode) {
        switch (mode.toLowerCase()) {
            case "1":
            case "on":
                enableLEDS();
            case "2":
            case "blink":
                blinkLEDS();
            default:
            case "0":
            case "off":
                disableLEDS();

        }
    }

    /**
     * Get the current index of the running vision system pipeline
     *
     * @return current index
     */
    int getPipelineIndex();

    /**
     * Set the index of the vision system's pipeline
     */
    void setPipelineIndex(int index);

    /**
     * Enables vision system LEDs
     */
    default void enableLEDS() {
        setLEDMode(LEDStates.ON);
    }

    /**
     * Disable vision system LEDs
     */
    default void disableLEDS() {
        setLEDMode(LEDStates.OFF);
    }

    /**
     * Blinks vision system LEDs
     */
    default void blinkLEDS() {
        setLEDMode(LEDStates.BLINK);
    }

    /**
     * Get the current camera mode of the vision system
     *
     * @return current camera mode
     */
    CAMMode getCamMode();

    /**
     * Set the camera mode of the vision system
     */
    void setCamMode(CAMMode targetMode);

    /**
     * Get the current vision state data. Returns {@code null} if it doesn't exist because the target isn't found or
     * unrealistic
     */
    TalonVisionState getVisionState();

    /**
     * Whether the vision system target is currently viewed
     *
     * @return view status of target
     */
    boolean targetViewed();

    //    Utils

    /**
     * Get distance from a specified target (Hypotenuse). Follows
     * <a href="https://docs.limelightvision.io/en/latest/cs_estimating_distance.html">...</a>
     *
     * @param targetHeight height of the retro reflector in meters. Already offsets for mount height
     * @return distance from the target in {@code meters}. Returns {@code null} if target is not found or value is
     * unrealistic
     */
    Double getDistanceFromTarget(double targetHeight);

    /**
     * Get distance from a specified target's base. Follows
     * <a href="https://docs.limelightvision.io/en/latest/cs_estimating_distance.html">...</a>
     *
     * @param targetHeight height of the retro reflector in meters. Already offsets for mount height
     * @return distance from the base of the target in {@code meters}. Returns {@code null} if target is not found or
     * value is unrealistic
     */
    Double getDistanceFromTargetBase(double targetHeight);

    Double getDistanceFromTargetBaseFromRobotCenter(double targetHeight);

    //    Double getDistanceFromTargetFromRobotCenter(double targetHeight);
}
