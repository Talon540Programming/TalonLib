package org.talon540.control.XboxController;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Button;
import org.talon540.control.XboxController.XboxControllerBinds.Triggers;

public class XboxControllerTriggerButton extends Button {
    private final XboxController controller;
    private final Triggers selectedTrigger;
    private final double activateThreshold;

    /**
     * Create a button from the trigger of an XboxController
     * @param controller the xbox controller
     * @param trigger the selected triggerr {@link XboxControllerBinds}
     * @param activateThreshold threshold which will activate the button
     */
    public XboxControllerTriggerButton(XboxController controller, Triggers trigger, double activateThreshold) {
        this.controller = controller;
        this.selectedTrigger = trigger;
        this.activateThreshold = activateThreshold;
    }

    /**
     * Create a trigger button with the default threshold to activate (0.4)
     * @param controller the xbox controller
     * @param leftTrigger the selected triggerr {@link XboxControllerBinds}
     */
    public XboxControllerTriggerButton(XboxController controller, Triggers leftTrigger) {
        this(controller, leftTrigger, 0.4);
    }

    @Override
    public boolean get() {
        switch (selectedTrigger) {
            case LEFT_TRIGGER:
                return controller.getLeftTriggerAxis() > activateThreshold;
            case RIGHT_TRIGGER:
                return controller.getRightTriggerAxis() > activateThreshold;
            default:
                return false;
        }
    }

}
