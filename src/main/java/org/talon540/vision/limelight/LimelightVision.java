package org.talon540.vision.Limelight;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.talon540.math.Vector2d;
import org.talon540.vision.TalonVisionState;
import org.talon540.vision.TalonVisionSystem;
import org.talon540.vision.VisionCameraMountConfig;
import org.talon540.vision.VisionFlags.CAMMode;
import org.talon540.vision.VisionFlags.LEDStates;

/**
 * An object used to get data and manipulate the state of a limelight camera
 */
public class LimelightVision implements TalonVisionSystem {

    private final VisionCameraMountConfig cameraPlacement;
    private final NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    /**
     * Construct a limelight object
     *
     * @param cameraPlacement camera placement relative to the robot
     * @param camMode camera mode to use
     * @param pipeline pipeline to set processing for
     */
    public LimelightVision(VisionCameraMountConfig cameraPlacement, CAMMode camMode, int pipeline) {
        this.cameraPlacement = cameraPlacement;

        setPipelineIndex(pipeline);
        setCamMode(camMode);
    }

    /**
     * Create a limelight object with the LEDs and Pipeline set to default
     *
     * @param cameraPlacement camera placement relative to the robot
     */
    public LimelightVision(VisionCameraMountConfig cameraPlacement) {
        this(
                cameraPlacement,
                CAMMode.PROCESSING,
                0
        );
    }

    @Override
    public LEDStates getLEDMode() {
        switch ((int) limelightTable.getEntry("ledMode").getDouble(0)) {
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
    public void setLEDMode(LEDStates state) {
        NetworkTableEntry ledEntry = limelightTable.getEntry("ledMode");
        switch (state) {
            case OFF:
                ledEntry.setNumber(1);
                break;

            case BLINK:
                ledEntry.setNumber(2);
                break;

            case ON:
                ledEntry.setNumber(3);
                break;

            case DEFAULT:
            default:
                ledEntry.setNumber(0);

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
        switch ((int) limelightTable.getEntry("camMode").getDouble(-1)) {
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
    public void setCamMode(CAMMode targetMode) {
        limelightTable.getEntry("camMode").setNumber(targetMode.val);
    }

    @Override
    public boolean targetViewed() {
        return limelightTable.getEntry("tv").getDouble(0) != 0;
    }

    @Override
    public TalonVisionState getVisionState() {
        if (!targetViewed())
            return null;

        return new TalonVisionState(
                limelightTable.getEntry("tx").getDouble(0),
                limelightTable.getEntry("ty").getDouble(0),
                limelightTable.getEntry("ts").getDouble(0),
                limelightTable.getEntry("ta").getDouble(0),
                null,
                limelightTable.getEntry("tl").getDouble(0)
        );
    }

    //    Utils
    @Override
    public Double getDistanceFromTarget(double targetHeight) {
        if (!targetViewed())
            return null;
        double deltaAngle = Math.toRadians(this.cameraPlacement.getMountAngleDegrees() + this.getVisionState().getPitch());
        return (targetHeight - this.cameraPlacement.getMountHeightMeters()) / Math.sin(deltaAngle);
    }

    @Override
    public Double getDistanceFromTargetBase(double targetHeight) {
        if (!targetViewed())
            return null;
        double deltaAngle = Math.toRadians(this.cameraPlacement.getMountAngleDegrees() + this.getVisionState().getPitch());
        return (targetHeight - this.cameraPlacement.getMountHeightMeters()) / Math.tan(deltaAngle);
    }

    @Override
    public Double getDistanceFromTargetBaseFromRobotCenter(double targetHeight) {
        // Use Law of cosines to find distance from center of the robot. See

        Vector2d cameraRelativePosition = cameraPlacement.getRobotRelativePosition();
        if (!targetViewed() || cameraRelativePosition == null)
            return null;

        double deltaX = cameraRelativePosition.getX();
        double deltaY = cameraRelativePosition.getY();

        double targetCameraOffset = Math.toRadians(getVisionState().getYaw());
        double distanceFromTarget = getDistanceFromTargetBase(targetHeight);

        double theta;

        if (deltaX > 5E-3) {
            if (deltaY > 5E-3) {
                // first quadrant
                theta = Math.PI - Math.atan(Math.abs(deltaX) / Math.abs(deltaY)) + targetCameraOffset;
            } else if (deltaY < -5E-3) {
                // fourth quadrant
                theta = (Math.PI / 2) - Math.atan(Math.abs(deltaY) / Math.abs(deltaX)) + targetCameraOffset;
            } else {
                // Vertically centered but horizontal offset
                theta = (Math.PI / 2) - targetCameraOffset;

            }
        } else if (deltaX < -5E-3) {
            if (deltaY > 5E-3) {
                // second quadrant
                theta = Math.PI - Math.atan(Math.abs(deltaX) / Math.abs(deltaY)) - targetCameraOffset;
            } else if (deltaY < -5E-3) {
                // third quadrant
                theta = (Math.PI / 2) - Math.atan(Math.abs(deltaY) / Math.abs(deltaX)) - targetCameraOffset;
            } else {
                // Vertically centered but horizontal offset
                theta = (Math.PI / 2) + targetCameraOffset;
            }
        } else {
            if (Math.abs(deltaY) < 5E-3) {
                // horizontally and vertical centered
                return distanceFromTarget;
            } else {
                // horizontally centered but vertical offset
                return distanceFromTarget - deltaY;
            }
        }

        double includedSideLength = Math.hypot(
                deltaX,
                deltaY
        );

        return Math.sqrt(Math.pow(
                distanceFromTarget,
                2
        ) + Math.pow(
                includedSideLength,
                2
        ) - (2 * distanceFromTarget * includedSideLength * Math.cos(theta)));
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addBooleanProperty(
                "viewed",
                this::targetViewed,
                null
        );
        builder.addDoubleProperty(
                "yaw",
                () -> targetViewed() ? getVisionState().getYaw() : 0,
                null
        );
        builder.addDoubleProperty(
                "pitch",
                () -> targetViewed() ? getVisionState().getPitch() : 0,
                null
        );
        builder.addDoubleProperty(
                "skew",
                () -> targetViewed() ? getVisionState().getSkew() : 0,
                null
        );
        builder.addDoubleProperty(
                "area",
                () -> targetViewed() ? getVisionState().getArea() : 0,
                null
        );
        builder.addDoubleProperty(
                "latency",
                () -> targetViewed() ? getVisionState().getPipelineLatency() : 0,
                null
        );
        builder.addDoubleProperty(
                "timestamp",
                () -> targetViewed() ? getVisionState().getStateTimestamp() : 0,
                null
        );

        builder.addDoubleProperty(
                "pipeline",
                this::getPipelineIndex,
                (index) -> setPipelineIndex(MathUtil.clamp(
                        (int) index,
                        0,
                        9
                ))
        );
        builder.addStringProperty(
                "LEDMode",
                () -> getLEDMode().toString(),
                this::setLEDMode
        );
    }

}
