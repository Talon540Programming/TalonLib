package org.talon540.control;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.button.Button;

public class BatteryVoltageCheck extends Button {
    private final double min;

    /**
     * Set the minimum voltage required before the battery safety reporting process starts
     *
     * @param minVoltage minimum voltage [0v, 12v]
     */
    public BatteryVoltageCheck(double minVoltage) {
        if (!(0 <= minVoltage && minVoltage <= 12))
            throw new IllegalArgumentException("Minimum voltage must be between 0 and 12 volts");
        this.min = minVoltage;
    }

    public BatteryVoltageCheck() {
        this(11);
    }

    @Override
    public boolean get() {
        return RobotController.getBatteryVoltage() < min;
    }
}
