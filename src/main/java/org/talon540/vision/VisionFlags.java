package org.talon540.vision;

public class VisionFlags {
    public enum LEDStates {
        /**
         * Turn on the processioning unit's LEDs
         */
        ON,
        /**
         * Turn off the processioning unit's LEDs
         */
        OFF,
        /**
         * Blink the processioning unit's LEDs
         */
        BLINK,
        /**
         * Set the processioning unit's LEDs to the pipeline or camera default
         */
        DEFAULT
    }

    public enum CAMMode {
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

        CAMMode(int val) {
            this.val = val;
        }
    }

    public enum TargetAmbiguity {
        /**
         * A target was found and is likely valid
         */
        SAFE,
        /**
         * A target was found but may be ambiguous
         */
        UNSAFE,
        /**
         * The vision system doesn't support target ambiguity
         */
        INVALID
    }

}
