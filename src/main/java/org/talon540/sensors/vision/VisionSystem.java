package org.talon540.sensors.vision;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.OptionalDouble;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.talon540.math.Vector2d;
import org.talon540.sensors.vision.VisionFlags.CAMMode;
import org.talon540.sensors.vision.VisionFlags.LEDStates;

public abstract class VisionSystem implements Sendable {
  protected final VisionCameraMountConfig mountConfig;
  protected final Collection<Consumer<VisionState>> viewEvents = new LinkedHashSet<>();

  protected VisionSystem(@NotNull VisionCameraMountConfig mountConfig) {
    this.mountConfig = mountConfig;
  }

  /**
   * Get the current index of the running vision system pipeline
   *
   * @return current index
   */
  public abstract int getPipelineIndex();

  /** Set the index of the vision system's pipeline */
  public abstract void setPipelineIndex(int index);

  /**
   * @return current mode of the LEDs
   */
  public abstract LEDStates getLEDMode();

  /**
   * Set the mode of the LEDs of the vision system
   *
   * @param state target state of the camera
   */
  public abstract void setLEDMode(LEDStates state);

  /** Enables vision system LEDs */
  public void enableLEDS() {
    setLEDMode(LEDStates.ON);
  }

  /** Disable vision system LEDs */
  public void disableLEDS() {
    setLEDMode(LEDStates.OFF);
  }

  /** Blinks vision system LEDs */
  public void blinkLEDS() {
    setLEDMode(LEDStates.BLINK);
  }

  /**
   * Get the current camera mode of the vision system
   *
   * @return current camera mode
   */
  public abstract CAMMode getCamMode();

  /** Set the camera mode of the vision system */
  public abstract void setCamMode(CAMMode targetMode);

  /**
   * Whether the vision system target is currently viewed
   *
   * @return view status of target
   */
  public abstract boolean targetViewed();

  /** Get the current vision state data. Returns {@code null} if there are no targets */
  public abstract VisionState getVisionState();

  /**
   * Create a trigger that activates whenever a target is viewed. Useful if you need to schedule a
   * command whenever target data is needed.
   *
   * @return target view event trigger
   */
  public Trigger getTargetViewedEvent() {
    return new Trigger(this::targetViewed);
  }

  /**
   * Provide the vision state to the provided event consumer when a target is viewed.\
   *
   * @param event Event consumer that receives the state.
   */
  public void whenViewed(Consumer<VisionState> event) {
    viewEvents.add(event);
  }

  /**
   * Poll the vision camera and feeds event consumers provided with {@link
   * VisionSystem#whenViewed(Consumer)}. Put this in the robot periodic method or addPeriodic in
   * TimedRobot.
   */
  public void poll() {
    if (!targetViewed()) return;

    viewEvents.forEach(eventConsumer -> eventConsumer.accept(getVisionState()));
  }

  // Calculations

  /**
   * Get distance from a specified target (Hypotenuse). Follows <a
   * href="https://docs.limelightvision.io/en/latest/cs_estimating_distance.html">...</a>
   *
   * @param targetHeightMeters height of the retro reflector in meters. Already offsets for mount
   *     height
   * @return distance from the target in {@code meters}. Returns empty if target is not found
   */
  public OptionalDouble getDistanceFromTarget(double targetHeightMeters) {
    if (!targetViewed()) return OptionalDouble.empty();

    double deltaAngle =
        Math.toRadians(this.mountConfig.getMountAngleDegrees() + this.getVisionState().getPitch());
    return OptionalDouble.of(
        (targetHeightMeters - this.mountConfig.getMountHeightMeters()) / Math.sin(deltaAngle));
  }

  /**
   * Get distance from a specified target's base. Follows <a
   * href="https://docs.limelightvision.io/en/latest/cs_estimating_distance.html">...</a>
   *
   * @param targetHeightMeters height of the retro reflector in meters. Already offsets for mount
   *     height
   * @return distance from the base of the target in {@code meters}. Returns empty if target is not
   *     found
   */
  public OptionalDouble getDistanceFromTargetBase(double targetHeightMeters) {
    if (!targetViewed()) return OptionalDouble.empty();
    double deltaAngle =
        Math.toRadians(this.mountConfig.getMountAngleDegrees() + this.getVisionState().getPitch());
    return OptionalDouble.of(
        (targetHeightMeters - this.mountConfig.getMountHeightMeters()) / Math.tan(deltaAngle));
  }

  /**
   * Get the distance from the center of the robot to the base of a target
   *
   * @param targetHeightMeters height of the retro reflector in meters. Already offsets for mount
   *     height
   * @return distance from the base of the target in {@code meters} from the center of the robot.
   *     Returns empty if target is not found
   */
  public OptionalDouble getDistanceToTargetBaseFromRobotCenter(double targetHeightMeters) {
    if (!targetViewed()) return OptionalDouble.empty();

    Vector2d cameraPosition = mountConfig.getRobotRelativePosition();

    double deltaX = cameraPosition.getX();
    double deltaY = cameraPosition.getY();

    double distanceFromTargetMeters = getDistanceFromTargetBase(targetHeightMeters).getAsDouble();

    // Included angle between the robot's center and the target
    double theta = Math.signum(deltaX) * Math.toRadians(getVisionState().getYaw());

    // @formatter:off

    if (deltaX == 0) {
      return OptionalDouble.of(distanceFromTargetMeters + deltaY);
    } else if (deltaY == 0) {
      theta += (Math.PI / 2.0);
      return OptionalDouble.of(
          Math.sqrt(
              Math.pow(distanceFromTargetMeters, 2)
                  + Math.pow(deltaX, 2)
                  - (2 * distanceFromTargetMeters * Math.abs(deltaX) * Math.cos(theta))));
    }

    theta +=
        deltaY < 0
            ? (Math.PI / 2.0) - Math.atan(Math.abs(deltaY) / Math.abs(deltaX))
            : Math.PI - Math.atan(Math.abs(deltaX) / Math.abs(deltaY));

    double includedSideLength = Math.hypot(deltaX, deltaY);

    return OptionalDouble.of(
        Math.sqrt(
            Math.pow(distanceFromTargetMeters, 2)
                + Math.pow(includedSideLength, 2)
                - (2 * distanceFromTargetMeters * includedSideLength * Math.cos(theta))));
    // @formatter:on
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addBooleanProperty("viewed", this::targetViewed, null);
    builder.addDoubleProperty("yaw", () -> targetViewed() ? getVisionState().getYaw() : 0, null);
    builder.addDoubleProperty(
        "pitch", () -> targetViewed() ? getVisionState().getPitch() : 0, null);
    builder.addDoubleProperty("skew", () -> targetViewed() ? getVisionState().getSkew() : 0, null);
    builder.addDoubleProperty("area", () -> targetViewed() ? getVisionState().getArea() : 0, null);
    builder.addDoubleProperty(
        "latency", () -> targetViewed() ? getVisionState().getPipelineLatency() : 0, null);
    builder.addDoubleProperty(
        "timestamp", () -> targetViewed() ? getVisionState().getStateTimestamp() : 0, null);
    builder.addDoubleProperty("pipeline", this::getPipelineIndex, null);
    builder.addStringProperty("LEDMode", () -> getLEDMode().toString(), null);
  }
}
