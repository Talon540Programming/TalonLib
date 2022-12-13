package org.talon540.sensors.vision.Limelight;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.jetbrains.annotations.NotNull;
import org.talon540.sensors.vision.VisionCameraMountConfig;
import org.talon540.sensors.vision.VisionFlags.CAMMode;
import org.talon540.sensors.vision.VisionFlags.LEDStates;
import org.talon540.sensors.vision.VisionState;
import org.talon540.sensors.vision.VisionSystem;

/**
 * An object used to get data and manipulate the state of a limelight camera
 */
public class LimelightVision extends VisionSystem {
    private final NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    /**
     * Construct a limelight object
     *
     * @param cameraPlacement camera placement relative to the robot
     * @param camMode camera mode to use
     * @param pipeline pipeline to set processing for
     */
    public LimelightVision(@NotNull VisionCameraMountConfig cameraPlacement, CAMMode camMode, int pipeline) {
        super(cameraPlacement);

        setCamMode(camMode);
        setPipelineIndex(pipeline);
    }

    /**
     * Create a limelight object with the LEDs and Pipeline set to default
     *
     * @param cameraPlacement camera placement relative to the robot
     */
    public LimelightVision(@NotNull VisionCameraMountConfig cameraPlacement) {
        this(
                cameraPlacement,
                CAMMode.PROCESSING,
                0
        );
    }

    @Override
    public LEDStates getLEDMode() {
        return switch ((int) limelightTable.getEntry("ledMode").getDouble(0)) {
            case 1 -> LEDStates.OFF;
            case 2 -> LEDStates.BLINK;
            case 3 -> LEDStates.ON;
            default -> LEDStates.DEFAULT;
        };
    }

    @Override
    public void setLEDMode(LEDStates state) {
        NetworkTableEntry ledEntry = limelightTable.getEntry("ledMode");
        switch (state) {
            case OFF -> ledEntry.setNumber(1);
            case BLINK -> ledEntry.setNumber(2);
            case ON -> ledEntry.setNumber(3);
            case DEFAULT -> ledEntry.setNumber(0);
        }
    }

    @Override
    public int getPipelineIndex() {
        return (int) limelightTable.getEntry("getpipe").getDouble(0);
    }

    @Override
    public void setPipelineIndex(int index) {
        if (!(0 <= index && index <= 9))
            throw new IllegalArgumentException("Pipeline must be within 0-9");

        limelightTable.getEntry("pipeline").setNumber(index);
    }

    @Override
    public CAMMode getCamMode() {
        return switch ((int) limelightTable.getEntry("camMode").getDouble(-1)) {
            case 0 -> CAMMode.PROCESSING;
            case 1 -> CAMMode.DRIVER;
            default -> CAMMode.INVALID;
        };

    }

    @Override
    public void setCamMode(CAMMode targetMode) {
        limelightTable.getEntry("camMode").setNumber(targetMode.val);
    }

    @Override
    public boolean targetViewed() {
        return limelightTable.getEntry("tv").getDouble(0) != 0;
    }

    @Override
    public VisionState getVisionState() {
        if (!targetViewed())
            return null;

        return new VisionState(
                limelightTable.getEntry("tx").getDouble(0),
                limelightTable.getEntry("ty").getDouble(0),
                limelightTable.getEntry("ts").getDouble(0),
                limelightTable.getEntry("ta").getDouble(0),
                limelightTable.getEntry("tl").getDouble(0)
        );
    }
}
