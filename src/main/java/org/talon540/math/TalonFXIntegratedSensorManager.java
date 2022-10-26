package org.talon540.math;

import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;

public class TalonFXIntegratedSensorManager {
    private final TalonFXSensorCollection collection;
    private final double kGearRatio;
    private final double kRadius;

    /**
     * Construct sensor manager using integrated sensor collection and dimensions of attached objects
     * @param collection {@link TalonFXSensorCollection} from the motor controller
     * @param radius radius of the attached item in meters
     * @param gearRatio gear ratio between the motor and the object. If it is not linear i.e. drivetrain gearbox is 54:20, enter 54.0/20.0
     */
    public TalonFXIntegratedSensorManager(TalonFXSensorCollection collection, double radius, double gearRatio) {
        this.collection = collection;
        this.kGearRatio = gearRatio;
        this.kRadius = radius;
    }

    /**
     * Construct sensor manager using integrated sensor collection and dimensions of attached objects.
     * @param collection {@link TalonFXSensorCollection} from the motor controller
     * @param radius radius of the attached item in meters
     * Note:  Assumes there is no gearbox attached (1:1) gear ratio
     */
    public TalonFXIntegratedSensorManager(TalonFXSensorCollection collection, double radius) {
        this(collection, radius, 1);
    }

    /** Get the angular velocity in {@code rad/s} */
    public double getAngularVelocity() {
        return conversions.Falcon500VelocityToAngularVelocity(collection.getIntegratedSensorVelocity(), kGearRatio);
    }

    /** Get the linear velocity in meters per second */
    public double getLinearVelocity() {
        return conversions.Falcon500VelocityToLinearVelocity(collection.getIntegratedSensorVelocity(), kRadius, kGearRatio);
    }

    /** Get the RPM of the motor */
    public double getRPM() {
        return conversions.Falcon500VelocityToRPM(collection.getIntegratedSensorVelocity());
    }

    /** Get the RPM of the object attached to the motor */
    public double getObjectRPM() {
        return getRPM() / kGearRatio;
    }

    /** Get the position of the encoder in meters */
    public double getPosition() {
        return (collection.getIntegratedSensorPosition() / kGearRatio) * ((2 *  Math.PI * kRadius) / 2048.0);
    }

    /** Return the raw velocity of the motor in CTRE encoder ticks / 100ms */
    public double getRawVelocity() {
        return collection.getIntegratedSensorVelocity();
    }

    /** Return the position of the encoder */
    public double getRawPosition(){
        return collection.getIntegratedSensorPosition();
    }

    /** Return the absolute position of the encoder */
    public double getRawAbsolutePosition() {
        return collection.getIntegratedSensorAbsolutePosition();
    }
}
