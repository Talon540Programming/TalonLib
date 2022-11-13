package org.talon540.control.XboxController;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Button;

public class XboxControllerTriggerButton extends Button {
    private final XboxController controller;
    private final Triggers selectedTrigger;
    private final double activateThreshold;

    /**
     * Create a button from the trigger of an XboxController
     *
     * @param controller the xbox controller
     * @param targetTrigger the selected trigger {@link Triggers}
     * @param activateThreshold threshold which will activate the button
     */
    public XboxControllerTriggerButton(XboxController controller, Triggers targetTrigger, double activateThreshold) {
        this.controller = controller;
        this.selectedTrigger = targetTrigger;
        this.activateThreshold = activateThreshold;
    }

    /**
     * Create a trigger button with the default threshold to activate (0.4 / 40%)
     *
     * @param controller the xbox controller
     * @param targetTrigger the selected trigger {@link Triggers}
     */
    public XboxControllerTriggerButton(XboxController controller, Triggers targetTrigger) {
        this(controller, targetTrigger, 0.4);
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

    public enum Triggers {
        LEFT_TRIGGER,
        RIGHT_TRIGGER
    }

}
