package edu.puc.core2.execution.structures.CDS.time;

import java.util.HashMap;

public abstract class CDSTimeNode {

    abstract public boolean isBottom();

    /**
     * Maximum-start, denoted max(n).
     */
    abstract public long getMax();

    /**
     * Descending-paths binary relation.
     */
    abstract protected HashMap<Long, Integer> getPaths();

    /**
     * Descending-paths count.
     * */
    abstract public int getPathsCount(long currentTime, long windowDelta);
}