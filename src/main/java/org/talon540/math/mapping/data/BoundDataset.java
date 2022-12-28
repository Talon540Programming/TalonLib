package org.talon540.math.mapping.data;

public class BoundDataset extends UnboundDataset {
  private final int limit;

  public BoundDataset(int limit) {
    this.limit = limit;
  }

  @Override
  public void addNode(double val) {
    if (this.limit <= super.nodeList.size()) super.nodeList.remove(0);
    super.addNode(val);
  }
}
