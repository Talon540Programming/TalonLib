package org.talon540.sensors.vision.PhotonVision;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.Timer;
import java.util.List;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.targeting.TargetCorner;

public class PhotonVisionState extends PhotonTrackedTarget {
  private final double stateTimestamp;

  private PhotonVisionState(
      double yaw,
      double pitch,
      double area,
      double skew,
      int id,
      Transform3d pose,
      Transform3d altPose,
      double ambiguity,
      List<TargetCorner> minAreaRectCorners,
      List<TargetCorner> detectedCorners,
      double resultTimestampSeconds) {
    super(
        yaw, pitch, area, skew, id, pose, altPose, ambiguity, minAreaRectCorners, detectedCorners);

    this.stateTimestamp = resultTimestampSeconds;
  }

  /**
   * Get the timestamp of the current state in seconds since the robot has started. Safe to compare
   * to {@link Timer#getFPGATimestamp()}
   *
   * @return state of the timestamp in terms of the FPGA clock.
   */
  public double getStateTimestamp() {
    return stateTimestamp;
  }

  /**
   * Check if the target has a fiducial id
   *
   * @return if the target has a fiducial id
   */
  public boolean hasFiducialId() {
    return getFiducialId() != -1;
  }

  /**
   * Check if the target is an AprilTag (has a Fiducial ID)
   *
   * @return if target is an AprilTag
   */
  public boolean isAprilTag() {
    return hasFiducialId();
  }

  public static PhotonVisionState fromPhotonTrackedTarget(
      PhotonTrackedTarget target, double stateTimestamp) {
    return new PhotonVisionState(
        target.getYaw(),
        target.getPitch(),
        target.getArea(),
        target.getSkew(),
        target.getFiducialId(),
        target.getBestCameraToTarget(),
        target.getAlternateCameraToTarget(),
        target.getPoseAmbiguity(),
        target.getMinAreaRectCorners(),
        target.getDetectedCorners(),
        stateTimestamp);
  }

  public static PhotonVisionState fromPhotonPipelineResult(PhotonPipelineResult pipelineResult) {
    return pipelineResult.hasTargets()
        ? fromPhotonTrackedTarget(
            pipelineResult.getBestTarget(), pipelineResult.getTimestampSeconds())
        : null;
  }
}
