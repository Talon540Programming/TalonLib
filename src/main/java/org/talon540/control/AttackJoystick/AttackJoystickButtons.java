package org.talon540.control.AttackJoystick;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class AttackJoystickButtons {
    public JoystickButton TRIGGER, TOP_MIDDLE, TOP_BOTTOM, TOP_LEFT, TOP_RIGHT, LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT;

    public AttackJoystickButtons(Joystick joystick) {
        TRIGGER = new JoystickButton(joystick, AttackJoystickButtonBindings.TRIGGER);
        TOP_MIDDLE = new JoystickButton(joystick, AttackJoystickButtonBindings.TOP_MIDDLE);
        TOP_LEFT = new JoystickButton(joystick, AttackJoystickButtonBindings.TOP_LEFT);
        TOP_RIGHT = new JoystickButton(joystick, AttackJoystickButtonBindings.TOP_RIGHT);
        TOP_BOTTOM = new JoystickButton(joystick, AttackJoystickButtonBindings.TOP_BOTTOM);
        LEFT_TOP = new JoystickButton(joystick, AttackJoystickButtonBindings.LEFT_TOP);
        LEFT_BOTTOM = new JoystickButton(joystick, AttackJoystickButtonBindings.LEFT_BOTTOM);
        RIGHT_TOP = new JoystickButton(joystick, AttackJoystickButtonBindings.RIGHT_TOP);
        RIGHT_BOTTOM = new JoystickButton(joystick, AttackJoystickButtonBindings.RIGHT_BOTTOM);
        BOTTOM_LEFT = new JoystickButton(joystick, AttackJoystickButtonBindings.BOTTOM_LEFT);
        BOTTOM_RIGHT = new JoystickButton(joystick, AttackJoystickButtonBindings.BOTTOM_RIGHT);
    }
}
