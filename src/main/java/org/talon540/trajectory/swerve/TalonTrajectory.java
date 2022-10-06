package org.talon540.trajectory.swerve;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;

public abstract class TalonTrajectory {
    public List<TrajectoryNode> trajectoryList;

    /** Override this value if you don't want to use the default value */
    public double kMaxTranslationalVelocity;
    /** Override this value if you don't want to use the default value */
    public double kMaxAccelerationSquared;
    /** Override this value if you don't want to use the default value */
    public double kMaxRotationalVelocity;

    /**
     * Add an array of datapoints to the trajectory
     * 
     * @param points { time, posX, posY, posRotRad, velX, velY, velRotRadPerS }
     */
    public void addPointsToTrajectory(double[][] points) {
        for (int i = 0; i < points.length; i++) {
            addPointToTrajectory(points[i]);
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
    public double totalTrajectoryTime() {
        return getEndingNode().time;
    }

    /**
     * Return the estimated total length of the trajectory
     */
    public double totalTrajectoryLength() {
        double distance = 0;

        // Subtract two so we dont throw out of bounds excpetion
        for (int i = 0; i < trajectoryList.size() - 2; i++) {
            Pose2d currentPosition = trajectoryList.get(i).position;
            Pose2d nextPosition = trajectoryList.get(i + 1).position;

            distance += Math.sqrt(Math.pow(nextPosition.getX() - currentPosition.getX(), 2)
                    + Math.pow(nextPosition.getY() - currentPosition.getY(), 2));
        }

        return distance;

    }

    /**
     * Sort the trajectory list by time, only needed if random points are inserted
     */
    public void sortTrajectoryList() {
        trajectoryList.sort((firstNode, secondNode) -> firstNode.time < secondNode.time ? -1 : 1);
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
        TrajectoryNode startingNode = getStartingNode();
        TrajectoryNode endingNode = getEndingNode();

        // Make sure the input time is in bound
        if (time <= startingNode.time) return startingNode;
        if (time >= endingNode.time) return endingNode;

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

        TrajectoryNode lastNode = trajectoryList.get(leftBound - 1);
        TrajectoryNode currentNode = trajectoryList.get(leftBound);

        if (currentNode.time - lastNode.time == 0) return currentNode;

        return lastNode.interpolateNode(currentNode, (time - lastNode.time) / (currentNode.time - lastNode.time));
    }

}
