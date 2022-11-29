package org.talon540.vision;

import edu.wpi.first.wpilibj.Timer;
import org.jetbrains.annotations.NotNull;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class TalonVisionState {
    private final double yaw, pitch, pipelineLatency, stateTimestamp;
    private final Double skew, area, error;

    /**
     * Create a TalonVisionState from data
     *
     * @param yaw (horizontal offset from target) of target
     * @param pitch (vertical offset from target) pitch of target
     * @param skew skew of target, set {@code null} if vision system doesn't support it
     * @param area area of target, set {@code null} if vision system doesn't support it
     * @param error error of target, set {@code null} if vision system doesn't support it
     * @param pipelineLatency latency of the pipeline (time taken to run calculations)
     */
    public TalonVisionState(
            double yaw, double pitch, Double skew, Double area, Double error, double pipelineLatency
    ) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.skew = skew;
        this.area = area;
        this.error = error;
        this.pipelineLatency = pipelineLatency / 1000;
        this.stateTimestamp = Timer.getFPGATimestamp() - this.pipelineLatency + 0.011;
    }


    /**
     * Get latency from the pipeline (the time required to do calculations
     *
     * @return pipeline calculation time/latency
     */
    public double getPipelineLatency() {return pipelineLatency;}

    /**
     * Get the timestamp of the time of calculation of the current state
     *
     * @return VisionState timestamp
     */
    public double getStateTimestamp() {
        return stateTimestamp;
    }

    /**
     * Get target yaw.
     *
     * @return target Yaw
     */
    public double getYaw() {
        return yaw;
    }

    /**
     * Get target pitch.
     *
     * @return target pitch
     */
    public double getPitch() {
        return pitch;
    }

    /**
     * Get target skew. Will return {@code null} if the vision system doesn't support it
     *
     * @return target skew
     */
    public Double getSkew() {
        return skew;
    }

    /**
     * Get target area [0, 100]. Will return {@code null} if the vision system doesn't support it
     *
     * @return target area
     */
    public Double getArea() {
        return area;
    }

    /**
     * Get target error or the ambiguity of the primary target. Values above a certain threshold often mean this vision
     * state is unreliable or inaccurate. Will return {@code null} if the vision system doesn't support it
     *
     * @return target error
     */
    public Double getError() {
        return error;
    }

    /**
     * Return the ambiguity of the target of the current state
     *
     * @return target ambiguity
     */
    public VisionFlags.TargetAmbiguity getTargetAmbiguity() {
        return error == null ? VisionFlags.TargetAmbiguity.INVALID : error == -1 ? VisionFlags.TargetAmbiguity.INVALID : error <= 0.2 ? VisionFlags.TargetAmbiguity.SAFE : VisionFlags.TargetAmbiguity.UNSAFE;
    }

    /**
     * Create a vision state from the latest data steam from a PhotonCamera
     *
     * @param stream photon camera results
     * @return TalonVisionState
     */
    public static TalonVisionState fromPhotonStream(@NotNull PhotonPipelineResult stream) {
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
    public static TalonVisionState fromPhotonTarget(PhotonTrackedTarget target, double pipelineLatency) {
        if (target == null)
            return null;

        return new TalonVisionState(
                target.getYaw(),
                target.getPitch(),
                target.getSkew(),
                target.getArea(),
                target.getPoseAmbiguity(),
                pipelineLatency
        );
    }

}
