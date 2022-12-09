package org.talon540.sensors.vision.PhotonVision;

import org.jetbrains.annotations.NotNull;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.talon540.sensors.vision.VisionState;

public class PhotonVisionState extends VisionState {
    private final int fiducialId;
    // private final double poseAmbiguity;

    /**
     * Create a vision state from data captured from a photon camera
     *
     * @param yaw (horizontal offset from target) of target
     * @param pitch (vertical offset from target) pitch of target
     * @param skew skew of target
     * @param area area of target
     * @param pipelineLatency latency of the pipeline (time taken to run calculations)
     * @param fiducialId fiduciary id of the target. If none, pass -1
     */
    public PhotonVisionState(
            double yaw, double pitch, double skew, double area, double pipelineLatency,
            // double error,
            int fiducialId
    ) {
        super(
                yaw,
                pitch,
                skew,
                area,
                pipelineLatency
        );

        this.fiducialId = fiducialId;
        // this.poseAmbiguity = error;
    }

    // /**
    //  * Get target error or the ambiguity of the primary target. Values above a certain threshold often mean this vision
    //  * state is unreliable or inaccurate. Will return {@code null} if the vision system doesn't support it
    //  *
    //  * @return target error
    //  */
    // public double getTargetAmbiguityPercent() {
    //     return targetAmbiguity;
    // }
    //
    // /**
    //  * Return the ambiguity of the target of the current state
    //  *
    //  * @return target ambiguity
    //  */
    // public TargetAmbiguity getTargetAmbiguity() {
    //     return targetAmbiguity == -1 ? TargetAmbiguity.INVALID : targetAmbiguity <= 0.2 ? TargetAmbiguity.SAFE : TargetAmbiguity.UNSAFE;
    // }

    /**
     * Return the fiduciary id of the target if it has one. Returns -1 if there is no id
     *
     * @return fiduciary di
     */
    public int getFiducialId() {
        return fiducialId;
    }

    /**
     * Return if the target has a fiduciary id (such as an AprilTag) or not (Retro reflective tape)
     *
     * @return if there is a valid fiduciary id
     */
    public boolean hasFiducialId() {
        return getFiducialId() != -1;
    }

    /**
     * Create a vision state from the latest data steam from a PhotonCamera. Return {@code null} if no targets
     *
     * @param stream photon camera results
     * @return TalonVisionState
     */
    public static PhotonVisionState fromPhotonStream(@NotNull PhotonPipelineResult stream) {
        return !stream.hasTargets() ? null : fromPhotonTarget(
                stream.getBestTarget(),
                stream.getLatencyMillis()
        );
    }

    /**
     * Create a vision state from a PhotonCamera Target
     *
     * @param target target from PhotonCamera
     * @param pipelineLatency latency of the pipeline
     * @return Talon Vision State from Photon Target
     */
    public static PhotonVisionState fromPhotonTarget(PhotonTrackedTarget target, double pipelineLatency) {
        if (target == null)
            return null;

        return new PhotonVisionState(
                target.getYaw(),
                target.getPitch(),
                target.getSkew(),
                target.getArea(),
                pipelineLatency,
                target.getFiducialId()
        );
    }
}
