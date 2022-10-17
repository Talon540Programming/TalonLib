package org.talon540.vision.Limelight;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.Timer;

/**
 * An object used to get data and manipulate the state of a limelight camera
 */
public class LimelightVision extends SubsystemBase {
    private String tableName = "limelight";
    private double mountAngle, mountHeight;

    public boolean targetViewed;
    public double offsetX, offsetY, targetArea, targetSkew, piplineLatencyMS, totalEstimatedLatencyMS;
    public Double nonZeroX, nonZeroY = null;

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

        NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable(this.tableName);

        limelightTable.getEntry("pipeline").setNumber(0);
        limelightTable.getEntry("camMode").setNumber(0);

        limelightTable.getEntry("tv").addListener(event -> {
            this.targetViewed = event.value.getDouble() == 1.0;
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("tx").addListener(event -> {
            this.offsetX = event.value.getDouble();
            if(this.offsetX != 0) nonZeroX = this.offsetX;
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("ty").addListener(event -> {
            this.offsetY = event.value.getDouble();
            if(this.offsetY != 0) nonZeroY = this.offsetY;
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("ta").addListener(event -> {
            this.targetArea = event.value.getDouble();
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("ts").addListener(event -> {
            this.targetSkew = event.value.getDouble();
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("tl").addListener(event -> {
            this.piplineLatencyMS = event.value.getDouble();
            this.totalEstimatedLatencyMS = this.piplineLatencyMS + 11;
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("getpipe").addListener(event -> {
            this.pipeline = (int) event.value.getDouble();
            this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("ledMode").addListener(event -> {
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
            NetworkTableInstance.getDefault().getTable(this.tableName).getEntry("pipeline").setNumber(piplineID);
        }
    }

    /**
     * This function sets the LEDS to off
     */
    public void disableLEDS() {
        this.setLEDState(LimelightLEDStates.OFF);
    }

    /**
     * This function sets the LEDS to on
     */
    public void enableLEDS() {
        this.setLEDState(LimelightLEDStates.ON);
    }

    /**
     * This function sets the LEDS to blink
     */
    public void blinkLEDS() {
        this.setLEDState(LimelightLEDStates.BLINK);
    }

    /**
     * This function sets the LED mode of the Limelight
     * 
     * @param dLedStates The state of the LEDS.
     */
    public void setLEDState(LimelightLEDStates dLedStates) {
        NetworkTableEntry ledEntry = NetworkTableInstance.getDefault().getTable(this.tableName).getEntry("ledMode");
        switch (dLedStates) {
            case ON:
            ledEntry.setNumber(3); // light on
                break;

            case OFF:
            ledEntry.setNumber(1); // light off
                break;

            case BLINK:
            ledEntry.setNumber(2); // light blinking
                break;

            case DEFAULT:
            default:
            ledEntry.setNumber(0); // as per pipeline mode (usually on)

        }
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType(this.tableName);

        builder.addDoubleProperty("stateChangeTimestamp", () -> this.visionStateTimestamp, null);
        builder.addBooleanProperty("targetInView", () -> this.targetViewed, null);
        builder.addDoubleProperty("offsetX", () -> this.offsetX, null);
        builder.addDoubleProperty("offsetY", () -> this.offsetY, null);
        builder.addDoubleProperty("pipelineLatency", () -> this.piplineLatencyMS, null);
        builder.addDoubleProperty("pipeline", () -> this.pipeline, null);
        builder.addStringProperty("LED Mode", () -> this.LEDState.toString(), null);

    }
}
