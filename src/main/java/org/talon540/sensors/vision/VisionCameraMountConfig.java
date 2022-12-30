package org.talon540.sensors.vision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents the position of a vision camera relative to the origin of the robot in the <a
 * href="https://docs.wpilib.org/en/stable/docs/software/advanced-controls/geometry/coordinate-systems.html#robot-coordinate-system">Robot
 * Coordinate System</a>.
 */
public class VisionCameraMountConfig {
  private final Pose3d robotOriginPosition;
  private final Supplier<Pose3d> cameraPosition;

  /**
   * Construct a VisionCameraMountConfig from a {@link Pose3d} supplier. This can be useful if the
   * camera is mounted on a non-static surface such as a turret or telescoping pole. The supplier
   * <b>cannot</b> return null or else an error will be thrown.
   *
   * @param robotOriginPosition {@link Pose3d} representing the position of the robot origin in the
   *     RCS.
   * @param cameraPosition {@link Pose3d} supplier that returns the position of the camera in the
   *     RCS.
   */
  public VisionCameraMountConfig(Pose3d robotOriginPosition, Supplier<Pose3d> cameraPosition) {
    this.robotOriginPosition = Objects.requireNonNull(robotOriginPosition);
    this.cameraPosition = Objects.requireNonNull(cameraPosition);

    if (!robotOriginPosition.getRotation().equals(new Rotation3d())) {
      throw new IllegalArgumentException(
          "Robot origin cannot have a non-zero roll, pitch, or yaw.");
    }
  }

  /**
   * Construct a VisionCameraMountConfig from a {@link Pose3d} supplier. This can be useful if the
   * camera is mounted on a non-static surface such as a turret or telescoping pole. The supplier
   * <b>cannot</b> return null or else an error will be thrown. Assumes that the robot origin is the
   * point on the floor horizontally and vertically centered to the robot.
   *
   * @param cameraPosition {@link Pose3d} representing the position of the camera in the RCS.
   */
  public VisionCameraMountConfig(Supplier<Pose3d> cameraPosition) {
    this(new Pose3d(), cameraPosition);
  }

  /**
   * Construct a VisionCameraMountConfig for a statically mounted camera from the position of the
   * camera as a {@link Pose3d}.
   *
   * @param robotOriginPosition {@link Pose3d} representing the position of the robot origin in the
   *     RCS.
   * @param cameraPosition {@link Pose3d} representing the position of the camera in the RCS.
   */
  public VisionCameraMountConfig(Pose3d robotOriginPosition, Pose3d cameraPosition) {
    this(robotOriginPosition, () -> cameraPosition);
  }

  /**
   * Construct a VisionCameraMountConfig for a statically mounted camera from the position of the
   * camera as a {@link Pose3d}. Assumes that the robot origin is the point on the floor
   * horizontally and vertically centered to the robot.
   *
   * @param cameraPosition {@link Pose3d} of the position of the camera in the RCS.
   */
  public VisionCameraMountConfig(Pose3d cameraPosition) {
    this(new Pose3d(), cameraPosition);
  }

  /**
   * Return the specified robot origin in the RCS.
   *
   * @return robot origin in the RCS.
   */
  public Pose3d getRobotOriginPosition() {
    return this.robotOriginPosition;
  }

  /**
   * Return the current position of the camera in the RCS.
   *
   * @return camera position in the RCS.
   */
  public Pose3d getCameraPosition() {
    return Objects.requireNonNull(cameraPosition.get(), "The provided camera position was null");
  }

  /**
   * Return the current transform between the origin of the robot to the camera.
   *
   * @return current {@link Transform3d}.
   */
  public Transform3d getRobotToCamera() {
    return new Transform3d(robotOriginPosition, getCameraPosition());
  }

  /**
   * Return the pitch of the camera relative to the horizontal plane the camera sits on assuming the
   * plane bisects the camera's sensor. This angle is signed.
   *
   * @return camera pitch in radians.
   * @see <a
   *     href="https://docs.limelightvision.io/en/latest/_images/DistanceEstimation.jpg"><i>a1</i>
   *     in the figure</a>
   */
  public double getCameraPitch() {
    return getCameraPosition().getRotation().getY();
  }

  /**
   * Return the height of the camera relative to the origin of the robot. Assuming the origin has a
   * height/Z value of 0, this would be the height of the camera off the floor.
   *
   * @return height of the camera relative to the origin of the robot.
   * @see <a
   *     href="https://docs.limelightvision.io/en/latest/_images/DistanceEstimation.jpg"><i>h1</i>
   *     in the figure</a>
   */
  public double getCameraHeight() {
    return getCameraPosition().getTranslation().getZ();
  }
}
