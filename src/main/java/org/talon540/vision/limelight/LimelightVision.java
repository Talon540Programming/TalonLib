package org.talon540.vision.Limelight;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * An object used to get data and manipulate the state of a limelight camera
 */
public class LimelightVision extends SubsystemBase {
    private final String tableName = "limelight";

    private final double mountAngle, mountHeight;

    /**
     * Is the target currently in the viewport of the limelight
     */
    public boolean targetViewed;

    /**
     * Horizontal offset from the crosshair
     */
    public double offsetX;
    /**
     * Vertical offset from the crosshair
     */
    public double offsetY;
    /**
     * Horizontal offset from the crosshair. Doesnt record 0 values in case the target is lost from view
     */
    public double nonZeroX;
    /**
     * Vertical offset from the crosshair. Doesnt record 0 values in case the target is lost from view
     */
    public double nonZeroY;
    /**
     * Area of the total screen of the target
     */
    public double targetArea;
    /**
     * Target skew or rotation
     */
    public double targetSkew;
    /**
     * Latency of the calculations of the computer vision processing
     */
    public double piplineLatencyMS;
    /**
     * Timestamp of the last time the state was updated.
     * Accounts for pipeline latency
     */
    public double visionStateTimestamp;

    /**
     * Construct a limelight object
     * 
     * @param mountAngle  mount angle of the limelight in degrees
     * @param mountHeight mount height of the center of the limelight's lens from
     *                    the floor in meters
     */
    public LimelightVision(double mountAngle, double mountHeight, int ledMode, int pipeline) {
        setName(this.tableName);
        this.mountAngle = mountAngle;
        this.mountHeight = mountHeight;

        NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable(this.tableName);

        limelightTable.getEntry("pipeline").setNumber(pipeline);
        limelightTable.getEntry("camMode").setNumber(ledMode);

        limelightTable.getEntry("NZtx").setDefaultDouble(0);
        limelightTable.getEntry("NZtx").setPersistent();
        limelightTable.getEntry("NZty").setDefaultDouble(0);
        limelightTable.getEntry("NZty").setPersistent();

        limelightTable.getEntry("tv").addListener(event -> {
            this.targetViewed = event.value.getDouble() == 1.0;
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("tx").addListener(event -> {
            this.offsetX = event.value.getDouble();
            if (this.offsetX != 0) limelightTable.getEntry("NZtx").setDouble(this.offsetX);
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("ty").addListener(event -> {
            this.offsetY = event.value.getDouble();
            if (this.offsetY != 0) limelightTable.getEntry("NZty").setDouble(this.offsetY);
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("ta").addListener(event -> {
            this.targetArea = event.value.getDouble();
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("ts").addListener(event -> {
            this.targetSkew = event.value.getDouble();
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("tl").addListener(event -> {
            this.piplineLatencyMS = event.value.getDouble();
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("NZtx").addListener(event -> {
            this.nonZeroX = event.value.getDouble();
        }, EntryListenerFlags.kUpdate);
        limelightTable.getEntry("NZtv").addListener(event -> {
            this.nonZeroY = event.value.getDouble();
        }, EntryListenerFlags.kUpdate);

    }

    /**
     * Create a limelight object with the LEDs and Pipeline set to default
     * 
     * @param mountAngle  mount angle of the limelight in degrees
     * @param mountHeight mount height of the center of the limelight's lens from
     *                    the floor in meters
     */
    public LimelightVision(double mountAngle, double mountHeight) {
        this(mountAngle, mountHeight, 0, 0);
    }

    @Override
    public void periodic() {
        this.visionStateTimestamp = Timer.getFPGATimestamp() - (this.piplineLatencyMS / 1000.0) + 0.011;
    }

    /**
     * Get distance from a specified target's base. Follows
     * https://docs.limelightvision.io/en/latest/cs_estimating_distance.html
     * 
     * @param targetHeight height of the retroreflector in meters. Already offsets
     *                     for mount height
     * @return distance from the base of the target in {@code meters}. Returns
     *         {@link Double#NaN} if target is not found or value is unrealistic
     */
    public double getDistanceFromTargetBase(double targetHeight) {
        if (!targetViewed)
            return Double.NaN;

        double deltaAngle = Math.toRadians(this.mountAngle + this.offsetY);
        return (targetHeight - this.mountHeight) / Math.tan(deltaAngle);
    }

    /**
     * Get distance from a specified target (Hypotenuse). Follows
     * https://docs.limelightvision.io/en/latest/cs_estimating_distance.html
     * 
     * @param targetHeight height of the retroreflector in meters. Already offsets
     *                     for mount height
     * @return distance from the target in {@code meters}. Returns
     *         {@link Double#NaN} if target is not found or value is unrealistic
     */
    public double getDistanceFromTarget(double targetHeight) {
        if (!targetViewed)
            return Double.NaN;

        double deltaAngle = Math.toRadians(this.mountAngle + this.offsetY);

        return (targetHeight - this.mountHeight) / Math.sin(deltaAngle);
    }

    /**
     * Set the limelight's pipeline
     * 
     * @param piplineID pipline id within [0,9]
     */
    public void setPipeline(int piplineID) {
        if (!(0 <= piplineID && piplineID <= 9))
            throw new IllegalArgumentException("Pipeline must be within 0-9");

        NetworkTableInstance.getDefault().getTable(this.tableName).getEntry("pipeline").setNumber(piplineID);
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
            case OFF:
                ledEntry.setNumber(1); // light off
                break;

            case BLINK:
                ledEntry.setNumber(2); // light blinking
                break;

            case ON:
                ledEntry.setNumber(3); // light on
                break;

            case DEFAULT:
            default:
                ledEntry.setNumber(0); // as per pipeline mode (usually on)

        }
    }

    public LimelightLEDStates getLEDState() {
        NetworkTableEntry ledEntry = NetworkTableInstance.getDefault().getTable(this.tableName).getEntry("ledMode");

        switch (ledEntry.getNumber(0).intValue()) {
            default:
            case 0:
                return LimelightLEDStates.DEFAULT;
            case 1:
                return LimelightLEDStates.OFF;
            case 2:
                return LimelightLEDStates.BLINK;
            case 3:
                return LimelightLEDStates.ON;
        }
    }

    /**
     * Get the current pipeline
     * 
     * @return
     */
    public double getPipeline() {
        return (int) NetworkTableInstance.getDefault().getTable(this.tableName).getEntry("getpipe").getDouble(0);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addBooleanProperty("targetInView", () -> this.targetViewed, null);
        builder.addDoubleProperty("tX", () -> this.offsetX, null);
        builder.addDoubleProperty("tY", () -> this.offsetY, null);
        builder.addDoubleProperty("NZtX", () -> this.nonZeroX, null);
        builder.addDoubleProperty("NZtY", () -> this.nonZeroY, null);
        builder.addDoubleProperty("pLatency", () -> this.piplineLatencyMS, null);
        builder.addDoubleProperty("sTimestamp", () -> this.visionStateTimestamp, null);
        builder.addDoubleProperty("pipeId", this::getPipeline, null);
        builder.addStringProperty("LED Mode", () -> this.getLEDState().toString(), (targetMode) -> {
            switch (targetMode.toLowerCase()) {
                case "on":
                    setLEDState(LimelightLEDStates.ON);
                    break;
                case "off":
                    setLEDState(LimelightLEDStates.OFF);
                    break;
                case "blink":
                    setLEDState(LimelightLEDStates.BLINK);
                    break;
                default:
                    setLEDState(LimelightLEDStates.DEFAULT);
                    break;
            }
        });
    }

}
