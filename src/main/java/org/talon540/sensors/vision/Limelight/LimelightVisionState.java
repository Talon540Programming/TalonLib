package org.talon540.sensors.vision.Limelight;

import edu.wpi.first.wpilibj.Timer;

public class LimelightVisionState {
  private final double yaw, pitch, skew, area, pipelineLatency, stateTimestamp;

  /**
   * Create a LimelightVisionState from data.
   *
   * @param yaw target yaw.
   * @param pitch target pitch.
   * @param area target pitch.
   * @param skew target skew.
   * @param pipelineLatency pipeline latency.
   */
  public LimelightVisionState(
      double yaw, double pitch, double area, double skew, double pipelineLatency) {
    this.yaw = yaw;
    this.pitch = pitch;
    this.skew = skew;
    this.area = area;

    // Convert from milliseconds to seconds. Add 11 for network delay.
    this.pipelineLatency = (pipelineLatency + 11) / 1000.0;

    this.stateTimestamp = Timer.getFPGATimestamp() - this.pipelineLatency;
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
   * Get the pipeline latency (time to run calculations).
   *
   * @return pipeline calculation time/latency in seconds.
   */
  public double getPipelineLatency() {
    return pipelineLatency;
  }

  /**
   * Return the yaw or horizontal offset of the target relative to the crosshair of the Limelight.
   * This angle is signed relative to the crosshair.
   *
   * @return target yaw (tx)
   */
  public double getYaw() {
    return yaw;
  }

  /**
   * Return the pitch or vertical offset of the target relative to the crosshair of the Limelight.
   * This angle is signed relative to the crosshair.
   *
   * @return target pitch (ty)
   */
  public double getPitch() {
    return pitch;
  }

  /**
   * Return the skew or rotation of the target.
   *
   * @return target skew (ts)
   */
  public double getSkew() {
    return skew;
  }

  /**
   * Return the area of the target relative to the frame. Within range [0, 100].
   *
   * @return target area.
   */
  public double getArea() {
    return area;
  }
}
