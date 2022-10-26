package org.talon540.control.XboxController;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Button;


public class XboxControllerDPADButton extends Button {
    private final XboxController controller;
    private final DPAD selectedButton;

    /**
     * Construct a button from a selected button on the DPAD
     *
     * @param controller xbox controller
     * @param selectedButton the selected {@link DPAD} button
     */
    public XboxControllerDPADButton(XboxController controller, DPAD selectedButton) {
        this.controller = controller;
        this.selectedButton = selectedButton;
    }

    @Override
    public boolean get() {
        return controller.getPOV() == selectedButton.POV;
    }

    /**
     * Returns true if the requested button or either axis on either side of the button is true
     */
    public boolean getWithNeighbor() {
        return get() || controller.getPOV() == selectedButton.POV + 45 || controller.getPOV() == selectedButton.POV - 45;
    }

    public enum DPAD {
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

        DPAD(int pov) {
            this.POV = pov;
        }

    }
}
