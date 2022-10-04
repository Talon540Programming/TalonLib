package org.talon540.mapping;

import edu.wpi.first.math.geometry.Pose2d;

public class BoundRobotPositionTreeMap extends UnboundRobotPositionMap {
    private int mapSize;

    public BoundRobotPositionTreeMap(int mapSize) {
        this.mapSize = mapSize;
    }

    @Override
    public void addPositionToMap(Pose2d position, double timestamp) {
        super.map.put(timestamp, position);
        if (super.map.size() > this.mapSize) super.map.remove(super.map.firstKey());
    }

}
