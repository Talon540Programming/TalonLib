package org.talon540.mapping;

import java.util.TreeMap;

import edu.wpi.first.math.geometry.Pose2d;

public class RobotPositionTreeMap {
    private TreeMap<Double, Pose2d> treeContent;
    private int treeSize;

    public RobotPositionTreeMap(int mapSize) {
        this.treeSize = mapSize;
        this.treeContent = new TreeMap<Double, Pose2d>();
    }

    public void addPositionToMap(Pose2d position, double timestamp) {
        this.treeContent.put(timestamp, position);
        if (this.treeContent.size() > this.treeSize) {
            this.treeContent.remove(this.treeContent.firstKey());
        }
    }

    public Pose2d getLatestPosition() {
        return this.treeContent.lastEntry().getValue();
    }

    public Pose2d getPoseFromTimestamp(double timestamp) {
        if (timestamp <= this.treeContent.firstKey()) {
            return this.treeContent.firstEntry().getValue();
        }

        if (timestamp >= this.treeContent.lastKey()) {
            return this.treeContent.lastEntry().getValue();
        }

        double lowerKeyBound = this.treeContent.floorKey(timestamp);
        double ceilingKeyBound = this.treeContent.ceilingKey(timestamp);

        return this.treeContent.get(lowerKeyBound).interpolate(this.treeContent.get(ceilingKeyBound),(timestamp - lowerKeyBound) / ((ceilingKeyBound - lowerKeyBound)));
    }
}
