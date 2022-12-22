package org.talon540.control.AttackJoystick;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Extends the normal WPI Joystick class with methods for calculating deadband angles and normalizing the curve
 * generated by changes in inputs
 */
public class TalonJoystick extends Joystick {
    private double deadband;

    public AttackJoystickButtons buttons = new AttackJoystickButtons(this);

    /**
     * @param port port on the driver-station
     * @param deadbandPercentage minimum percent required to bypass deadband.
     */
    public TalonJoystick(int port, double deadbandPercentage) {
        super(port);
        this.deadband = deadbandPercentage;
    }

    /**
     * Create a joystick with 20% deadband (default)
     */
    public TalonJoystick(int port) {
        this(
                port,
                0.2
        );
    }

    /**
     * Get the X value from the joysticks and check if it is within the deadband provided
     *
     * @return normalized X
     */
    public double getDeadbandX() {
        return MathUtil.applyDeadband(super.getX(), this.deadband);
    }

    /**
     * Get the Y value from the joysticks and check if it is within the deadband provided
     *
     * @return normalized Y
     */
    public double getDeadbandY() {
        return MathUtil.applyDeadband(super.getY(), this.deadband);
    }

    /**
     * Change the currently set deadband. Must be within [-1, 1]
     *
     * @param deadband new deadband val
     */
    public void setDeadband(double deadband) {
        if (deadband > 1 || 0 > deadband)
            throw new IllegalArgumentException("Deadband cannot exceed max output");
        this.deadband = deadband;
    }

    /**
     * Get the current deadband of the button
     *
     * @return deadband
     */
    public double getDeadband() {
        return deadband;
    }
}
