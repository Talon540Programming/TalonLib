package org.talon540.sensors.vision;

import edu.wpi.first.util.sendable.Sendable;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public abstract class VisionSystem implements Sendable {
  protected final VisionCameraMountConfig mountConfig;

  protected VisionSystem(@NotNull VisionCameraMountConfig mountConfig) {
    this.mountConfig = Objects.requireNonNull(mountConfig);
  }

  /**
   * Get the current index of the running vision system pipeline.
   *
   * @return current pipeline index.
   */
  public abstract int getPipelineIndex();

  /** Set the index of the vision system's pipeline. */
  public abstract void setPipelineIndex(int index);

  /**
   * @return current mode of the vision system's LEDs.
   */
  public abstract VisionLEDMode getLEDMode();

  /**
   * Set the mode of the LEDs of the vision system.
   *
   * @param state LED mode to set.
   */
  public abstract void setLEDMode(VisionLEDMode state);

  /** Enables the vision system's LEDs. */
  public void enableLEDS() {
    setLEDMode(VisionLEDMode.kOn);
  }

  /** Disables the vision system's LEDs. */
  public void disableLEDS() {
    setLEDMode(VisionLEDMode.kOff);
  }

  /** Blinks the vision system's LEDs. */
  public void blinkLEDS() {
    setLEDMode(VisionLEDMode.kBlink);
  }

  /**
   * Get the current camera mode of the vision system.
   *
   * @return current camera mode.
   */
  public abstract VisionCAMMode getCamMode();

  /** Set the camera mode of the vision system. */
  public abstract void setCamMode(VisionCAMMode targetMode);

  /**
   * Whether the vision system has any valid targets in its view.
   *
   * @return view status of a valid target.
   */
  public abstract boolean targetViewed();
}
