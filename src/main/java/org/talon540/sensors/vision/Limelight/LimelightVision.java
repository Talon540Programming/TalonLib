package org.talon540.sensors.vision.Limelight;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.SendableBuilder;
import java.util.Optional;
import org.talon540.sensors.vision.VisionCAMMode;
import org.talon540.sensors.vision.VisionCameraMountConfig;
import org.talon540.sensors.vision.VisionLEDMode;
import org.talon540.sensors.vision.VisionSystem;

/** An object used to get data and manipulate the state of a limelight camera */
public class LimelightVision extends VisionSystem {
  // Target Data
  private final NetworkTableEntry targetViewedEntry;
  private final NetworkTableEntry targetYawEntry;
  private final NetworkTableEntry targetPitchEntry;
  private final NetworkTableEntry targetAreaEntry;
  private final NetworkTableEntry targetSkewEntry;
  private final NetworkTableEntry pipelineLatencyEntry;

  // Camera Controls
  private final NetworkTableEntry ledModeEntry;
  private final NetworkTableEntry camModeEntry;
  private final NetworkTableEntry pipelineEntry;
  private final NetworkTableEntry snapshotEntry;

  /**
   * Construct a Limelight camera using a specific NT instance.
   *
   * @param instance NT instance to use.
   * @param config Mount config of the camera.
   */
  public LimelightVision(NetworkTableInstance instance, VisionCameraMountConfig config) {
    super(config);

    NetworkTable limelightTable = instance.getTable("limelight");
    this.targetViewedEntry = limelightTable.getEntry("tv");
    this.targetYawEntry = limelightTable.getEntry("tx");
    this.targetPitchEntry = limelightTable.getEntry("ty");
    this.targetAreaEntry = limelightTable.getEntry("ta");
    this.targetSkewEntry = limelightTable.getEntry("ts");
    this.pipelineLatencyEntry = limelightTable.getEntry("tl");

    this.ledModeEntry = limelightTable.getEntry("ledMode");
    this.camModeEntry = limelightTable.getEntry("camMode");
    this.pipelineEntry = limelightTable.getEntry("pipeline");
    this.snapshotEntry = limelightTable.getEntry("snapshot");
  }

  /**
   * Construct a limelight camera using the default NetworkTablesInstance.
   *
   * @param config Mount config of the camera.
   */
  public LimelightVision(VisionCameraMountConfig config) {
    this(NetworkTableInstance.getDefault(), config);
  }

  @Override
  public VisionLEDMode getLEDMode() {
    return switch (ledModeEntry.getNumber(-1).intValue()) {
      case 1 -> VisionLEDMode.kOff;
      case 2 -> VisionLEDMode.kBlink;
      case 3 -> VisionLEDMode.kOn;
      default -> VisionLEDMode.kDefault;
    };
  }

  @Override
  public void setLEDMode(VisionLEDMode state) {
    switch (state) {
      case kOff -> ledModeEntry.setNumber(1);
      case kBlink -> ledModeEntry.setNumber(2);
      case kOn -> ledModeEntry.setNumber(3);
      case kDefault -> ledModeEntry.setNumber(0);
    }
  }

  @Override
  public int getPipelineIndex() {
    return pipelineEntry.getNumber(-1).intValue();
  }

  @Override
  public void setPipelineIndex(int index) {
    pipelineEntry.setNumber(index);
  }

  @Override
  public VisionCAMMode getCamMode() {
    return switch (camModeEntry.getNumber(-1).intValue()) {
      case 0 -> VisionCAMMode.kProcessing;
      case 1 -> VisionCAMMode.kDriver;
      default -> VisionCAMMode.kInvalid;
    };
  }

  @Override
  public void setCamMode(VisionCAMMode targetMode) {
    switch (targetMode) {
      case kProcessing -> camModeEntry.setNumber(0);
      case kDriver -> camModeEntry.setNumber(1);
    }
  }

  @Override
  public boolean targetViewed() {
    return targetViewedEntry.getDouble(0) == 1;
  }

  /** Take a snapshot (photo) from the limelight. */
  public void takeSnapshot() {
    snapshotEntry.setNumber(1);
  }

  /**
   * Get the current vision state of the limelight of any targets seen by the camera. Will return
   * empty if there are no targets.
   *
   * @return current vision state.
   */
  public Optional<LimelightVisionState> getVisionState() {
    if (!targetViewed()) return Optional.empty();

    return Optional.of(
        new LimelightVisionState(
            targetYawEntry.getDouble(0),
            targetPitchEntry.getDouble(0),
            targetAreaEntry.getDouble(0),
            targetSkewEntry.getDouble(0),
            pipelineLatencyEntry.getDouble(0)));
  }

  @Override
  public void initSendable(SendableBuilder builder) {}
}
