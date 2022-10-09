package org.talon540.control;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class AttackJoystickButtons {
    public JoystickButton TRIGGER, TOPMIDDLE, TOPLEFT, TOPRIGHT, LEFTTOP, LEFTBOTTOM, RIGHTTOP, RIGHTBOTTOM, BOTTOMLEFT, BOTTOMRIGHT;

    public AttackJoystickButtons(Joystick joystick) {
        TRIGGER = new JoystickButton(joystick, AttackJoystickButtonBindings.TRIGGER);
        TOPMIDDLE = new JoystickButton(joystick, AttackJoystickButtonBindings.TOPMIDDLE);
        TOPLEFT = new JoystickButton(joystick, AttackJoystickButtonBindings.TOPLEFT);
        TOPRIGHT = new JoystickButton(joystick, AttackJoystickButtonBindings.TOPRIGHT);
        LEFTTOP = new JoystickButton(joystick, AttackJoystickButtonBindings.LEFTTOP);
        LEFTBOTTOM = new JoystickButton(joystick, AttackJoystickButtonBindings.LEFTBOTTOM);
        RIGHTTOP = new JoystickButton(joystick, AttackJoystickButtonBindings.RIGHTTOP);
        RIGHTBOTTOM = new JoystickButton(joystick, AttackJoystickButtonBindings.RIGHTBOTTOM);
        BOTTOMLEFT = new JoystickButton(joystick, AttackJoystickButtonBindings.BOTTOMLEFT);
        BOTTOMRIGHT = new JoystickButton(joystick, AttackJoystickButtonBindings.BOTTOMRIGHT);
    }
}
