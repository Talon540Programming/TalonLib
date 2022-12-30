package org.talon540.sensors.vision;

public enum VisionCAMMode {
    /**
     * Use the vision system solely as a driver camera
     */
    DRIVER(1),
    /**
     * Use the vision system for vision processing
     */
    PROCESSING(0),

    /**
     * Unknown current camera mode
     */
    INVALID(-1);

    public final int val;

    VisionCAMMode(int val) {
        this.val = val;
    }
}
