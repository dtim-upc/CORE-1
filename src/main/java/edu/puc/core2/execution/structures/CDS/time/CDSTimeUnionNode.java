package edu.puc.core2.execution.structures.CDS.time;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class CDSTimeUnionNode extends CDSTimeNode {

    private final WeakReference<CDSTimeNode> left;
    private final WeakReference<CDSTimeNode> right;
    /** Recall max(left(u)) >= max(right(u)) */
    private final long max;
    private final HashMap<Long, Integer> paths;

    /** Use {@link CDSNodeManager} to create CDSTimeNodes. */
    CDSTimeUnionNode(WeakReference<CDSTimeNode> left, WeakReference<CDSTimeNode> right, long currentTime, long windowDelta) {
        CDSTimeNode tempLeft = left.get();
        CDSTimeNode tempRight = right.get();
        this.left = left;
        this.right = right;
        // If left is null, right will also be null.
        // This union node will point to something already outside the time-window, therefore we can set max(u) = 0
        // and wait for the next `prune` to remove this union node pointing to the ether.
        this.max = tempLeft != null ? tempLeft.getMax() : 0;
        this.paths = unionOfPaths(tempLeft, tempRight, currentTime - windowDelta);
    }

    /** We iterate over all entries of left and right's node paths attribute that are inside the time-window. */
    private HashMap<Long, Integer> unionOfPaths(CDSTimeNode l, CDSTimeNode r, long threshold) {
        int initialCapacity = (l == null ? 0 : l.getPaths().keySet().size()) + (r == null ? 0 : r.getPaths().keySet().size());
        HashMap<Long, Integer> map = new HashMap<>(initialCapacity);
        if (l != null) {
            for (Map.Entry<Long, Integer> entry: l.getPaths().entrySet()) {
               if (entry.getKey() > threshold)  {
                   map.put(entry.getKey(), entry.getValue());
               }
            }
        }
        if (r != null) {
            for (Map.Entry<Long, Integer> entry: r.getPaths().entrySet()) {
                if (entry.getKey() > threshold)  {
                    map.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }
        }
        return map;
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
    protected HashMap<Long, Integer> getPaths() {
        return this.paths;
    }

    @Override
    public int getPathsCount(long currentTime, long windowDelta) {
        int count = 0;
        long threshold = currentTime - windowDelta;
        for (Long key : this.paths.keySet()) {
            if (key > threshold) {
                count += this.paths.get(key);
            }
        }
        return count;
    }
}
