package org.talon540.sensors.vision;

import edu.wpi.first.wpilibj.Timer;

public class VisionState {
    private final double yaw, pitch, skew, area, pipelineLatency, stateTimestamp;

    /**
     * Create a TalonVisionState from data
     *
     * @param yaw (horizontal offset from target) of target
     * @param pitch (vertical offset from target) pitch of target
     * @param skew skew of target
     * @param area area of target
     * @param pipelineLatency latency of the pipeline (time taken to run calculations)
     */
    public VisionState(
            double yaw, double pitch, double skew, double area, double pipelineLatency
    ) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.skew = skew;
        this.area = area;
        this.pipelineLatency = pipelineLatency / 1000;
        this.stateTimestamp = Timer.getFPGATimestamp() - this.pipelineLatency + 0.011;
    }

    /**
     * Get latency from the pipeline (the time required to do calculations
     *
     * @return pipeline calculation time/latency
     */
    public double getPipelineLatency() {
        return pipelineLatency;
    }

    /**
     * Get the timestamp of the time of calculation of the current state
     *
     * @return VisionState timestamp
     */
    public double getStateTimestamp() {
        return stateTimestamp;
    }

    /**
     * Get target yaw in degrees. Equivalent to tx
     *
     * @return target Yaw
     */
    public double getYaw() {
        return yaw;
    }

    /**
     * Get target pitch in degrees. Equivalent to ty
     *
     * @return target pitch
     */
    public double getPitch() {
        return pitch;
    }

    /**
     * Get target skew
     *
     * @return target skew
     */
    public double getSkew() {
        return skew;
    }

    /**
     * Get target area [0, 100]
     *
     * @return target area
     */
    public double getArea() {
        return area;
    }
}
