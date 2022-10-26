package org.talon540.control.XboxController;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import org.talon540.control.XboxController.XboxControllerDPADButton.DPAD;
import org.talon540.control.XboxController.XboxControllerTriggerButton.Triggers;


public class XboxControllerButtons {
    public final JoystickButton LEFT_BUMPER;
    public final JoystickButton RIGHT_BUMPER;
    public final JoystickButton LEFT_STICK;
    public final JoystickButton RIGHT_STICK;
    public final JoystickButton A;
    public final JoystickButton B;
    public final JoystickButton X;
    public final JoystickButton Y;
    public final JoystickButton BACK;
    public final JoystickButton START;
    public final Button LEFT_TRIGGER;
    public final Button RIGHT_TRIGGER;
    public final XboxControllerDPADButton DPAD_NORTH;
    public final XboxControllerDPADButton DPAD_NORTHEAST;
    public final XboxControllerDPADButton DPAD_EAST;
    public final XboxControllerDPADButton DPAD_SOUTHEAST;
    public final XboxControllerDPADButton DPAD_SOUTH;
    public final XboxControllerDPADButton DPAD_SOUTHWEST;
    public final XboxControllerDPADButton DPAD_WEST;
    public final XboxControllerDPADButton DPAD_NORTHWEST;

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

        LEFT_TRIGGER = new XboxControllerTriggerButton(controller, Triggers.LEFT_TRIGGER);
        RIGHT_TRIGGER = new XboxControllerTriggerButton(controller, Triggers.RIGHT_TRIGGER);

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
