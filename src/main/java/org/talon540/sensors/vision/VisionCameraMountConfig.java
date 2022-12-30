package org.talon540.sensors.vision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import java.util.Objects;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the position of the vision camera relative to the origin point of the robot. The
 * origin point of the robot is the point horizontally and vertically centered lying on the ground.
 * In the robot coordinate system, this would be {@code new Pose3d(0, 0, 0)} The origin point is
 * mostly used for position estimation calculations.
 */
public class VisionCameraMountConfig {
  private final Pose3d robotOrigin;
  private final Supplier<Pose3d> cameraPosition;

  /**
   * Construct a CameraMountConfig from a {@link Pose3d} supplier. This can be useful if the camera
   * is mounted on a non-static surface such as a turret or telescoping pole. The logic for the
   * Transform of the camera must be handled by this supplier. The supplier <b>cannot</b> return
   * null or else an error will be thrown.
   *
   * @param robotOrigin {@link Pose3d} representing the position of the robot origin in the RCS.
   * @param cameraPosition {@link Pose3d} supplier that returns the position of the camera in the
   *     RCS.
   */
  public VisionCameraMountConfig(
      @NotNull Pose3d robotOrigin, @NotNull Supplier<Pose3d> cameraPosition) {
    if (!robotOrigin.getRotation().equals(new Rotation3d())) {
      throw new IllegalArgumentException(
          "The robot origin cannot have different yaw, pitch, or skew. It must hold a Rotation3d with a standard values.");
    }

    this.robotOrigin = robotOrigin;
    this.cameraPosition = cameraPosition;
  }

  /**
   * Construct a CameraMountConfig from a {@link Pose3d} supplier. This can be useful if the camera
   * is mounted on a non-static surface such as a turret or telescoping pole. The logic for the
   * Transform of the camera must be handled by this supplier. The supplier <b>cannot</b> return
   * null or else an error will be thrown. Assumes that the robot origin is the point on the floor
   * horizontally and vertically centered to the robot.
   *
   * @param cameraPosition {@link Pose3d} representing the position of the camera in the RCS.
   */
  public VisionCameraMountConfig(@NotNull Supplier<Pose3d> cameraPosition) {
    this(new Pose3d(), cameraPosition);
  }

  /**
   * Construct a CameraMountConfig for a statically mounted camera from the position of the camera.
   *
   * @param robotOrigin {@link Pose3d} representing the position of the robot origin in the RCS.
   * @param cameraPosition {@link Pose3d} representing the position of the camera in the RCS.
   */
  public VisionCameraMountConfig(@NotNull Pose3d robotOrigin, @NotNull Pose3d cameraPosition) {
    this(robotOrigin, () -> cameraPosition);
  }

  /**
   * Construct a CameraMountConfig for a statically mounted camera from the position of the camera.
   * Assumes that the robot origin is the point on the floor horizontally and vertically centered to
   * the robot.
   *
   * @param cameraPosition {@link Pose3d} of the position of the camera in the RCS.
   */
  public VisionCameraMountConfig(@NotNull Pose3d cameraPosition) {
    this(new Pose3d(), cameraPosition);
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
   * Return the current transform between the origin (center) of the robot to the camera.
   *
   * @return current {@link Transform3d}.
   */
  public Transform3d getRobotToCamera() {
    return new Transform3d(robotOrigin, cameraPosition.get());
  }

  /**
   * Return the pitch of the camera relative to the horizontal plane the camera sits on assuming the
   * plane bisects the camera sensor.
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
   * Return the height of the camera relative to the origin of the robot. Assuming the origin is on
   * the floor, this would be the height of the camera off the floor.
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
