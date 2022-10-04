package org.talon540.mapping;

import java.util.TreeMap;

import edu.wpi.first.math.geometry.Pose2d;

public class UnboundRobotPositionMap {
    private TreeMap<Double, Pose2d> map = new TreeMap<Double, Pose2d>();

    public void addPositionToMap(double timestamp, Pose2d position) {
        this.map.put(timestamp, position);
    }

    public Pose2d getLatestPosition() {
        return this.map.lastEntry().getValue();
    }

    public Pose2d getPositionFromTimestamp(double timestamp) {
        if (timestamp <= this.map.firstKey()) {
            return this.map.firstEntry().getValue();
        }

        if (timestamp >= this.map.lastKey()) {
            return this.map.lastEntry().getValue();
        }

        double lowerKeyBound = this.map.floorKey(timestamp);
        double ceilingKeyBound = this.map.ceilingKey(timestamp);

        return this.map.get(lowerKeyBound).interpolate(this.map.get(ceilingKeyBound),(timestamp - lowerKeyBound) / ((ceilingKeyBound - lowerKeyBound)));
    }
}
