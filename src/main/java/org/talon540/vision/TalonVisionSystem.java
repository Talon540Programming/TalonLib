package org.talon540.vision;

import org.talon540.vision.VisionFlags.CAMMode;
import org.talon540.vision.VisionFlags.LEDStates;

import edu.wpi.first.util.sendable.Sendable;


public interface TalonVisionSystem extends Sendable {
    /**
     * Set the state of the LEDs of the vision system
     *
     * @param state target state of the camera
     */
    void setLEDState(LEDStates state);

    /**
     * @return Get the current state of the leds
     */
    LEDStates getLEDState();

    /**
     * Get the current index of the pipeline
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
        setLEDState(LEDStates.ON);
    }

    /**
     * Disable vision system LEDs
     */
    default void disableLEDS() {
        setLEDState(LEDStates.OFF);
    }

    /**
     * Blinks vision system LEDs
     */
    default void blinkLEDS() {
        setLEDState(LEDStates.BLINK);
    }

    /**
     * Set the camera mode of the vision system
     */
    void setCamMode(CAMMode targetMode);

    /**
     * Get the current camera mode of the vision system
     * @return current camera mode
     */
    CAMMode getCamMode();

    /**
     * Get the current vision state data. Returns {@code null} if it doesn't exist
     */
    TalonVisionState getVisionState();

    /**
     * Weather the vision system target is currently viewed
     *
     * @return view status of target
     */
    default boolean targetViewed() {return getVisionState() != null;}


    /**
     * Get distance from a specified target's base. Follows
     * <a href="https://docs.limelightvision.io/en/latest/cs_estimating_distance.html">...</a>
     *
     * @param targetHeight height of the retro reflector in meters. Already offsets for mount height
     * @return distance from the base of the target in {@code meters}. Returns {@code null} if target is not found or
     * value is unrealistic
     * @implNote Returns null if target is not in view
     */
    Double getDistanceFromTargetBase(double targetHeight);

    /**
     * Get distance from a specified target (Hypotenuse). Follows
     * <a href="https://docs.limelightvision.io/en/latest/cs_estimating_distance.html">...</a>
     *
     * @param targetHeight height of the retro reflector in meters. Already offsets for mount height
     * @return distance from the target in {@code meters}. Returns {@code null} if target is not found or value is
     * unrealistic
     */
    Double getDistanceFromTarget(double targetHeight);

}
