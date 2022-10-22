package org.talon540.mapping.data;

public class BoundDataMap extends UnboudDataMap {
    private int limit;

    public BoundDataMap(int limit) {
        this.limit = limit;
    }

    @Override
    public void addNode(double val) {
        if(super.nodeList.size() > this.limit) super.nodeList.remove(0);
        super.addNode(val);
    }
}
