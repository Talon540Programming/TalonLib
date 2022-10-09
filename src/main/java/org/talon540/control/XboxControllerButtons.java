package org.talon540.control;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class XboxControllerButtons {
    public JoystickButton LEFTBUMPER, RIGHTBUMPER, LEFTSTICK, RIGHTSTICK, A, B, X, Y, BACK, START;

    public XboxControllerButtons(XboxController controller) {
        LEFTBUMPER = new JoystickButton(controller, XboxController.Button.kLeftBumper.value);
        RIGHTBUMPER = new JoystickButton(controller, XboxController.Button.kRightBumper.value);
        LEFTSTICK = new JoystickButton(controller, XboxController.Button.kLeftStick.value);
        RIGHTSTICK = new JoystickButton(controller, XboxController.Button.kRightStick.value);
        A = new JoystickButton(controller, XboxController.Button.kA.value);
        B = new JoystickButton(controller, XboxController.Button.kB.value);
        X = new JoystickButton(controller, XboxController.Button.kX.value);
        Y = new JoystickButton(controller, XboxController.Button.kY.value);
        BACK = new JoystickButton(controller, XboxController.Button.kBack.value);
        START = new JoystickButton(controller, XboxController.Button.kStart.value);

    }
}
