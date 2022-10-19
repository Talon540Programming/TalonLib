package org.talon540.control.RoboRio;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.button.Button;

public class UserButton extends Button {
    @Override
    public boolean get() {
        return RobotController.getUserButton();
    }
}
