package org.talon540.vision.TalonTracking;

public enum TalonTrackingStates {
    /**
     * The device is on and updating values in NetworkTables
     */
    POLLING,
    /**
     * Put the device in standby mode, on but doesn't perform vision processing
     */
    STANDBY,
    /**
     * Disable the device
     */
    OFF
}
