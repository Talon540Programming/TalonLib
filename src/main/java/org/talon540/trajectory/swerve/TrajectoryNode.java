package org.talon540.trajectory.swerve;

import org.talon540.math.Vector3d;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * Repersents a point on a trajectory path with a robot's position and speed at that point on the trajectory
 */
public class TrajectoryNode {
    public TrajectoryNode(
        double time,
        Pose2d position,
        Vector3d velocity
    ) {}

    public TrajectoryNode(
        double time,
        double posX,
        double posY,
        double posDirection,
        double velX,
        double velY,
        double velRotation
        ) 
    {
            this(time, new Pose2d(new Translation2d(posX, posY), new Rotation2d(posDirection)), new Vector3d(velX, velY, velRotation));
    }

    public static TrajectoryNode fromPoint(double... pointInfo) {
        if(pointInfo.length != 6) {
            throw new IllegalArgumentException();
        }

        return new TrajectoryNode(pointInfo[0], new Pose2d(new Translation2d(pointInfo[1], pointInfo[2]), new Rotation2d(pointInfo[3])), new Vector3d(pointInfo[4], pointInfo[5], pointInfo[6]));
    }

}
