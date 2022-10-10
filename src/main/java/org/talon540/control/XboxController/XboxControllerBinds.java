package org.talon540.control.XboxController;

public class XboxControllerBinds {
    public static enum Triggers {
        LEFT_TRIGGER,
        RIGHT_TRIGGER
    }

    public static enum DPAD {
        NORTH(0),
        NORTHEAST(45),
        EAST(90),
        SOUTHEAST(135),
        SOUTH(180),
        SOUTHWEST(225),
        WEST(270),
        NORTHWEST(315);

        /**
         * The POV of the selected button in degrees (clockwise) where 0 is North
         */
        public final int POV;

        private DPAD(int pov) {
            this.POV = pov;
        }

    }
}
