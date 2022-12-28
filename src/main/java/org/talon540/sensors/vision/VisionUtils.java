package org.talon540.sensors.vision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import org.jetbrains.annotations.ApiStatus;
import org.talon540.math.Vector2d;

/**
 * Vision utilities used for computer vision based calculations. Uses stuff from both PhotonVision
 * and WPI.
 */
public final class VisionUtils {
  private VisionUtils() {
    throw new AssertionError("utility class");
  }

  /**
   * Algorithm from <a
   * href="https://docs.limelightvision.io/en/latest/cs_estimating_distance.html">...</a> Estimates
   * range to a target's base (horizontal distance) using the target's elevation.
   *
   * @param cameraHeightMeters The physical height of the camera off the floor in meters.
   * @param targetHeightMeters The physical height of the target off the floor in meters. This
   *     should be the height of whatever is being targeted (i.e. if the targeting region is set to
   *     top, this should be the height of the top of the target).
   * @param cameraPitchRadians The pitch of the camera from the horizontal plane in radians.
   *     Positive values up.
   * @param targetPitchRadians The pitch of the target in the camera's lens in radians. Positive
   *     values up.
   * @return The estimated distance to the target in meters.
   * @see org.photonvision.PhotonUtils
   */
  public static double calculateDistanceToTargetBaseMeters(
      double cameraHeightMeters,
      double targetHeightMeters,
      double cameraPitchRadians,
      double targetPitchRadians) {
    return (targetHeightMeters - cameraHeightMeters)
        / Math.tan(cameraPitchRadians + targetPitchRadians);
  }

  /**
   * Algorithm from <a
   * href="https://docs.limelightvision.io/en/latest/cs_estimating_distance.html">...</a> Estimates
   * range to a target (hypotenuse formed between the camera, base, and the target) using the
   * target's elevation.
   *
   * @param cameraHeightMeters The physical height of the camera off the floor in meters.
   * @param targetHeightMeters The physical height of the target off the floor in meters. This
   *     should be the height of whatever is being targeted (i.e. if the targeting region is set to
   *     top, this should be the height of the top of the target).
   * @param cameraPitchRadians The pitch of the camera from the horizontal plane in radians.
   *     Positive values up.
   * @param targetPitchRadians The pitch of the target in the camera's lens in radians. Positive
   *     values up.
   * @return The estimated distance to the target in meters.
   */
  public static double calculateDistanceToTargetHypotenuseMeters(
      double cameraHeightMeters,
      double targetHeightMeters,
      double cameraPitchRadians,
      double targetPitchRadians) {
    return (targetHeightMeters - cameraHeightMeters)
        / Math.sin(cameraPitchRadians + targetPitchRadians);
  }

  /**
   * Estimate the {@link Translation2d} of the target relative to the camera.
   *
   * @param targetDistanceMeters The distance to the target in meters.
   * @param yaw The observed yaw of the target.
   * @return The target's camera-relative translation.
   * @see org.photonvision.PhotonUtils
   */
  public static Translation2d estimateCameraToTargetTranslation(
      double targetDistanceMeters, Rotation2d yaw) {
    return new Translation2d(
        yaw.getCos() * targetDistanceMeters, yaw.getSin() * targetDistanceMeters);
  }

  /**
   * Get the distance from the center of the robot to the base of a target
   *
   * @param cameraHeightMeters The physical height of the camera off the floor in meters.
   * @param targetHeightMeters The physical height of the target off the floor in meters. This
   *     should be the height of whatever is being targeted (i.e. if the targeting region is set to
   *     top, this should be the height of the top of the target).
   * @param cameraPitchRadians The pitch of the camera from the horizontal plane in radians.
   *     Positive values up.
   * @param targetPitchRadians The pitch of the target in the camera's lens in radians. Positive
   *     values up.
   * @param targetYawRadians The yaw of the target in the camera's lens in radians. Positive values
   *     right.
   * @param robotToCamera {@link Vector2d } position of the camera relative to the center (0, 0) of
   *     the robot
   * @return distance from the center of the robot to the base of a target
   */
  @ApiStatus.Experimental
  public static double calculateDistanceToTargetBaseFromRobotCenterMeters(
      double cameraHeightMeters,
      double targetHeightMeters,
      double cameraPitchRadians,
      double targetPitchRadians,
      double targetYawRadians,
      Vector2d robotToCamera) {
    double deltaX = robotToCamera.getX();
    double deltaY = robotToCamera.getY();

    double distanceFromTargetMeters =
        calculateDistanceToTargetBaseMeters(
            cameraHeightMeters, targetHeightMeters, cameraPitchRadians, targetPitchRadians);

    // Included angle between the robot's center and the target
    double theta = Math.signum(deltaX) * targetYawRadians;

    if (deltaX == 0) {
      return distanceFromTargetMeters + deltaY;
    } else if (deltaY == 0) {
      theta += (Math.PI / 2.0);
      return Math.sqrt(
          Math.pow(distanceFromTargetMeters, 2)
              + Math.pow(deltaX, 2)
              - (2 * distanceFromTargetMeters * Math.abs(deltaX) * Math.cos(theta)));
    }

    theta +=
        deltaY < 0
            ? (Math.PI / 2.0) - Math.atan(Math.abs(deltaY) / Math.abs(deltaX))
            : Math.PI - Math.atan(Math.abs(deltaX) / Math.abs(deltaY));

    double includedSideLength = Math.hypot(deltaX, deltaY);

    return Math.sqrt(
        Math.pow(distanceFromTargetMeters, 2)
            + Math.pow(includedSideLength, 2)
            - (2 * distanceFromTargetMeters * includedSideLength * Math.cos(theta)));
  }

  /**
   * Returns the robot's pose in the field coordinate system given an object's field-relative pose,
   * the transformation from the camera's pose to the object's pose (obtained via computer vision),
   * and the transformation from the robot's pose to the camera's pose.
   *
   * <p>The object could be a target or a fiducial marker.
   *
   * @param objectInField An object's field-relative pose.
   * @param cameraToObject The transformation from the camera's pose to the object's pose. This
   *     comes from computer vision.
   * @param robotToCamera The transformation from the robot's pose to the camera's pose.
   * @return The robot's field-relative pose.
   * @see edu.wpi.first.math.ComputerVisionUtil
   */
  public static Pose3d objectToRobotPose(
      Pose3d objectInField, Transform3d cameraToObject, Transform3d robotToCamera) {
    Transform3d objectToCamera = cameraToObject.inverse();
    Transform3d cameraToRobot = robotToCamera.inverse();
    return objectInField.plus(objectToCamera).plus(cameraToRobot);
  }
}
