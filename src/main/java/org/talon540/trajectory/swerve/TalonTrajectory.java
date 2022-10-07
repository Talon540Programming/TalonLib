package org.talon540.trajectory.swerve;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;

public class TalonTrajectory {
    public String TrajectoryName;
    public List<TrajectoryNode> trajectoryList = new ArrayList<TrajectoryNode>();

    /** Override this value if you don't want to use the default value */
    public double kMaxTranslationalVelocity;
    /** Override this value if you don't want to use the default value */
    public double kMaxAccelerationSquared;
    /** Override this value if you don't want to use the default value */
    public double kMaxRotationalVelocity;

    public TalonTrajectory(String name, double maxTrans, double maxAccel, double maxRot) {
        this.TrajectoryName = name;
        this.kMaxTranslationalVelocity = maxTrans;
        this.kMaxAccelerationSquared = maxAccel;
        this.kMaxRotationalVelocity = maxRot;
    }

    public TalonTrajectory(String name) {
        this(name, 0, 0 ,0);
    }

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
    public double getTrajectoryTime() {
        return getEndingNode().time;
    }

    /**
     * Return the difference in runtime between two nodes
     * 
     * @param index
     * @return remaining runtime
     */
    public double getTrajectoryTime(int index) {
        return getTrajectoryTime() - this.trajectoryList.get(index).time;
    }

    /**
     * Return the remaining time from the provided time
     * 
     * @param time
     * @param estimated if true, substracts the provided time, else conduct a binary
     *                  search to find the correct node and substract that time
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
     * Get the remaining trajectory length from a specific node
     * 
     * @param index
     * @return remaining length
     */
    public double getTrajectoryLength(int index) {
        if (index == 0)
            return getTrajectoryLength();
        if (index > trajectoryList.size())
            return -1;

        double distance = 0;

        // Subtract two so we dont throw out of bounds excpetion
        for (int i = index; i < trajectoryList.size() - 2; i++) {
            Pose2d currentPosition = trajectoryList.get(i).position;
            Pose2d nextPosition = trajectoryList.get(i + 1).position;

            distance += Math.sqrt(Math.pow(nextPosition.getX() - currentPosition.getX(), 2)
                    + Math.pow(nextPosition.getY() - currentPosition.getY(), 2));
        }

        return distance;
    }

    /**
     * Get the remaining trajectory length from a specific node
     * 
     * @param time
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

            List<TrajectoryNode> shortenedList = new ArrayList<TrajectoryNode>();

            shortenedList.add(getNodeFromTime(time));
            shortenedList.addAll(trajectoryList.subList(getNodeIndexFromTime(time), this.trajectoryList.size() - 1));

            double distance = 0;

            // Subtract two so we dont throw out of bounds excpetion
            for (int i = 0; i < shortenedList.size() - 2; i++) {
                Pose2d currentPosition = shortenedList.get(i).position;
                Pose2d nextPosition = shortenedList.get(i + 1).position;

                distance += Math.sqrt(Math.pow(nextPosition.getX() - currentPosition.getX(), 2)
                        + Math.pow(nextPosition.getY() - currentPosition.getY(), 2));
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
        trajectoryList.sort((firstNode, secondNode) -> firstNode.time < secondNode.time ? -1 : 1);
    }

    /**
     * Conduct a binary search on trajectory nodes to find upper bound node index
     * 
     * @param time
     * @return
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

        if (currentNode.time - lastNode.time == 0)
            return currentNode;

        return lastNode.interpolateNode(currentNode, (time - lastNode.time) / (currentNode.time - lastNode.time));
    }

}
