package org.talon540.control;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class UserButton extends Trigger {
    public UserButton() {
        super(RobotController::getUserButton);
    }
}
