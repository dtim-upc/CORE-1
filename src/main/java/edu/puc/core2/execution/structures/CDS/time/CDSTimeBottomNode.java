package edu.puc.core2.execution.structures.CDS.time;

import java.util.HashMap;

public class CDSTimeBottomNode extends CDSTimeNode {

    private final long max;

    private final HashMap<Long, Integer> paths;

    // Use with care since the current time is always wrong!
    public static final CDSTimeBottomNode BOTTOM = new CDSTimeBottomNode(0, null);

    CDSTimeBottomNode(long currentTime, HashMap<Long, Integer> paths) {
        this.max = currentTime;
        this.paths = paths;
    }

    /** Use {@link CDSNodeManager} to create CDSTimeNodes. */
    CDSTimeBottomNode(long currentTime) {
        this.max = currentTime;
        this.paths = new HashMap<>(1);
        this.paths.put(currentTime, 1);
    }

    @Override
    public long getMax() {
        return max;
    }

    @Override
    public boolean isBottom() {
        return true;
    }

    @Override
    protected HashMap<Long, Integer> getPaths() {
        return this.paths;
    }

    @Override
    public int getPathsCount(long currentTime, long windowDelta) {
        return (this.max > currentTime - windowDelta ? 1 : 0);
    }
}

