package org.talon540.control.XboxController;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import org.talon540.control.XboxController.XboxControllerBinds.DPAD;

public class XboxControllerButtons {
    public JoystickButton LEFT_BUMPER, RIGHT_BUMPER, LEFT_STICK, RIGHT_STICK, A, B, X, Y, BACK, START;
    public Button LEFT_TRIGGER, RIGHT_TRIGGER;
    public XboxControllerDPADButton DPAD_NORTH, DPAD_NORTHEAST, DPAD_EAST, DPAD_SOUTHEAST, DPAD_SOUTH, DPAD_SOUTHWEST, DPAD_WEST, DPAD_NORTHWEST;

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

        LEFT_TRIGGER = new XboxControllerTriggerButton(controller, XboxControllerBinds.Triggers.LEFT_TRIGGER);
        RIGHT_TRIGGER = new XboxControllerTriggerButton(controller, XboxControllerBinds.Triggers.RIGHT_TRIGGER);

        DPAD_NORTH = new XboxControllerDPADButton(controller, DPAD.NORTH);
        DPAD_NORTHEAST = new XboxControllerDPADButton(controller, DPAD.NORTHEAST);
        DPAD_EAST = new XboxControllerDPADButton(controller, DPAD.EAST);
        DPAD_SOUTHEAST = new XboxControllerDPADButton(controller, DPAD.SOUTHEAST);
        DPAD_SOUTH = new XboxControllerDPADButton(controller, DPAD.SOUTH);
        DPAD_SOUTHWEST = new XboxControllerDPADButton(controller, DPAD.SOUTHWEST);
        DPAD_WEST = new XboxControllerDPADButton(controller, DPAD.WEST);
        DPAD_NORTHWEST = new XboxControllerDPADButton(controller, DPAD.NORTHWEST);
    }
}
