package org.talon540.drive.swerve.trajectory;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import org.talon540.math.Vector3d;

/**
 * Repersents a point on a trajectory path with a robot's position and speed at
 * that point on the trajectory
 */
public class TrajectoryNode {
    public double time;
    public Pose2d position;
    public Vector3d velocity;

    /**
     * @param time     timepoint of this point in the trajectory in seconds
     * @param position {@link Pose2d} of the robot's position at this point
     * @param velocity {@link} Vector3d} of the robot's velocity at that time
     */
    public TrajectoryNode(
            double time,
            Pose2d position,
            Vector3d velocity) {
        this.time = time;
        this.position = position;
        this.velocity = velocity;
    }

    /**
     * 
     * @param time         timepoint of this point in the trajectory in
     *                     {@code seconds}
     * @param posX         horizontal position {@code meters}
     * @param posY         vertical positon {@code meters}
     * @param posDirection facing direction in {@code radians}
     * @param velX         strafe velocity in {@code m/s}
     * @param velY         forward velocity in {@code m/s}
     * @param velRotation  rotational velocity in {@code rad/s}
     */
    public TrajectoryNode(
            double time,
            double posX,
            double posY,
            double posDirection,
            double velX,
            double velY,
            double velRotation) {
        this(time, new Pose2d(new Translation2d(posX, posY), new Rotation2d(posDirection)),
                new Vector3d(velX, velY, velRotation));
    }

    /**
     * Create a Trajectory node from it's data, mostly used for reading from a file
     * 
     * @param data
     * Note:  must be of len 7, even if a datapoint is 0 (which it shouldn't be)
     *           add a zero for blank spaces or an exception is thrown
     */
    public static TrajectoryNode fromData(double... data) {
        if (data.length != 7) {
            throw new IllegalArgumentException();
        }

        return new TrajectoryNode(data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
    }

    /**
     * Interpolate a Trajectory Node from the current node and a ceiling node
     * 
     * @param ceilingNode Ceiling node
     * @param pos         where the requested node is between the current node and
     *                    higher node within [0, 1]
     * @return interpolated {@link TrajectoryNode}
     * Note:  assumes this node is the floor node
     */
    public TrajectoryNode interpolateNode(TrajectoryNode ceilingNode, double pos) {
        // Uses forumula:
        // ratio(ceiling - current) + current
        Vector3d startVector = Vector3d.fromPose2d(position);
        Vector3d endVector = Vector3d.fromPose2d(ceilingNode.position);

        double interpolatedTime = (ceilingNode.time - time) * pos + time;
        Pose2d interpolatedPose2d = (((endVector.substractVector(startVector)).multiplyVectorByScale(pos))
                .addVector(startVector)).toPose2d();
        // Pose2d interpolatedPose2d = position.interpolate(ceilingNode.position, pos);
        // // idk if this works
        Vector3d interpolatedVector3d = ((ceilingNode.velocity.substractVector(velocity)).multiplyVectorByScale(pos))
                .addVector(velocity);

        return new TrajectoryNode(interpolatedTime, interpolatedPose2d, interpolatedVector3d);
    }

}
