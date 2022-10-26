package org.talon540.mapping.data;

public class BoundDataMap extends UnboundDataMap {
    private final int limit;

    public BoundDataMap(int limit) {
        this.limit = limit;
    }

    @Override
    public void addNode(double val) {
        if (this.limit <= super.nodeList.size())
            super.nodeList.remove(0);
        super.addNode(val);
    }
}
