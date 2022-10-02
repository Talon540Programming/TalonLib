package org.talon540.vision.limelight;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.Timer;

public class LimelightVision extends SubsystemBase {
    private String tableName = "limelight";
    private volatile NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable(this.tableName);;
    private double mountAngle, mountHeight;

    public boolean targetViewed;
    public double offsetX, offsetY, targetArea, targetSkew, piplineLatencyMS, totalEstimatedLatencyMS;
    public int pipeline;
    public LimelightLEDStates LEDState;

    /** Timestamp of the last time the state was updated */
    public double visionStateTimestamp = 0;

    /**
     * Construct a limelight object
     * 
     * @param mountAngle  mount angle of the limelight in degrees
     * @param mountHeight mount height of the limelight from the floor in meters
     */
    public LimelightVision(double mountAngle, double mountHeight) {
        setName(this.tableName);
        this.mountAngle = mountAngle;
        this.mountHeight = mountHeight;

        this.limelightTable.getEntry("pipeline").setNumber(0);
        this.limelightTable.getEntry("camMode").setNumber(0);

        this.limelightTable.getEntry("tv").addListener(event -> {
            this.targetViewed = event.value.getDouble() == 1.0;
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        this.limelightTable.getEntry("tx").addListener(event -> {
            this.offsetX = event.value.getDouble();
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        this.limelightTable.getEntry("ty").addListener(event -> {
            this.offsetY = event.value.getDouble();
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        this.limelightTable.getEntry("ta").addListener(event -> {
            this.targetArea = event.value.getDouble();
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        this.limelightTable.getEntry("ts").addListener(event -> {
            this.targetSkew = event.value.getDouble();
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        this.limelightTable.getEntry("tl").addListener(event -> {
            this.piplineLatencyMS = event.value.getDouble();
            this.totalEstimatedLatencyMS = this.piplineLatencyMS + 11;
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        this.limelightTable.getEntry("getpipe").addListener(event -> {
            this.pipeline = (int) event.value.getDouble();
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        this.limelightTable.getEntry("ledMode").addListener(event -> {
            switch ((int) event.value.getDouble()) {
                case 0:
                    this.LEDState = LimelightLEDStates.DEFAULT;
                    break;
                case 1:
                    this.LEDState = LimelightLEDStates.OFF;

                    break;
                case 2:
                    this.LEDState = LimelightLEDStates.BLINK;

                    break;
                default:
                case 3:
                    this.LEDState = LimelightLEDStates.ON;
                    break;
            }
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);

    }

    /**
     * Get distance from a specified target's base
     * 
     * @param targetHeight height of the retroreflector in meters
     * @return distance from the base of the target in meters
     */
    public double getDistanceFromTargetBase(double targetHeight) {
        double verticalOffset = Math.toRadians(this.offsetY);
        double limelightAngle = Math.toRadians(this.mountAngle);

        return ((targetHeight - this.mountHeight) / (Math.tan((limelightAngle + verticalOffset))));
    }

    /**
     * Get distance from a specified target (Hypotenuse)
     * 
     * @param targetHeight height of the retroreflector in meters
     * @return distance from the target in meters
     */
    public double getDistanceFromTarget(double targetHeight) {
        double verticalOffset = Math.toRadians(this.offsetY);
        double limelightAngle = Math.toRadians(this.mountAngle);

        return ((targetHeight - this.mountHeight) / (Math.sin((limelightAngle + verticalOffset))));
    }

    /**
     * Set the limelight's pipeline
     * 
     * @param piplineID pipline id within [0,9]
     */
    public void setPipeline(int piplineID) {
        if (0 <= piplineID && piplineID <= 9) {
            this.limelightTable.getEntry("pipeline").setNumber(piplineID);
        }
    }

    /**
     * This function sets the LEDS to off
     */
    public void disableLEDS() {
        this.setLEDState(LimelightLEDStates.OFF);
    }

    /**
     * This function sets the LED mode of the Limelight
     * 
     * @param dLedStates The state of the LEDS.
     */
    public void setLEDState(LimelightLEDStates dLedStates) {
        switch (dLedStates) {
            case ON:
                this.limelightTable.getEntry("ledMode").setNumber(3); // light on
                break;

            case OFF:
                this.limelightTable.getEntry("ledMode").setNumber(1); // light off
                break;

            case BLINK:
                this.limelightTable.getEntry("ledMode").setNumber(2); // light blinking
                break;

            case DEFAULT:
            default:
                this.limelightTable.getEntry("ledMode").setNumber(0); // as per pipeline mode (usually on)

        }
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType(this.tableName);

        builder.addDoubleProperty("stateChangeTimestamp", () -> {
            return this.visionStateTimestamp;
        }, null);
        builder.addBooleanProperty("targetInView", () -> {
            return this.targetViewed;
        }, null);
        builder.addDoubleProperty("offsetX", () -> {
            return this.offsetX;
        }, null);
        builder.addDoubleProperty("offsetY", () -> {
            return this.offsetY;
        }, null);
        builder.addDoubleProperty("pipelineLatency", () -> {
            return this.piplineLatencyMS;
        }, null);
        builder.addDoubleProperty("pipeline", () -> {
            return this.pipeline;
        }, null);

    }
}
