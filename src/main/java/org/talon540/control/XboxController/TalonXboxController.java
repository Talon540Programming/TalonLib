package org.talon540.control.XboxController;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class TalonXboxController extends CommandXboxController {
    private double deadband;

    /**
     * @param port The port index on the Driver Station that the controller is plugged into.
     * @param deadband minimum percent required to bypass deadband.
     */
    public TalonXboxController(int port, double deadband) {
        super(port);

        this.deadband = deadband;
    }

    /**
     * Construct an instance of a controller with a default deadband
     *
     * @param port The port index on the Driver Station that the controller is plugged into.
     */
    public TalonXboxController(int port) {
        this(
                port,
                0.05
        );
    }

    /**
     * Get the X value from the left joystick and check if it is within the deadband provided
     *
     * @return normalized X
     */
    public double getLeftDeadbandX() {
        return checkDeadband(super.getLeftX());
    }

    /**
     * Get the Y value from the left joystick and check if it is within the deadband provided
     *
     * @return normalized Y
     */
    public double getLeftDeadbandY() {
        return checkDeadband(super.getLeftY());
    }

    /**
     * Get the X value from the right joystick and check if it is within the deadband provided
     *
     * @return normalized X
     */
    public double getRightDeadbandX() {
        return checkDeadband(super.getRightX());
    }

    /**
     * Get the Y value from the right joystick and check if it is within the deadband provided
     *
     * @return normalized Y
     */
    public double getRightDeadbandY() {
        return checkDeadband(super.getRightY());
    }

    /**
     * Get a trigger that returns true if the trigger axis is above 20%
     */
    public Trigger getLeftTrigger() {
        return new Trigger(() -> getLeftTriggerAxis() > 0.2);
    }

    /**
     * Get a trigger that returns true if the trigger axis is above 20%
     */
    public Trigger getRightTrigger() {
        return new Trigger(() -> getRightTriggerAxis() > 0.2);
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

    /**
     * Start rumbling both sides of the controller to some percent
     *
     * @param percent percent in [-1,1]
     */
    public void startRumble(double percent) {
        this.getHID().setRumble(
                GenericHID.RumbleType.kLeftRumble,
                percent
        );
        this.getHID().setRumble(
                GenericHID.RumbleType.kRightRumble,
                percent
        );
    }

    /**
     * Stop the controller from rumbling
     */
    public void stopRumble() {
        this.getHID().setRumble(
                GenericHID.RumbleType.kLeftRumble,
                0
        );
        this.getHID().setRumble(
                GenericHID.RumbleType.kRightRumble,
                0
        );
    }

    /**
     * Get an instant command that starts rumbling the controller at 100%
     *
     * @return rumble start command
     */
    public InstantCommand getStartRumble() {
        return new InstantCommand(() -> this.startRumble(1));
    }

    /**
     * Get an instant command that stops rumbling the controller
     *
     * @return rumble stop command
     */
    public InstantCommand getStopRumble() {
        return new InstantCommand(this::stopRumble);
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
