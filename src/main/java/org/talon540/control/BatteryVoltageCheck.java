package org.talon540.control;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.button.Button;


public class BatteryVoltageCheck extends Button {
    private final double min;

    /**
     * Set the minimum voltage required before the battery safety reporting process starts
     *
     * @param minVoltage minimum voltage [0v, 12v]
     */
    public BatteryVoltageCheck(double minVoltage) {this.min = MathUtil.clamp(minVoltage, 0, 12);}

    @Override
    public boolean get() {
        return RobotController.getBatteryVoltage() < min;
    }
}
