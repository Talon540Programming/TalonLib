package org.talon540;

import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class TalonFX_DifferentialMotorGroup extends MotorControllerGroup {
    private TalonFXSensorCollection sensorGroup;

    public TalonFX_DifferentialMotorGroup(WPI_TalonFX leader, WPI_TalonFX follower) {
        super(leader, follower);

        sensorGroup = leader.getSensorCollection();
    }

    /**
     * Return the velocity of the motor group
     * Return the TalonFX integrated sensor velocity in {@code CTRE ticks per 100ms}
     */
    public double getVelocity() {
        return sensorGroup.getIntegratedSensorVelocity();
    }

    /**
     * Return the position of the motor group
     * Return the TalonFX integrated sensor velocity in {@code CTRE ticks per 100ms}
     */
    public double getPosition() {
        return sensorGroup.getIntegratedSensorPosition();
    }
}
