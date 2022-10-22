package org.talon540.mapping.position;

import edu.wpi.first.math.geometry.Pose2d;

/**
 * Create a type of {@link UnboundRobotPositionMap} which has a specific limit
 * on the number of entries that can be made into the map. This is usually for
 * memory and speed purposes
 */
public class BoundRobotPositionMap extends UnboundRobotPositionMap {
    private int mapSize;

    /**
     * Create a position map object with a max number of entries
     * 
     * @param mapSize max number of entries in the map
     */
    public BoundRobotPositionMap(int mapSize) {
        this.mapSize = mapSize;
    }

    @Override
    public void addPositionToMap(Pose2d position, double timestamp) {
        super.map.put(timestamp, position);
        if (super.map.size() > this.mapSize)
            super.map.remove(super.map.firstKey());
    }

}
