package org.talon540.control.XboxController;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.event.BooleanEvent;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
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
     * @return deadband X value from the left joystick
     */
    public double getLeftDeadbandX() {
        return MathUtil.applyDeadband(getLeftX(), this.deadband);
    }

    /**
     * Get the Y value from the left joystick and check if it is within the deadband provided
     *
     * @return deadband Y value from the left joystick
     */
    public double getLeftDeadbandY() {
        return MathUtil.applyDeadband(getLeftY(), this.deadband);
    }

    /**
     * Get the X value from the right joystick and check if it is within the deadband provided
     *
     * @return deadband X value from the right joystick
     */
    public double getRightDeadbandX() {
        return MathUtil.applyDeadband(getRightX(), this.deadband);
    }

    /**
     * Get the Y value from the right joystick and check if it is within the deadband provided
     *
     * @return deadband Y value from the right joystick
     */
    public double getRightDeadbandY() {
        return MathUtil.applyDeadband(getRightY(), this.deadband);
    }

    /**
     * Get a trigger that returns true if the trigger axis is above 20%
     * @param loop the event loop instance to attach the event to.
     */
    public Trigger getLeftTrigger(EventLoop loop) {
        return new BooleanEvent(loop, () -> getLeftTriggerAxis() > 0.2).castTo(Trigger::new);
    }

    /**
     * Get a trigger that returns true if the trigger axis is above 20%. Uses default button EventLoop
     */
    public Trigger getLeftTrigger() {
        return getLeftTrigger(CommandScheduler.getInstance().getDefaultButtonLoop());
    }

    /**
     * Get a trigger that returns true if the trigger axis is above 20%
     * @param loop the event loop instance to attach the event to.
     */
    public Trigger getRightTrigger(EventLoop loop) {
        return new BooleanEvent(loop, () -> getRightTriggerAxis() > 0.2).castTo(Trigger::new);
    }

    /**
     * Get a trigger that returns true if the trigger axis is above 20%. Uses default button EventLoop
     */
    public Trigger getRightTrigger() {
        return getRightTrigger(CommandScheduler.getInstance().getDefaultButtonLoop());
    }

    /**
     * Change the currently set deadband. Must be within [0, 1]
     *
     * @param deadband new deadband val
     */
    public void setDeadband(double deadband) {
        if (deadband > 1 || 0 > deadband)
            throw new IllegalArgumentException("Deadband cannot exceed max magnitude");
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
     * @param percent percent in [0, 1]
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
     * Start rumbling the controller at 100%
     */
    public void startRumble() {
        startRumble(1);
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
        return new InstantCommand(this::startRumble);
    }

    /**
     * Get an instant command that stops rumbling the controller
     *
     * @return rumble stop command
     */
    public InstantCommand getStopRumble() {
        return new InstantCommand(this::stopRumble);
    }
}
