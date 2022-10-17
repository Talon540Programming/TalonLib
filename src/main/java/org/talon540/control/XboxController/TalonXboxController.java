package org.talon540.control.XboxController;

import edu.wpi.first.wpilibj.XboxController;

/**
 * Extends the normal WPI XboxController class with methods for calculating
 * deadband angles and normalizing the curve generated by changes in inputs
 */
public class TalonXboxController extends XboxController {
    public double deadband;

    public XboxControllerButtons buttons = new XboxControllerButtons(this);

    /**
     * @param port               port on the driverstation
     * @param deadbandPercentage minimum percent required to bypass deadband.
     */
    public TalonXboxController(int port, double deadbandPercentage) {
        super(port);
        this.deadband = deadbandPercentage;
    }

    /**
     * Create a joystick with 10% deadband (default)
     * 
     * @param port
     */
    public TalonXboxController(int port) {
        this(port, 0.1);
    }

    /**
     * Get the X value from the left joystick and check if it is within the deadband
     * provided
     * 
     * @return normalized X
     */
    public double getLeftDeadbandX() {
        return checkDeadband(super.getLeftX());
    }

    /**
     * Get the Y value from the left joystick and check if it is within the deadband
     * provided
     * 
     * @return normalized Y
     */
    public double getLeftDeadbandY() {
        return checkDeadband(super.getLeftY());
    }

    /**
     * Get the X value from the right joystick and check if it is within the
     * deadband provided
     * 
     * @return normalized X
     */
    public double getRightDeadbandX() {
        return checkDeadband(super.getRightX());
    }

    /**
     * Get the Y value from the right joystick and check if it is within the
     * deadband provided
     * 
     * @return normalized Y
     */
    public double getRightDeadbandY() {
        return checkDeadband(super.getRightY());
    }

    /**
     * Start rumbling both sides of the controller to some percent
     * @param percent percent in [-1,1]
     */
    public void startRumble(double percent) {
        this.setRumble(RumbleType.kLeftRumble, percent);
        this.setRumble(RumbleType.kRightRumble, percent);
    }

    /**
     * Stop the controller from rumbling
     */
    public void stopRumble() {
        this.setRumble(RumbleType.kLeftRumble, 0);
        this.setRumble(RumbleType.kRightRumble, 0);

    }

    /**
     * Return 0 if the reported value is within the deadband
     * 
     * @param val current val within domain [-1, 1]
     * @return Checked deadband value
     */
    private double checkDeadband(double val) {
        if (Math.abs(val) > deadband) {
            if (val > 0.0) {
                return (val - deadband) / (1.0 - deadband);
            } else {
                return (val + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }

}
