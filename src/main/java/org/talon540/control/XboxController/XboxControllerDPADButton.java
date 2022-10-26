package org.talon540.control.XboxController;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Button;
import org.talon540.control.XboxController.XboxControllerBinds.DPAD;


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
}
