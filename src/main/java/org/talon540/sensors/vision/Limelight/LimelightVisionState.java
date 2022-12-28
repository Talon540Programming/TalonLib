package org.talon540.sensors.vision.Limelight;

import edu.wpi.first.wpilibj.Timer;
import org.talon540.sensors.vision.VisionState;

public class LimelightVisionState implements VisionState {
  private final double yaw, pitch, skew, area, pipelineLatency, stateTimestamp;

  /**
   * Create a vision state from data
   *
   * @param yaw (horizontal offset from target) of target
   * @param pitch (vertical offset from target) pitch of target
   * @param skew skew of target
   * @param area area of target
   * @param pipelineLatency latency of the pipeline (time taken to run calculations)
   */
  public LimelightVisionState(
      double yaw, double pitch, double skew, double area, double pipelineLatency) {
    this.yaw = yaw;
    this.pitch = pitch;
    this.skew = skew;
    this.area = area;
    this.pipelineLatency = pipelineLatency / 1000;
    this.stateTimestamp = Timer.getFPGATimestamp() - this.pipelineLatency + 0.011;
  }

  @Override
  public double getPipelineLatency() {
    return pipelineLatency;
  }

  @Override
  public double getStateTimestamp() {
    return stateTimestamp;
  }

  @Override
  public double getYaw() {
    return yaw;
  }

  @Override
  public double getPitch() {
    return pitch;
  }

  @Override
  public double getSkew() {
    return skew;
  }

  @Override
  public double getArea() {
    return area;
  }
}
