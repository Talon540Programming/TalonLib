package org.talon540.vision.PhotonVision;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.SendableBuilder;
import org.photonvision.PhotonCamera;
import org.photonvision.common.hardware.VisionLEDMode;
import org.talon540.vision.TalonVisionState;
import org.talon540.vision.TalonVisionSystem;
import org.talon540.vision.VisionFlags.CAMMode;
import org.talon540.vision.VisionFlags.LEDStates;


public class PhotonVision implements TalonVisionSystem {
    private final PhotonCamera camera;
    private final double mountAngleDeg, mountHeight;

    /**
     * Construct a photon vision system with custom values
     *
     * @param cameraName name of the camera subtable
     * @param mountAngle mount angle of the camera in degrees
     * @param mountHeight mount height of the center of the camera's lens from the floor in meters
     * @param camMode camera mode to use
     * @param pipeline pipeline to set processing for
     */
    public PhotonVision(String cameraName, double mountHeight, double mountAngle, CAMMode camMode, int pipeline) {
        this.camera = new PhotonCamera(cameraName);
        this.mountHeight = mountHeight;
        this.mountAngleDeg = mountAngle;

        setPipelineIndex(pipeline);
        setCamMode(camMode);
    }

    /**
     * Construct a photon vision system with default pipeline and using the camera as a vision processesor
     *
     * @param cameraName name of the camera subtable
     * @param mountAngle mount angle of the camera in degrees
     * @param mountHeight mount height of the center of the camera's lens from the floor in meters
     */
    public PhotonVision(String cameraName, double mountHeight, double mountAngle) {
        this(cameraName, mountHeight, mountAngle, CAMMode.PROCESSING, 0);
    }

    @Override
    public TalonVisionState getVisionState() {
        return TalonVisionState.fromPhotonStream(camera.getLatestResult());
    }

    @Override
    public void setCamMode(CAMMode targetMode) {
        camera.setDriverMode(targetMode == CAMMode.DRIVER);
    }

    @Override
    public CAMMode getCamMode() {
        return camera.getDriverMode() ? CAMMode.DRIVER : CAMMode.PROCESSING;
    }

    @Override
    public void setPipelineIndex(int index) {
        camera.setPipelineIndex(index);
    }

    @Override
    public int getPipelineIndex() {
        return camera.getPipelineIndex();
    }

    @Override
    public void setLEDState(LEDStates state) {
        switch (state) {
            case ON:
                camera.setLED(VisionLEDMode.kOn);
                break;
            case OFF:
                camera.setLED(VisionLEDMode.kOff);
                break;
            case BLINK:
                camera.setLED(VisionLEDMode.kBlink);
                break;
            case DEFAULT:
                camera.setLED(VisionLEDMode.kDefault);
                break;
        }
    }

    @Override
    public LEDStates getLEDState() {
        switch (camera.getLEDMode()) {
            case kOn:
                return LEDStates.ON;
            case kOff:
                return LEDStates.OFF;
            case kBlink:
                return LEDStates.BLINK;
            default:
            case kDefault:
                return LEDStates.DEFAULT;
        }
    }

    @Override
    public Double getDistanceFromTarget(double targetHeight) {
        if (!targetViewed())
            return null;
        double deltaAngle = Math.toRadians(this.mountAngleDeg + this.getVisionState().getOffsetY());
        return (targetHeight - this.mountHeight) / Math.sin(deltaAngle);
    }

    @Override
    public Double getDistanceFromTargetBase(double targetHeight) {
        if (!targetViewed())
            return null;
        double deltaAngle = Math.toRadians(this.mountAngleDeg + this.getVisionState().getOffsetY());
        return (targetHeight - this.mountHeight) / Math.tan(deltaAngle);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addBooleanProperty("tViewed", this::targetViewed, null);
        builder.addDoubleProperty("tOffsetX", () -> getVisionState().getOffsetX(), null);
        builder.addDoubleProperty("tOffsetY", () -> getVisionState().getOffsetY(), null);
        builder.addDoubleProperty("tYaw", () -> getVisionState().getYaw(), null);
        builder.addDoubleProperty("tPitch", () -> getVisionState().getPitch(), null);
        builder.addDoubleProperty("tSkew", () -> getVisionState().getSkew(), null);
        builder.addDoubleProperty("tArea", () -> getVisionState().getArea(), null);
        builder.addDoubleProperty("tError", () -> getVisionState().getError(), null);
        builder.addDoubleProperty("pLatency", () -> getVisionState().getPipelineLatency(), null);
        builder.addDoubleProperty("tTimestamp", () -> getVisionState().getStateTimestamp(), null);

        builder.addDoubleProperty("pipeline",
                this::getPipelineIndex,
                (index) -> setPipelineIndex(MathUtil.clamp((int) index, 0, 9))
        );
        builder.addStringProperty("LEDMode", () -> getLEDState().toString(), this::setLEDState);
    }
}
