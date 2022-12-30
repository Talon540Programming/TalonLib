package org.talon540.sensors.vision;

/**
 * Represents a simple result state from a vision system.
 */
public interface VisionState {
  /**
   * Get the timestamp of the time of calculation of the current state
   *
   * @return VisionState timestamp
   */
  double getStateTimestamp();

  /**
   * Get latency from the pipeline (the time required to do calculations
   *
   * @return pipeline calculation time/latency
   */
  double getPipelineLatency();

  /**
   * Get target yaw in degrees. Equivalent to tx
   *
   * @return target Yaw
   */
  double getYaw();

  /**
   * Get target pitch in degrees. Equivalent to ty
   *
   * @return target pitch
   */
  double getPitch();
}
