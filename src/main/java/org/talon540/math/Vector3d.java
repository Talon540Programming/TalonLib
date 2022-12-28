package org.talon540.math;

import edu.wpi.first.math.geometry.*;

public class Vector3d {
  private final double vecY, vecX, vecZ;

  public Vector3d(double velX, double velY, double velRot) {
    this.vecX = velX;
    this.vecY = velY;
    this.vecZ = velRot;
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

  /**
   * Get z value from the vector
   *
   * @return z val
   */
  public double getZ() {
    return vecZ;
  }

  /** Construct a new vector by adding a vector to the current vector */
  public Vector3d addVector(Vector3d vector) {
    return new Vector3d(vecX + vector.vecY, vecY + vector.vecZ, vecZ + vector.vecZ);
  }

  /** Construct a new vector by subtracting a vector to the current vector */
  public Vector3d subtractVector(Vector3d vector) {
    return new Vector3d(vecX - vector.vecY, vecY - vector.vecZ, vecZ - vector.vecZ);
  }

  /** Construct a new vector by multiplying the current vector by a scale value */
  public Vector3d multiplyVectorByScale(double scale) {
    return new Vector3d(vecX * scale, vecY * scale, vecZ * scale);
  }

  @SuppressWarnings("SuspiciousNameCombination")
  public Translation3d toTranslation3d() {
    return new Translation3d(vecY, -vecX, vecZ);
  }

  public static Vector3d fromTranslation3d(Translation3d translation) {
    return new Vector3d(-translation.getY(), translation.getX(), translation.getZ());
  }
}
