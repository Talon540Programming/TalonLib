package org.talon540.math;

public abstract class conversions {

    /**
     * Return the velocity of a wheel from it's RPM
     * 
     * @param RPM    RPM of the wheel
     * @param radius radius of the rotating object (unit used is the unit returned)
     * @return Linear velocity of the wheel in the unit of the rotating object's
     *         radius
     */
    public static double RPMtoLinearVelocity(double RPM, double radius) {
        return RPMtoAngularVelocty(RPM) * radius;
    }

    /**
     * Convert RPM to angular velocity
     * 
     * @param RPM
     * @return Angular velocity in {@code rad/s}
     */
    public static double RPMtoAngularVelocty(double RPM) {
        return RPM * Math.PI / 30.0;
    }

    /**
     * Convert linear velocity of a rotating object to it's RPM
     * 
     * @param linearVelocity
     * @param radius
     * @return RPM
     */
    public static double LinearVelocityToRPM(double linearVelocity, double radius) {
        return AngularVelocityToRPM(linearVelocity) / radius;
    }

    /**
     * Convert the angular velocity of a rotating object to it's RPM
     * 
     * @param angularVelocity in {@code rad/s}
     * @return RPM
     */
    public static double AngularVelocityToRPM(double angularVelocity) {
        return angularVelocity * 30.0 / Math.PI;
    }

    /**
     * Convert the velocity reported by the {@code TalonFX} integrated sensor to the
     * linear velocity of something connected to it
     * 
     * @param FXVel     the velocity reported by the motor in encoder
     *                  {@code ticks / 100ms}
     * @param radius    radius of the item attached to motor
     * @param gearRatio gear ratio between the two
     * @return linear velocity of the rotating item
     */
    public static double Falcon500VelocityToLinearVelocity(double FXVel, double radius, double gearRatio) {
        return RPMtoLinearVelocity(Falcon500VelocityToRPM(FXVel), radius) / gearRatio;
    }

    /**
     * Convert the linear velocity of something connected to a {@code TalonFX} to a
     * CTRE velocity
     * 
     * @param linearVelocity linear velocity in the same unit as radius
     * @param radius         linear velocity in the same unit as radius
     * @param gearRatio      gear ratio between the rotating object and the motor
     * @return CTRE TalonFX Velocity in {@code ticks / 100ms}
     */
    public static double LinearVelocityToFalcon500Velocity(double linearVelocity, double radius, double gearRatio) {
        return RPMtoFalcon500Velocity(LinearVelocityToRPM(linearVelocity, radius)) * gearRatio;
    }

    /**
     * Convert the velocity reported by the {@code TalonFX} integrated sensor to the
     * angular velocity of something connected to it
     * 
     * @param FXVel     the velocity reported by the motor in encoder
     *                  {@code ticks / 100ms}
     * @param gearRatio gear ratio between the two
     * @return angular velocity of the rotating item in {@code rad/s}
     */
    public static double Falcon500VelocityToAngularVelocity(double FXVel, double gearRatio) {
        return RPMtoAngularVelocty(Falcon500VelocityToRPM(FXVel)) / gearRatio;
    }

    /**
     * Convert the angular velocity of something connected to a {@code TalonFX} to a
     * CTRE velocity
     * 
     * @param angularVelocity angular velocity of the rotating object in
     *                        {@code rad/s}
     * @param gearRatio       gear ratio between the rotating object and the motor
     * @return CTRE TalonFX Velocity in {@code ticks / 100ms}
     */
    public static double AngularVelocityToFalcon500Velocity(double angularVelocity, double gearRatio) {
        return RPMtoFalcon500Velocity(AngularVelocityToRPM(angularVelocity)) * gearRatio;
    }

    /**
     * Convert the velocity reported by the {@code TalonFX} integrated sensor to the
     * linear velocity of something connected to it
     * 
     * @implNote this method assumes a gear ratio of 1 (i.e nothing between the two)
     * @param FXVel  the velocity reported by the motor in encoder
     *               {@code ticks / 100ms}
     * @param radius radius of the item attached to motor
     * @return linear velocity of the rotating item
     */
    public static double Falcon500VelocityToLinearVelocity(double FXVel, double radius) {
        return Falcon500VelocityToLinearVelocity(FXVel, radius, 1);
    }

    /**
     * Convert the linear velocity of something connected to a {@code TalonFX} to a
     * CTRE velocity
     * 
     * @implNote this method assumes a gear ratio of 1 (i.e nothing between the two)
     * @param linearVelocity linear velocity in the same unit as radius
     * @param radius         linear velocity in the same unit as radius
     * @return CTRE TalonFX Velocity in {@code ticks / 100ms}
     */
    public static double LinearVelocityToFalcon500Velocity(double linearVelocity, double radius) {
        return LinearVelocityToFalcon500Velocity(linearVelocity, radius, 1);
    }

    /**
     * Convert the velocity reported by the {@code TalonFX} integrated sensor to the
     * angular velocity of something connected to it
     * 
     * @implNote this method assumes a gear ratio of 1 (i.e nothing between the two)
     * @param FXVel the velocity reported by the motor in encoder
     *              {@code ticks / 100ms}
     * @return angular velocity of the rotating item in {@code rad/s}
     */
    public static double Falcon500VelocityToAngularVelocity(double FXVel) {
        return Falcon500VelocityToAngularVelocity(FXVel, 1);
    }

    /**
     * Convert the angular velocity of something connected to a {@code TalonFX} to a
     * CTRE velocity
     * 
     * @implNote this method assumes a gear ratio of 1 (i.e nothing between the two)
     * @param angularVelocity angular velocity of the rotating object in
     *                        {@code rad/s}
     * @return CTRE TalonFX Velocity in {@code ticks / 100ms}
     */
    public static double AngularVelocityToFalcon500Velocity(double angularVelocity) {
        return AngularVelocityToFalcon500Velocity(angularVelocity, 1);
    }

    /**
     * Conver linear velocity to angular velocity
     * 
     * @return angular velocity in {@code rad/s}
     */
    public static double LinearVelocityToAngularVelocity(double linearVelocity, double radius) {
        return linearVelocity / radius;
    }

    /**
     * Convert angular velocity to linear velocity
     * 
     * @param angularVelocity angular velocity in {@code rad/s}
     */
    public static double AngularVelocityToLinearVelocity(double angularVelocity, double radius) {
        return angularVelocity * radius;
    }

    /**
     * Convert the CTRE velocity reported by a Falcon500's integrated sensor to RPM
     * 
     * @param FXVel
     * @return CTRE velocity in {@code ticks / 100ms}
     */
    public static double Falcon500VelocityToRPM(double FXVel) {
        return FXVel * 600.0 / 2048.0;
    }

    /**
     * Convert RPM to CTRE velocity useable by a Falon500
     * 
     * @param RPM
     * @return CTRE velocity in {@code ticks / 100ms}
     */
    public static double RPMtoFalcon500Velocity(double RPM) {
        return RPM * 2048.0 / 600.0;
    }

}
