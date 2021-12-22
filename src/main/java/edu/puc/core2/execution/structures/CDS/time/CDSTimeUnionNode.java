package edu.puc.core2.execution.structures.CDS.time;

import java.lang.ref.WeakReference;

public class CDSTimeUnionNode extends CDSTimeNode {

    private final WeakReference<CDSTimeNode> left;
    private final WeakReference<CDSTimeNode> right;
    /** Recall max(left(u)) >= max(right(u)) */
    private final long max;
    private final int paths;

    /** Use {@link CDSNodeManager} to create CDSTimeNodes. */
    CDSTimeUnionNode(WeakReference<CDSTimeNode> left, WeakReference<CDSTimeNode> right) {
        CDSTimeNode tempLeft = left.get();
        CDSTimeNode tempRight = right.get();
        this.left = left;
        this.right = right;
        // If left is null, right will also be null.
        // This union node will point to something already outside the time-window, therefore we can set max(u) = 0
        // and wait for the next `prune` to remove this union node pointing to the ether.
        this.max = tempLeft != null ? tempLeft.getMax() : 0;
        this.paths = (tempLeft != null ? tempLeft.getPaths() : 0) + (tempRight != null ? tempRight.getPaths() : 0);
    }

    public CDSTimeNode getRight() {
        return this.right.get();
    }

    public CDSTimeNode getLeft() {
        return this.left.get();
    }

    public WeakReference<CDSTimeNode> getRightReference() {
        return this.right;
    }

    public WeakReference<CDSTimeNode> getLeftReference() {
        return this.left;
    }

    @Override
    public boolean isBottom() {
        return false;
    }

    @Override
    public long getMax() {
        return this.max;
    }

    @Override
    public int getPaths() {
        return this.paths;
    }
}
