package org.talon540.sensors.vision.PhotonVision;

import edu.wpi.first.util.sendable.SendableBuilder;
import org.jetbrains.annotations.NotNull;
import org.photonvision.PhotonCamera;
import org.photonvision.common.hardware.VisionLEDMode;
import org.talon540.sensors.vision.VisionCameraMountConfig;
import org.talon540.sensors.vision.VisionFlags.CAMMode;
import org.talon540.sensors.vision.VisionFlags.LEDStates;
import org.talon540.sensors.vision.VisionSystem;


public class PhotonVision extends VisionSystem {
    private final PhotonCamera camera;

    /**
     * Construct a photon vision system with custom values
     *
     * @param cameraName name of the camera sub-table
     * @param cameraPlacement camera placement relative to the robot
     * @param camMode camera mode to use
     * @param pipeline pipeline to set processing for
     */
    public PhotonVision(
            @NotNull String cameraName, @NotNull VisionCameraMountConfig cameraPlacement, CAMMode camMode, int pipeline
    ) {
        super(cameraPlacement);
        this.camera = new PhotonCamera(cameraName);

        setPipelineIndex(pipeline);
        setCamMode(camMode);

    }

    /**
     * Construct a photon vision system with default pipeline and using the camera as a vision processor
     *
     * @param cameraName name of the camera sub-table
     * @param cameraPlacement camera placement relative to the robot
     */
    public PhotonVision(
            @NotNull String cameraName, @NotNull VisionCameraMountConfig cameraPlacement
    ) {
        this(
                cameraName,
                cameraPlacement,
                CAMMode.PROCESSING,
                0
        );
    }

    @Override
    public LEDStates getLEDMode() {
        switch (camera.getLEDMode()) {
            case kOn:
                return LEDStates.ON;
            case kOff:
                return LEDStates.OFF;
            case kBlink:
                return LEDStates.BLINK;
            case kDefault:
            default:
                return LEDStates.DEFAULT;
        }
    }

    @Override
    public void setLEDMode(LEDStates state) {
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
    public int getPipelineIndex() {
        return camera.getPipelineIndex();
    }

    @Override
    public void setPipelineIndex(int index) {
        camera.setPipelineIndex(index);
    }

    @Override
    public CAMMode getCamMode() {
        return camera.getDriverMode() ? CAMMode.DRIVER : CAMMode.PROCESSING;
    }

    @Override
    public void setCamMode(CAMMode targetMode) {
        camera.setDriverMode(targetMode == CAMMode.DRIVER);
    }

    @Override
    public boolean targetViewed() {
        return camera.getLatestResult().hasTargets();
    }

    @Override
    public PhotonVisionState getVisionState() {
        return PhotonVisionState.fromPhotonStream(camera.getLatestResult());
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.addDoubleProperty(
                "id",
                () -> targetViewed() ? getVisionState().getFiducialId() : -2,
                null
        );
    }
}
