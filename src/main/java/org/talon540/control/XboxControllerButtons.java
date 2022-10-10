package org.talon540.control;

import org.talon540.control.XboxControllerBinds.DPAD;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Button;

public class XboxControllerButtons {
    public JoystickButton LEFT_BUMPER, RIGHT_BUMPER, LEFT_STICK, RIGHT_STICK, A, B, X, Y, BACK, START;
    public Button LEFT_TRIGGER, RIGHT_TRIGGER;
    public Button DAPD_NORTH, DAPD_NORTHEAST, DAPD_EAST, DAPD_SOUTHEAST, DAPD_SOUTH, DAPD_SOUTHWEST, DAPD_WEST, DAPD_NORTHWEST;

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

        DAPD_NORTH = new XboxControllerDPADButton(controller, DPAD.NORTH);
        DAPD_NORTHEAST = new XboxControllerDPADButton(controller, DPAD.NORTHEAST);
        DAPD_EAST = new XboxControllerDPADButton(controller, DPAD.EAST);
        DAPD_SOUTHEAST = new XboxControllerDPADButton(controller, DPAD.SOUTHEAST);
        DAPD_SOUTH = new XboxControllerDPADButton(controller, DPAD.SOUTH);
        DAPD_SOUTHWEST = new XboxControllerDPADButton(controller, DPAD.SOUTHWEST);
        DAPD_WEST = new XboxControllerDPADButton(controller, DPAD.WEST);
        DAPD_NORTHWEST = new XboxControllerDPADButton(controller, DPAD.NORTHWEST);
    }
}
