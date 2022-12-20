package org.talon540.sensors.vision.PhotonVision;

import edu.wpi.first.util.sendable.SendableBuilder;
import org.jetbrains.annotations.NotNull;
import org.photonvision.PhotonCamera;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonPipelineResult;
import org.talon540.sensors.vision.VisionCameraMountConfig;
import org.talon540.sensors.vision.VisionFlags.CAMMode;
import org.talon540.sensors.vision.VisionFlags.LEDStates;
import org.talon540.sensors.vision.VisionSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class PhotonVision extends VisionSystem {
    private final PhotonCamera camera;
    private final List<Consumer<PhotonVisionState>> stateConsumers = new ArrayList<>();

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
        return switch (camera.getLEDMode()) {
            case kOn -> LEDStates.ON;
            case kOff -> LEDStates.OFF;
            case kBlink -> LEDStates.BLINK;
            case kDefault -> LEDStates.DEFAULT;
        };
    }

    @Override
    public void setLEDMode(LEDStates state) {
        switch (state) {
            case ON -> camera.setLED(VisionLEDMode.kOn);
            case OFF -> camera.setLED(VisionLEDMode.kOff);
            case BLINK -> camera.setLED(VisionLEDMode.kBlink);
            case DEFAULT -> camera.setLED(VisionLEDMode.kDefault);
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
        return PhotonVisionState.fromPhotonPipelineResult(camera.getLatestResult());
    }

    public List<PhotonVisionState> getVisionStates() {
        // @formatter:off
        PhotonPipelineResult result = camera.getLatestResult();

        if (!result.hasTargets()) return null;

        double latency = result.getLatencyMillis();

        return result.targets.stream().map(target -> PhotonVisionState.fromPhotonTrackedTarget(target, latency)).toList();

        // @formatter:on
    }

    public void poll() {
        if (!targetViewed())
            return;

        for (PhotonVisionState state : getVisionStates()) {
            for (Consumer<PhotonVisionState> stateConsumer : stateConsumers) {
                stateConsumer.accept(state);
            }
        }
    }

    /**
     * Accept consumer when a target or targets are viewed.
     *
     * @param stateConsumer consumer
     */
    public void whenViewed(Consumer<PhotonVisionState> stateConsumer) {
        stateConsumers.add(stateConsumer);
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
