package org.talon540.drive.swerve.trajectory;

import edu.wpi.first.math.geometry.Pose2d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class TalonTrajectory {
    public final String TrajectoryName;
    public final List<TrajectoryNode> trajectoryList = new ArrayList<>();

    /**
     * Max translational velocity in {@code meters per second}
     * Override this value if you don't want to use the default value (defined in rbt code)
     */
    public final double kMaxTranslationalVelocity;
    /**
     * Max translational acceleration in {@code meters per second squared}
     * Override this value if you don't want to use the default value (defined in rbt code)
     */
    public final double kMaxTranslationalAcceleration;
    /**
     * Max rotational angular velocity in {@code radians per second}
     * Override this value if you don't want to use the default value (defined in rbt code)
     */
    public final double kMaxRotationalVelocity;
    /**
     * Max rotational angular acceleration in {@code radians per second squared}
     * Override this value if you don't want to use the default value (defined in rbt code)
     */
    public final double kMaxRotationalAcceleration;


    public TalonTrajectory(String name, double maxTVel, double maxTAcc, double maxRVel, double maxRAcc) {
        this.TrajectoryName = name;

        this.kMaxTranslationalVelocity = maxTVel;
        this.kMaxTranslationalAcceleration = maxTAcc;

        this.kMaxRotationalVelocity = maxRVel;
        this.kMaxRotationalAcceleration = maxRAcc;
    }

    public TalonTrajectory(String name) {
        this(name, 0, 0, 0, 0);
    }

    /**
     * Add an array of data-points to the trajectory
     *
     * @param points { time, posX, posY, posRotRad, velX, velY, velRotRadPerS }
     */
    public void addPointsToTrajectory(double[][] points) {
        for (double[] data : points) {
            addPointToTrajectory(data);
        }
    }

    /**
     * Add a single datapoint to the trajectory
     *
     * @param point { time, posX, posY, posRotRad, velX, velY, velRotRadPerS }
     */
    public void addPointToTrajectory(double[] point) {
        this.trajectoryList.add(TrajectoryNode.fromData(point));
    }

    /**
     * Return the first node in this trajectory
     */
    public TrajectoryNode getStartingNode() {
        return this.trajectoryList.get(0);
    }

    /**
     * Return the last node in this trajectory
     */
    public TrajectoryNode getEndingNode() {
        return this.trajectoryList.get(this.trajectoryList.size() - 1);
    }

    /**
     * Get the total runtime of a trajectory in seconds
     *
     * @return runtime in seconds
     */
    public double getTrajectoryTime() {
        return getEndingNode().time;
    }

    /**
     * Return the difference in runtime between two nodes
     *
     * @return remaining runtime
     */
    public double getTrajectoryTime(int index) {
        return getTrajectoryTime() - this.trajectoryList.get(index).time;
    }

    /**
     * Return the remaining time from the provided time
     *
     * @param estimated if true, subtracts the provided time, else conduct a binary
     * search to find the correct node and subtract that time
     * @return remaining runtime
     */
    public double getTrajectoryTime(double time, boolean estimated) {
        return getTrajectoryTime() - (estimated ? time : getNodeFromTime(time).time);
    }

    /**
     * Return the estimated total length of the trajectory
     */
    public double getTrajectoryLength() {
        double distance = 0;

        // Subtract two, so we don't throw out of bounds exception
        for (int i = 0; i < trajectoryList.size() - 2; i++) {
            Pose2d currentPosition = trajectoryList.get(i).position;
            Pose2d nextPosition = trajectoryList.get(i + 1).position;

            distance += Math.sqrt(Math.pow(nextPosition.getX() - currentPosition.getX(), 2) + Math.pow(nextPosition.getY() - currentPosition.getY(), 2));
        }

        return distance;

    }

    /**
     * Get the remaining trajectory length from a specific node
     *
     * @return remaining length
     */
    public double getTrajectoryLength(int index) {
        if (index == 0)
            return getTrajectoryLength();
        if (index > trajectoryList.size())
            return -1;

        double distance = 0;

        // Subtract two, so we don't throw out of bounds exception
        for (int i = index; i < trajectoryList.size() - 2; i++) {
            Pose2d currentPosition = trajectoryList.get(i).position;
            Pose2d nextPosition = trajectoryList.get(i + 1).position;

            distance += Math.sqrt(Math.pow(nextPosition.getX() - currentPosition.getX(), 2) + Math.pow(nextPosition.getY() - currentPosition.getY(), 2));
        }

        return distance;
    }

    /**
     * Get the remaining trajectory length from a specific node
     *
     * @return remaining length
     */
    public double getTrajectoryLength(double time) {
        try {
            // IDK how java streams work so im just gonna assume this works
            // List<TrajectoryNode> shortenedList = trajectoryList.stream().filter(node ->
            // time < node.time).collect(Collectors.toList());
            // List<TrajectoryNode> shortenedList =
            // trajectoryList.subList(getNodeIndexFromTime(time), this.trajectoryList.size()
            // - 1);

            List<TrajectoryNode> shortenedList = new ArrayList<>();

            shortenedList.add(getNodeFromTime(time));
            shortenedList.addAll(trajectoryList.subList(getNodeIndexFromTime(time), this.trajectoryList.size() - 1));

            double distance = 0;

            // Subtract two, so we don't throw out of bounds exception
            for (int i = 0; i < shortenedList.size() - 2; i++) {
                Pose2d currentPosition = shortenedList.get(i).position;
                Pose2d nextPosition = shortenedList.get(i + 1).position;

                distance += Math.sqrt(Math.pow(nextPosition.getX() - currentPosition.getX(), 2) + Math.pow(nextPosition.getY() - currentPosition.getY(), 2));
            }

            return distance;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Sort the trajectory list by time, only needed if random points are inserted
     */
    public void sortTrajectoryList() {
        trajectoryList.sort(Comparator.comparingDouble(firstNode -> firstNode.time));
    }

    /**
     * Conduct a binary search on trajectory nodes to find upper bound node index
     */
    public int getNodeIndexFromTime(double time) {
        // Make sure the input time is in bound
        if (time <= getStartingNode().time)
            return 0;
        if (time >= getEndingNode().time)
            return this.trajectoryList.size() - 1;

        // Conduct a binary search to find current trajectory node
        int leftBound = 0;
        int rightBound = trajectoryList.size() - 1;

        while (leftBound != rightBound) {
            int midpoint = (leftBound + rightBound) / 2;

            if (trajectoryList.get(midpoint).time < time) {
                leftBound = midpoint + 1;
            } else {
                rightBound = midpoint;
            }
        }

        return leftBound;
    }

    /**
     * Return a TrajectoryNode from the Trajectory using the time provided.
     * If the exact time of the requested node is not found, it will instead
     * interpolate the requested node.
     * If the given time is under or over the total time, the min or max node is
     * returned respectively
     *
     * @param time in {@code seconds}
     * @return {@link TrajectoryNode} at requested time
     */
    public TrajectoryNode getNodeFromTime(double time) {
        int currentIndex = getNodeIndexFromTime(time);

        TrajectoryNode lastNode = trajectoryList.get(currentIndex - 1);
        TrajectoryNode currentNode = trajectoryList.get(currentIndex);

        if (currentNode.time - lastNode.time < 1E-9)
            return currentNode;

        return lastNode.interpolateNode(currentNode, (time - lastNode.time) / (currentNode.time - lastNode.time));
    }

}
