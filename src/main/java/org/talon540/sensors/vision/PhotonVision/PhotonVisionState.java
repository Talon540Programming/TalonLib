package org.talon540.sensors.vision.PhotonVision;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.Timer;
import org.jetbrains.annotations.NotNull;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.targeting.TargetCorner;
import org.talon540.sensors.vision.VisionState;

import java.util.List;

public class PhotonVisionState extends PhotonTrackedTarget implements VisionState {
    private final double pipelineLatencyMS;
    private final double stateTimestamp;

    public PhotonVisionState(
            double yaw,
            double pitch,
            double area,
            double skew,
            int id,
            Transform3d pose,
            Transform3d altPose,
            double ambiguity,
            List<TargetCorner> corners,
            double latency
    ) {
        super(
                yaw,
                pitch,
                area,
                skew,
                id,
                pose,
                altPose,
                ambiguity,
                corners
        );
        this.pipelineLatencyMS = latency;
        this.stateTimestamp = Timer.getFPGATimestamp() - this.pipelineLatencyMS + 0.011;
    }

    @Override
    public double getPipelineLatency() {
        return pipelineLatencyMS;
    }

    @Override
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

    public static PhotonVisionState fromPhotonTrackedTarget(@NotNull PhotonTrackedTarget target, double delay) {
        return new PhotonVisionState(
                target.getYaw(),
                target.getPitch(),
                target.getArea(),
                target.getSkew(),
                target.getFiducialId(),
                target.getBestCameraToTarget(),
                target.getAlternateCameraToTarget(),
                target.getPoseAmbiguity(),
                target.getCorners(),
                delay
        );
    }

    public static PhotonVisionState fromPhotonPipelineResult(@NotNull PhotonPipelineResult pipelineResult) {
        return pipelineResult.hasTargets() ? fromPhotonTrackedTarget(
                pipelineResult.getBestTarget(),
                pipelineResult.getLatencyMillis()
        ) : null;
    }

}
