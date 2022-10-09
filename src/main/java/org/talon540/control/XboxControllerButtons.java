package org.talon540.control;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class XboxControllerButtons {
    public JoystickButton LEFT_BUMPER, RIGHT_BUMPER, LEFT_STICK, RIGHT_STICK, A, B, X, Y, BACK, START, LEFT_TRIGGER, RIGHT_TRIGGER;

    public XboxControllerButtons(XboxController controller) {
        LEFT_BUMPER = new JoystickButton(controller, XboxController.Button.kLeftBumper.value);
        RIGHT_BUMPER = new JoystickButton(controller, XboxController.Button.kRightBumper.value);
        LEFT_STICK = new JoystickButton(controller, XboxController.Button.kLeftStick.value);
        RIGHT_STICK = new JoystickButton(controller, XboxController.Button.kRightStick.value);
        A = new JoystickButton(controller, XboxController.Button.kA.value);
        B = new JoystickButton(controller, XboxController.Button.kB.value);
        X = new JoystickButton(controller, XboxController.Button.kX.value);
        Y = new JoystickButton(controller, XboxController.Button.kY.value);
        BACK = new JoystickButton(controller, XboxController.Button.kBack.value);
        START = new JoystickButton(controller, XboxController.Button.kStart.value);

        // TODO: Find trigger button ids
        LEFT_TRIGGER = new JoystickButton(controller, 0);
        RIGHT_BUMPER = new JoystickButton(controller, 0);
    }
}
