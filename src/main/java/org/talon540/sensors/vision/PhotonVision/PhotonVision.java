package org.talon540.sensors.vision.PhotonVision;

import edu.wpi.first.util.sendable.SendableBuilder;
import java.util.List;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.talon540.sensors.vision.VisionCAMMode;
import org.talon540.sensors.vision.VisionCameraMountConfig;
import org.talon540.sensors.vision.VisionLEDMode;
import org.talon540.sensors.vision.VisionSystem;

public class PhotonVision extends VisionSystem {
  private final PhotonCamera camera;

  /**
   * Construct a PhotonVision system.
   *
   * @param cameraName name of the photon camera as configured.
   * @param config Mount config of the camera.
   */
  public PhotonVision(String cameraName, VisionCameraMountConfig config) {
    super(config);
    this.camera = new PhotonCamera(cameraName);
  }

  @Override
  public VisionLEDMode getLEDMode() {
    return switch (camera.getLEDMode()) {
      case kOn -> VisionLEDMode.kOn;
      case kOff -> VisionLEDMode.kOff;
      case kBlink -> VisionLEDMode.kBlink;
      case kDefault -> VisionLEDMode.kDefault;
    };
  }

  @Override
  public void setLEDMode(VisionLEDMode state) {
    switch (state) {
      case kOn -> camera.setLED(org.photonvision.common.hardware.VisionLEDMode.kOn);
      case kOff -> camera.setLED(org.photonvision.common.hardware.VisionLEDMode.kOff);
      case kBlink -> camera.setLED(org.photonvision.common.hardware.VisionLEDMode.kBlink);
      case kDefault -> camera.setLED(org.photonvision.common.hardware.VisionLEDMode.kDefault);
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
  public VisionCAMMode getCamMode() {
    return camera.getDriverMode() ? VisionCAMMode.kDriver : VisionCAMMode.kProcessing;
  }

  @Override
  public void setCamMode(VisionCAMMode targetMode) {
    camera.setDriverMode(targetMode == VisionCAMMode.kDriver);
  }

  @Override
  public boolean targetViewed() {
    return camera.getLatestResult().hasTargets();
  }

  /**
   * Return the vision state of the best target from the pipeline results.
   *
   * @return PhotonVisionState of best result.
   */
  public PhotonVisionState getVisionState() {
    return PhotonVisionState.fromPhotonPipelineResult(camera.getLatestResult());
  }

  /**
   * Return the current results of the pipeline in terms of {@link PhotonVisionState} objects. List
   * will be empty if there are no valid targets.
   *
   * @return List of {@link PhotonVisionState} objects from pipeline results.
   */
  public List<PhotonVisionState> getVisionStates() {
    PhotonPipelineResult pipelineResult = camera.getLatestResult();
    double stateTimestamp = pipelineResult.getTimestampSeconds();

    return pipelineResult.targets.stream()
        .map(target -> PhotonVisionState.fromPhotonTrackedTarget(target, stateTimestamp))
        .toList();
  }

  /**
   * Return the photon camera used for this system.
   *
   * @return photon camera being used.
   */
  public PhotonCamera getCamera() {
    return camera;
  }

  @Override
  public void initSendable(SendableBuilder builder) {}
}
