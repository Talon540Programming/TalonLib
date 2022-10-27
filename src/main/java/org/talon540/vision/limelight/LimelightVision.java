package org.talon540.vision.Limelight;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.talon540.vision.TalonVisionState;
import org.talon540.vision.TalonVisionSystem;
import org.talon540.vision.VisionFlags.CAMMode;
import org.talon540.vision.VisionFlags.LEDStates;


/**
 * An object used to get data and manipulate the state of a limelight camera
 */
public class LimelightVision implements TalonVisionSystem {
    private final double mountAngle, mountHeight;

    /**
     * Construct a limelight object
     *
     * @param mountAngle mount angle of the limelight in degrees
     * @param mountHeight mount height of the center of the limelight's lens from the floor in meters
     * @param camMode camera mode to use
     * @param pipeline pipeline to set processing for
     */
    public LimelightVision(double mountAngle, double mountHeight, CAMMode camMode, int pipeline) {
        this.mountAngle = mountAngle;
        this.mountHeight = mountHeight;


        setPipelineIndex(pipeline);
        setCamMode(camMode);
    }

    /**
     * Create a limelight object with the LEDs and Pipeline set to default
     *
     * @param mountAngle mount angle of the limelight in degrees
     * @param mountHeight mount height of the center of the limelight's lens from the floor in meters
     */
    public LimelightVision(double mountAngle, double mountHeight) {
        this(mountAngle, mountHeight, CAMMode.PROCESSING, 0);
    }

    @Override
    public TalonVisionState getVisionState() {
        NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("pipeline");

        if (limelightTable.getEntry("tv").getDouble(0) == 0)
            return null;

        return new TalonVisionState(
                limelightTable.getEntry("tx").getDouble(0),
                limelightTable.getEntry("ty").getDouble(0),
                null,
                null,
                limelightTable.getEntry("ts").getDouble(0),
                limelightTable.getEntry("ta").getDouble(0),
                null,
                limelightTable.getEntry("tl").getDouble(0)
        );
    }

    @Override
    public void setCamMode(CAMMode targetMode) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(targetMode.val);
    }

    @Override
    public CAMMode getCamMode() {
        switch (NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").getNumber(-1).intValue()) {
            case 0:
                return CAMMode.PROCESSING;
            case 1:
                return CAMMode.DRIVER;
            default:
            case -1:
                return CAMMode.INVALID;

        }

    }

    @Override
    public Double getDistanceFromTarget(double targetHeight) {
        if (!targetViewed())
            return null;
        double deltaAngle = Math.toRadians(this.mountAngle + getVisionState().getOffsetY());
        return (targetHeight - this.mountHeight) / Math.sin(deltaAngle);
    }

    @Override
    public Double getDistanceFromTargetBase(double targetHeight) {
        if (!targetViewed())
            return null;
        double deltaAngle = Math.toRadians(this.mountAngle + getVisionState().getOffsetY());
        return (targetHeight - this.mountHeight) / Math.tan(deltaAngle);
    }

    @Override
    public void setPipelineIndex(int index) {
        if (!(0 <= index && index <= 9))
            throw new IllegalArgumentException("Pipeline must be within 0-9");

        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(index);
    }

    @Override
    public void setLEDState(LEDStates state) {
        NetworkTableEntry ledEntry = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode");
        switch (state) {
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

    @Override
    public LEDStates getLEDState() {
        NetworkTableEntry ledEntry = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode");

        switch (ledEntry.getNumber(0).intValue()) {
            default:
            case 0:
                return LEDStates.DEFAULT;
            case 1:
                return LEDStates.OFF;
            case 2:
                return LEDStates.BLINK;
            case 3:
                return LEDStates.ON;
        }
    }

    @Override
    public int getPipelineIndex() {
        return (int) NetworkTableInstance.getDefault().getTable("limelight").getEntry("getpipe").getDouble(0);
    }

    @Override
    public void initSendable(SendableBuilder builder) {

    }
}
