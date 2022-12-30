package org.talon540.sensors.vision;

public enum VisionCAMMode {
    /**
     * Use the vision system solely as a driver camera
     */
    kDriver,

    /**
     * Use the vision system for vision processing
     */
    kProcessing,

    /**
     * Unknown current camera mode
     */
    kInvalid
}
