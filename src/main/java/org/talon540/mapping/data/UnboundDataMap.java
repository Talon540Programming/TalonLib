package org.talon540.mapping.data;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

import java.util.ArrayList;
import java.util.List;

public class UnboundDataMap implements Sendable {
    protected List<Double> nodeList = new ArrayList<Double>();

    /**
     * Add datapoint to dataset
     * @param val value to add
     */
    public void addNode(double val) {
        nodeList.add(val);
    }

    /**
     * Get average of the dataset
     */
    public double getAverage() {
        if(nodeList.size() == 0) return 0;

        try {
            return nodeList.stream().mapToDouble(a -> a).average().getAsDouble();
        } catch (Exception e) {
            double sum = 0;
            for(int i = 0; i< nodeList.size() - 1; i++) {
                sum += nodeList.get(i);
            }

            return sum / nodeList.size();
        }
    }

    /**
     * Get variance of dataset
     */
    public double getVariance() {
        if(nodeList.size() == 0) return 0;

        double mean = getAverage();

        double sum = 0;
        for(int i = 0; i< nodeList.size() - 1; i++) {
            sum += (Math.pow(nodeList.get(i) - mean, 2));
        }

        return sum / (nodeList.size() - 1);
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
    public int getNodeCount() {
        return nodeList.size();
    }

    /**
     * Reset the dataset
     */
    public void clearList() {
        nodeList.clear();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Node Count", () -> nodeList.size(), null);
        builder.addDoubleProperty("Average", this::getAverage, null);
        builder.addDoubleProperty("Variance", this::getVariance, null);
        builder.addDoubleProperty("Standard Deviance", this::getStandardDeviation, null);
    }
}
