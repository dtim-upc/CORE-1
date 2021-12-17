package edu.puc.core2.execution.structures.CDS.time;

public class CDSTimeBottomNode extends CDSTimeNode {

    private final long max;

    // Use with care since the current time is always wrong!
    public static final CDSTimeBottomNode BOTTOM = new CDSTimeBottomNode(0);

    /** Use {@link CDSNodeManager} to create CDSTimeNodes. */
    CDSTimeBottomNode(long currentTime) {
        this.max = currentTime;
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
    public int getPaths() {
        return 1;
    }
}

