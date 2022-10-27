package org.talon540.vision;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.talon540.math.Vector2d;


public class TalonVisionState {
    private final double pipelineLatency, stateTimestamp;
    private final Double yaw, pitch, skew, area, error;
    private final Vector2d offsets;

    /**
     * Create a TalonVisionState from data
     *
     * @param offsetX horizontal offset from target
     * @param offsetY vertical offset from target
     * @param yaw yaw of target, set {@code null} if vision system doesn't support it
     * @param pitch pitch of target, set {@code null} if vision system doesn't support it
     * @param skew skew of target, set {@code null} if vision system doesn't support it
     * @param area area of target, set {@code null} if vision system doesn't support it
     * @param error error of target, set {@code null} if vision system doesn't support it
     * @param pipelineLatency latency of the pipeline (time taken to run calculations)
     */
    public TalonVisionState(
            double offsetX,
            double offsetY,
            Double yaw,
            Double pitch,
            Double skew,
            Double area,
            Double error,
            double pipelineLatency
    ) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.skew = skew;
        this.area = area;
        this.error = error;
        this.pipelineLatency = pipelineLatency;
        this.stateTimestamp = Timer.getFPGATimestamp() - (this.pipelineLatency / 1000.0) + 0.011;

        this.offsets = new Vector2d(offsetX, offsetY);
    }

    /**
     * Create a vision state from the latest data steam from a PhotonCamera
     *
     * @param stream photon camera results
     * @return TalonVisionState
     */
    public static TalonVisionState fromPhotonStream(PhotonPipelineResult stream) {
        if (!stream.hasTargets())
            return null;
        return fromPhotonTarget(stream.getBestTarget(), stream.getLatencyMillis());
    }

    /**
     * Create a vision state from a PhotonCamera Target
     *
     * @param target target from PhotonCamera
     * @param pipelineLatency latency of the pipeline
     * @return Talon Vision State from Photon Target
     */
    public static TalonVisionState fromPhotonTarget(PhotonTrackedTarget target, double pipelineLatency) {
        return new TalonVisionState(
                target.getCameraToTarget().getX(),
                target.getCameraToTarget().getY(),
                target.getYaw(),
                target.getPitch(),
                target.getSkew(),
                target.getArea(),
                target.getPoseAmbiguity(),
                pipelineLatency
        );
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
     * Get target yaw. Will return {@code null} if the vision system doesn't support it
     *
     * @return target Yaw
     */
    public Double getYaw() {
        return yaw;
    }

    /**
     * Get yaw as a form of a {@link Rotation2d} object
     *
     * @return yaw as {@link Rotation2d}. Will return {@code null} if the vision system doesn't support it
     */
    public Rotation2d getYawRotation2d() {
        return yaw == null ? null : Rotation2d.fromDegrees(-yaw);
    }

    /**
     * Get target pitch. Will return {@code null} if the vision system doesn't support it
     *
     * @return target pitch
     */
    public Double getPitch() {
        return pitch;
    }

    /**
     * Get pitch as a form of a {@link Rotation2d} object
     *
     * @return pitch as {@link Rotation2d}. Will return {@code null} if the vision system doesn't support it
     */
    public Rotation2d getPitchRotation2d() {
        return pitch == null ? null : Rotation2d.fromDegrees(pitch);
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
     * Get skew as a form of a {@link Rotation2d} object
     *
     * @return skew as {@link Rotation2d}. Will return {@code null} if the vision system doesn't support it
     */
    public Rotation2d getSkewRotation2d() {
        return skew == null ? null : Rotation2d.fromDegrees(skew);
    }

    /**
     * Get target area. Will return {@code null} if the vision system doesn't support it
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
     * Get the offsets from the target from the vision system
     *
     * @return target Offsets
     */
    public Vector2d getOffsets() {
        return offsets;
    }

    /**
     * Target horizontal offset
     *
     * @return horizontal offset
     */
    public double getOffsetX() {
        return offsets.getX();
    }

    /**
     * Target vertical offset
     *
     * @return vertical offset
     */
    public double getOffsetY() {
        return offsets.getY();
    }

}
