package org.talon540.mapping.position;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

import java.util.TreeMap;


/**
 * An object used in the logging and retrieving of the Robot's position on the field in the form of a {@link Pose2d}
 * object
 */
public class UnboundRobotPositionMap implements Sendable {
    protected final TreeMap<Double, Pose2d> map = new TreeMap<>();

    /**
     * Add a position to the position map
     *
     * @param position position of the robot in the form of a {@link Pose2d} object
     * @param timestamp timestamp key
     */
    public void addPositionToMap(Pose2d position, double timestamp) {
        this.map.put(
                timestamp,
                position
        );
    }

    /**
     * Get the last entered position in the position map. Returns null if the map is empty
     */
    public Pose2d getLatestPosition() {
        return this.map.isEmpty() ? null : this.map.lastEntry().getValue();
    }

    /**
     * Get the estimated position of the robot at a specific time. If the time provided is between two different
     * positions, it will interpolate the difference between the two timeslots and return the estimated position.
     * Returns null if the map is empty
     *
     * @param timestamp timestamp to reference
     * @return robot's position on the field
     */
    public Pose2d getPositionFromTimestamp(double timestamp) {
        if (this.map.isEmpty())
            return null;

        if (timestamp <= this.map.firstKey()) {
            return this.map.firstEntry().getValue();
        }

        if (timestamp >= this.map.lastKey()) {
            return this.map.lastEntry().getValue();
        }

        double lowerKeyBound = this.map.floorKey(timestamp);
        double ceilingKeyBound = this.map.ceilingKey(timestamp);

        return this.map.get(lowerKeyBound).interpolate(
                this.map.get(ceilingKeyBound),
                (timestamp - lowerKeyBound) / ((ceilingKeyBound - lowerKeyBound))
        );
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty(
                "count",
                map::size,
                null
        );
        builder.addStringProperty(
                "cPosition",
                () -> this.map.isEmpty() ? "Empty Map" : getLatestPosition().toString(),
                null
        );
        builder.addDoubleProperty(
                "cTimestamp",
                () -> this.map.isEmpty() ? 0 : map.firstKey(),
                null
        );
    }
}
