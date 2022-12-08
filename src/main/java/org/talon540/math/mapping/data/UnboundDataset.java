package org.talon540.math.mapping.data;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

import java.util.ArrayList;
import java.util.List;


public class UnboundDataset implements Sendable {
    protected final List<Double> nodeList = new ArrayList<>();

    /**
     * Add datapoint to dataset
     *
     * @param val value to add
     */
    public void addNode(double val) {
        nodeList.add(val);
    }

    /**
     * Get average of the dataset
     */
    public double getAverage() {
        if (nodeList.size() == 0)
            return 0;

        return nodeList.stream().mapToDouble(a -> a).average().orElse(0);
    }

    /**
     * Get variance of dataset
     */
    public double getVariance() {
        if (nodeList.size() == 0)
            return 0;

        double mean = getAverage();
        return nodeList.stream().mapToDouble(a -> Math.pow(
                a - mean,
                2
        )).sum() / (nodeList.size() - 1);
    }

    /**
     * Get standard deviation of the dataset
     */
    public double getStandardDeviation() {
        return Math.sqrt(getVariance());
    }

    /**
     * Get the number of data-points in the dataset
     */
    public int getDatasetSize() {
        return nodeList.size();
    }

    /**
     * Reset the dataset
     */
    public void clearDataset() {
        nodeList.clear();
    }

    /**
     * Check if a values lies outside 2.5 standard deviations of the mean or is an outlier of the dataset
     *
     * @param val value to check
     * @return whether value is an outlier
     */
    public boolean isOutlier(double val) {
        return val > 2.5 * getStandardDeviation();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty(
                "Node Count",
                this::getDatasetSize,
                null
        );
        builder.addDoubleProperty(
                "Average",
                this::getAverage,
                null
        );
        builder.addDoubleProperty(
                "Variance",
                this::getVariance,
                null
        );
        builder.addDoubleProperty(
                "Standard Deviance",
                this::getStandardDeviation,
                null
        );
    }
}
