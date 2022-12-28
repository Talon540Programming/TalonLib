package org.talon540.sensors.vision;

public interface VisionState {
  /**
   * Get latency from the pipeline (the time required to do calculations
   *
   * @return pipeline calculation time/latency
   */
  double getPipelineLatency();

  /**
   * Get the timestamp of the time of calculation of the current state
   *
   * @return VisionState timestamp
   */
  double getStateTimestamp();

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

  /**
   * Get target skew
   *
   * @return target skew
   */
  double getSkew();

  /**
   * Get target area [0, 100]
   *
   * @return target area
   */
  double getArea();
}
