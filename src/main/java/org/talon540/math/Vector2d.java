package org.talon540.math;

import edu.wpi.first.math.geometry.Translation2d;

public class Vector2d {
  private final double vecX, vecY;

  public Vector2d(double vecX, double vecY) {
    this.vecX = vecX;
    this.vecY = vecY;
  }

  /**
   * Get X value from the vector
   *
   * @return x val
   */
  public double getX() {
    return vecX;
  }

  /**
   * Get Y value from the vector
   *
   * @return y val
   */
  public double getY() {
    return vecY;
  }

  /** Construct a new vector by adding a vector to the current vector */
  public Vector2d addVector(Vector2d vector) {
    return new Vector2d(vecX + vector.vecX, vecY + vector.vecY);
  }

  /** Construct a new vector by subtracting a vector to the current vector */
  public Vector2d subtractVector(Vector2d vector) {
    return new Vector2d(vecX - vector.vecX, vecY - vector.vecY);
  }

  /** Construct a new vector by multiplying the current vector by a scale value */
  public Vector2d multiplyVectorByScale(double scale) {
    return new Vector2d(vecX * scale, vecY * scale);
  }

  /**
   * Get the Translation from the origin of the plane in terms of a {@link Translation2d}
   * Mathematical equivalent of spinning the robot 90 degrees clockwise about the origin (facing
   * y-axis to facing x-axis)
   *
   * @return {@link Translation2d} in traditional mathematical terms
   */
  @SuppressWarnings("SuspiciousNameCombination")
  public Translation2d getTranslationFromOrigin() {
    return new Translation2d(vecY, -vecX);
  }

  /**
   * Construct a {@link Vector2d} from a {@link Translation2d}. Useful for using school math in code
   * math
   *
   * @param translation translation to derive vector from
   * @return {@link Vector2d} based on translation
   */
  public static Vector2d fromTranslation2d(Translation2d translation) {
    return new Vector2d(-translation.getY(), translation.getX());
  }
}
