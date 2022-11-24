package org.talon540.mapping.position;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

import java.util.Map;
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
    public void addPose(Pose2d position, double timestamp) {
        this.map.put(
                timestamp,
                position
        );
    }

    /**
     * Get the last entered position in the position map. Returns null if the map is empty
     */
    public Pose2d getLatestPose() {
        Map.Entry<Double, Pose2d> pos = this.map.lastEntry();

        return pos != null ? pos.getValue() : null;
    }

    /**
     * Get the estimated position of the robot at a specific timestamp. If the timestamp requested is between two
     * different positions, it will interpolate the difference between the two positions and return the estimated
     * position using {@link Pose2d#interpolate(Pose2d, double)}
     *
     * @param timestamp timestamp to reference
     * @return robot's position on the field
     */
    public Pose2d getPositionFromTimestamp(double timestamp) {
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
        builder.addStringProperty(
                "Current Pose",
                () -> {
                    Pose2d currentPose = getLatestPose();
                    return currentPose != null ? currentPose.toString() : "N/A";
                },
                null
        );

        builder.addDoubleProperty(
                "Size",
                map::size,
                null
        );
    }
}
